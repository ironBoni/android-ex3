import React, { useState, useEffect, useRef } from 'react'
import './ChatList.css';
import Contact from '../Contact/Contact'
import { users, chats, myServer, dataServer, aspMvcServer } from '../../Data/data'
import { Modal } from 'react-bootstrap';
import { HubConnection, HubConnectionBuilder } from "@microsoft/signalr";

function ChatList(props) {
    var id = props.username;
    var myContacts = useRef([]);
    var token = props.token;
    //var token = props.token;
    var setContactsAlready = false;
    // goes over the chat and find the contacts he talked with.
    // GET from the server the list of contact
    const [contactCon, setContactCon] = useState(null);
    const [usersList, setUsersList] = useState(users);
    const [contactsLst, setContactsLst] = useState([]);
    const [userImage, setUserImage] = useState('');
    const [name, setname] = useState('');
    const [showImageModal, setShowImageModal] = useState(false);
    const [errorAddUser, setErrorAddUser] = useState('')
    const [showAddModal, setShowAddModal] = useState(false);

    useEffect(() => {
        try {
            const connect = new HubConnectionBuilder()
                .withUrl(dataServer + "hub")
                .withAutomaticReconnect()
                .build();

            setContactCon(connect)
        }
        catch (e) { console.log(e); }
    }, []);

    useEffect(() => {
        if (contactCon) {
            contactCon.start()
                .then(result => {
                    contactCon.on("ReceiveContact", contact => {
                        myContacts.current.push({
                            name: contact.name, profileImage: '/images/default.jpg', id: contact.id,
                            server: contact.server, last: ''
                        });
                        setContactsLst(myContacts)
                    })
                    contactCon.invoke("SetIdInServer", props.username).then(res => { }).catch(e => console.log("Not connected"));
                })
                .catch(e => console.log('Connection failed: ', e));
        }
    }, [contactCon])

    useEffect(async () => {
        var config = {
            method: 'GET',
            headers: {
                'Accept': '*/*',
                'Accept-Encoding': 'gzip, deflate, br',
                'Connection': 'keep-alive',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            }
        }
        var res = await fetch(dataServer + "api/contacts/", config)
        var data = await res.json();
        myContacts.current = data;
        setContactsLst(data);
        setContactsAlready = true;

    }, []);

    useEffect(async () => {
        var id = props.username;
        // GET to get the server of the idToAdd 
        var config = {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token
            }
        }
        if (!id)
            id = 'noam';
        var res = await fetch(dataServer + "api/contacts/server/" + id, config);
        var response = await res.json();

        setUserImage(response.profileImage);
        setname(response.name);

    })

    async function addUserAsFriend() {
        var textBox = document.getElementById('contact-user');
        var textBoxNick = document.getElementById('contact-nickname');
        var textBoxServer = document.getElementById('contact-server');

        var idToAdd = textBox.value.trimEnd();
        var nickname = textBoxNick.value;
        var server = textBoxServer.value;
        
        if (!idToAdd) {
            console.log("dsssdddd");
            setErrorAddUser("id is required!");
            return;
        }
        if (!nickname) {
            setErrorAddUser("nickname is required!");
            return;
        }
        if (!server) {
            setErrorAddUser("server is required!");
            return;
        }


        try {
            await contactCon.invoke("AddContact", id, idToAdd, nickname, server);
        }
        catch (e) {
            console.log(e);
        }
        var myid = props.username;

        try {
            // GET to get the server of the idToAdd 
            var config = {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + token
                }
            }

            var res = await fetch(dataServer + "api/contacts/server/" + idToAdd, config);
            var response = await res.json();
            var profileImage = response.profileImage;

            if (idToAdd === myid) {
                setErrorAddUser("You cannot add yourself to the chat list.");
                return;
            }
        } catch (e) { console.log("The user you're adding is in another server.") }

        // POST request to add contact to server
        var data = { "id": idToAdd, "name": nickname, "server": server };
        var config = {
            method: 'POST',
            headers: {
                'accept': '*/*',
                'content-type': 'application/json',
                'Authorization': 'Bearer ' + token
            },
            body: JSON.stringify(data)
        }
        var res = await fetch(dataServer + "api/Contacts/", config);

        if (res.status === 400) {
            setErrorAddUser("This user is already in your contacts list.");
            return;
        }

        // End of POST
        chats.push({
            chatId: Math.floor(1000 * Math.random() + 200),
            participicants: [idToAdd, id],
            messages: []
        });
        var newContacts = [...myContacts.current];

        if (!profileImage || profileImage == undefined)
            profileImage = '/images/default.jpg';
        newContacts.push({
            name: nickname, profileImage: profileImage, id: idToAdd,
            server: server, last: ''
        });
        myContacts.current = newContacts;
        setContactsLst(newContacts);
        setErrorAddUser('');
        setShowAddModal(false);

        // Send Invitation to him
        // POST request - from (id), to (idToAdd), server (of me)
        data = { "from": id, "to": idToAdd, "server": dataServer };
        config = {
            method: 'POST',
            headers: {
                'accept': '*/*',
                'content-type': 'application/json'
            },
            body: JSON.stringify(data)
        }

        var url = server;
        if (url[url.length-1] !== "/")
                url = url + "/";
            if (url.indexOf("http://") == -1)
                url = "http://" + url;
        url = url.trim();
        url = url.replace(/\s/g, '');
        console.log("url is" + url);
        fetch(url + "api/invitations/", config).then(res => {
            console.log("invitations status: " + res.status);
        })
    };

    const addUserPressedEnter = (e) => {
        if (e.key === "Enter") {
            addUserAsFriend(e);
        }
    }

    useEffect(() => {
        /* when the react is the wwwroot folder built and static
         if the focus() function is called the focus is behaving unexpectedly.
        However, if focusing while "npm start", it works great
        in in the README we will ask the teacher to check with "npm start"*/
        var textBox = document.getElementById('contact-user');

        if (textBox && window.location.href.indexOf(aspMvcServer) < 0)
            textBox.focus();
    }, [showAddModal])

    return (
        <div className='col-3 border-right main-window'>
            <div className='chatList-container'>
                <div className='settings-tray'>
                    <button className='click-button' onClick={() => setShowImageModal(true)}>
                        <img className='user-image' alt='user' src={userImage}></img></button>
                    <span className='name'>{name}</span>
                    <span className="settings-tray--right float-right">
                        <i className="bi bi-person-plus">
                            <button className='click-button' onClick={() => setShowAddModal(true)}>
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                                    className="bi bi-person-plus" viewBox="0 0 16 16">
                                    <path
                                        d="M6 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6zm2-3a2 2 0 1 1-4 0 2 2 0 0 1 4 0zm4 8c0 1-1 1-1 1H1s-1 0-1-1 1-4 6-4 6 3 6 4zm-1-.004c-.001-.246-.154-.986-.832-1.664C9.516 10.68 8.289 10 6 10c-2.29 0-3.516.68-4.168 1.332-.678.678-.83 1.418-.832 1.664h10z"></path>
                                    <path fillRule="evenodd"
                                        d="M13.5 5a.5.5 0 0 1 .5.5V7h1.5a.5.5 0 0 1 0 1H14v1.5a.5.5 0 0 1-1 0V8h-1.5a.5.5 0 0 1 0-1H13V5.5a.5.5 0 0 1 .5-.5z"></path>
                                </svg>
                            </button>
                            <Modal show={showAddModal} centered onHide={() => {
                                setShowAddModal(false);
                                setErrorAddUser('');
                            }}>
                                <Modal.Header closeButton className='header'>
                                    Add Contact
                                </Modal.Header>
                                <Modal.Body>{
                                    <div>
                                        <div className='padd'>
                                            <input type="text" placeholder='Enter a id'
                                                className="form-control" id="contact-user" onKeyDown={addUserPressedEnter} required   />
                                        </div>
                                        <div className='padd'>
                                            <input type="text" placeholder='Enter nickname'
                                                className="form-control" id="contact-nickname" onKeyDown={addUserPressedEnter} required />
                                        </div>
                                        <div className='padd'>
                                            <input type="text" placeholder='Enter the server'
                                                className="form-control" id="contact-server" onKeyDown={addUserPressedEnter} required/>
                                        </div>
                                        <div className='error-add-user' id='errorAddingUser'>{errorAddUser}</div>
                                    </div>
                                }</Modal.Body>
                                <Modal.Footer>
                                    <button className='btn btn-primary' onClick={addUserAsFriend}>Add Contact</button>
                                </Modal.Footer>
                            </Modal>
                        </i>
                    </span>
                    <Modal show={showImageModal} centered onHide={() => setShowImageModal(false)}>
                        <Modal.Header closeButton className='header'>
                            {name}
                        </Modal.Header>
                        <Modal.Body><img src={userImage} className='big-image'></img></Modal.Body>
                    </Modal>
                </div>
                <div className="search-box">
                    <div className='search-container'>
                        <i>
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                                className="bi bi-search" viewBox="0 0 16 16">
                                <path
                                    d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001c.03.04.062.078.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1.007 1.007 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0z"></path>
                            </svg>
                        </i>
                        <input type="text" placeholder="Search here" autoComplete="off"></input>
                    </div>
                </div>

                <div className='left-bar'>
                    {myContacts.current.map((user, key) => {
                        if (user.id != props.username) {
                            return (<Contact userInfo={user} setChosenChat={props.setChosenChat} key={key}
                                updateLastM={props.updateLastProp} username={props.username} token={props.token}
                                renderAgain={props.renderAgain} />)
                        }
                    })}
                </div>
            </div>
        </div>
    )
}

export default ChatList;