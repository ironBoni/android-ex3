import React, { useState, useEffect } from 'react'
import './Contact.css';
import { dataServer } from '../../Data/data'

const Contact = (props) => {
    var { userInfo, setChosenChat, updateLastM } = props;
    const [lastMsg, setLastMsg] = useState('');
    const [lastMsgTime, setLastMsgTime] = useState('');
    const [lastMsgType, setLastMsgType] = useState('text');
    const [fileName, setFileName] = useState('');
    var id = props.username;

    async function updateLastMessage() {
        var config = {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + props.token
            }
        }
        var res = await fetch(dataServer + "api/contacts/" + userInfo.id + "/messages/last", config);
        var response = await res.json();
        setLastMsg(response.content);

        if (!response.created || response.created == null || response.created == undefined) {
            setLastMsgTime("");
            return;
        }
        var date = new Date(Date.parse(response.created));
        var time = date.toLocaleTimeString().substring(0, 5)
        if (time[time.length - 1] === ":") {
            time = time.substring(0, time.length - 1)
        }
        setLastMsgTime(date.toLocaleDateString() + " " + time);
    }

    useEffect(() => {
        updateLastMessage();
        if (updateLastM && updateLastM.current)
            updateLastM.current.push(updateLastMessage);
    }, []);

    function setChat() {
        setChosenChat(userInfo);
        localStorage.setItem(id + "chosenChat", userInfo.id)
    }
    return (
        <div className='contact' onClick={() => setChat()}>
            <img className='profile-image' alt='profile' src={userInfo.profileImage}></img>
            <div className='text'>
                <h6 className='contact-name'>{userInfo.name}</h6>
                <p className='contact-message'>
                    {lastMsgType === 'text' ? (lastMsg) : (fileName)}
                </p>
            </div>
            <span className='time small float-right'>{lastMsgTime}</span>
        </div>
    )
}

export default Contact;