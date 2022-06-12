import React, { useEffect, useState } from 'react';
import { useRef } from 'react';
import MessageField from '../MessageField/MessageField';
import UserImage from '../UserImage/UserImage';
import './Conversation.css';
import { dataServer, chats, video_extensions, audio_extensions, image_extensions, aspMvcServer } from '../../Data/data';
import { Modal } from 'react-bootstrap';
import Contact from '../Contact/Contact';
import { HubConnection, HubConnectionBuilder } from "@microsoft/signalr";


const Conversation = (props) => {
    const [connection, setConnection] = useState(null);
    const [msg, setMsg] = useState("");
    const [msgList, setMsgList] = useState([]);
    var newMsgs = [];
    var audioPieces = [];
    var token = props.token;
    const latestChat = useRef(null);
    latestChat.current = msgList;
    const [counter, setCounter] = useState(0);
    const [showAudioModal, setShowAudioModal] = useState(false);
    const [showFileModal, setShowFileModal] = useState(false);
    const [showVideoModal, setShowVideoModal] = useState(false);
    var isRecordActive = false;
    const [sTop, setSTop] = useState(0)
    const [voiceRecorder, setVoiceRecorder] = useState(null);


    const [stream, setStream] = useState({
        hasAccessToMic: false, voiceRecorder: null
    });

    var updateLastMsgInGui = () => {
        props.updateLastProp.current.forEach(userNotifier => {
            if (userNotifier)
                userNotifier();
        });
    }

    function focusTextBox() {
        /* when the react is the wwwroot folder built and static
         if the focus() function is called the focus is behaving unexpectedly.
        However, if focusing while "npm start", it works great
        in in the README we will ask the teacher to check with "npm start"*/
        var textbox = document.getElementById('textbox');
        if (textbox && window.location.href.indexOf(aspMvcServer) < 0)
            textbox.focus();
    }

    const [recordInfo, setRecordInfo] = useState({
        isRecording: false, canRecord: false, url: ""
    });
    var recorder;

    const [showPictureModal, setShowPictureModal] = useState(false);

    const updateScroll = () => {
        var chat = document.getElementById('chat');
        chat.scrollTop = chat.scrollHeight;
        setSTop(2000);
    }

    var id = props.username;
    var canAddRecord = false;
    var alreadyGotMessages = false;
    var oldUser = "";
    var connectionId = "";
    var config = {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + token
        }
    }

    useEffect(() => {
        fetch(dataServer + "api/contacts/" + props.chosenChat.id + "/messages", config).then(res => res.json())
            .then(data => {
                setMsgList(data);
                oldUser = props.chosenChat.id;
            });

        focusTextBox();
    }, [props.chosenChat])

    useEffect(() => {
        try {
            const connect = new HubConnectionBuilder()
                .withUrl(dataServer + "hub")
                .withAutomaticReconnect()
                .build();

            setConnection(connect)
        }
        catch (e) { console.log(e); }
    }, []);

    useEffect(() => {
        if (connection) {
            connection.start()
                .then(result => {
                    connection.on("ReceiveMessage", message => {
                        const updatedMsgList = [...latestChat.current];
                        var chosenChatId = localStorage.getItem(id + "chosenChat")
                        props.setRenderAgain(!props.renderAgain)
                        updateScroll();
                        updateLastMsgInGui();
                        setTimeout(updateScroll, 125);

                        if (message.senderUsername !== chosenChatId) return;
                        updatedMsgList.push(message);
                        setMsgList(updatedMsgList);
                        updateScroll();
                        updateLastMsgInGui();
                        setTimeout(updateScroll, 125);
                    })
                    connection.invoke("SetIdInServer", props.username).then(res => { }).catch(e => console.log("Not connected"));
                })
                .catch(e => console.log('Connection failed: ', e));
        }
    }, [connection])

    async function sendMessage() {
        if (msg) {
            var newMessages = [...msgList];
            var msgListInDb;
            // get last message
            chats.forEach(chatData => {
                chatData.participicants.forEach(participicant => {
                    if (participicant === props.chosenChat.id && chatData.participicants.includes(id)) {
                        msgListInDb = chatData.messages;
                        return;
                    }
                })
            });


            var newMsg = {
                id: Math.floor(1000 * Math.random() + 200),
                type: "text",
                content: msg,
                senderUsername: id,
                created: new Date()
            };

            var newMessages = [...msgList];
            newMessages.push(newMsg);
            setMsgList(newMessages);
            setMsg("");
            updateScroll();
            updateLastMsgInGui();
            setTimeout(updateScroll, 125);

            // GET to get the server of the other user 
            var config = {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + token
                }
            }

            var res = await fetch(dataServer + "api/contacts/server/" + props.chosenChat.id, config);
            var response = await res.json();

            //POST - api/contacts/{id}/messages
            var data = { "content": msg };
            var config = {
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + token,
                    'Accept': '*/*',
                    'Accept-Endcoding': 'gzip, deflate, br',
                    'Connection': 'keep-alive',
                    'Content-type': 'application/json'
                },
                body: JSON.stringify(data)
            }
            fetch(dataServer + "api/contacts/" + props.chosenChat.id + "/messages", config);

            //POST - Transfer to the other server
            var data = { "from": props.username, "to": props.chosenChat.id, "content": msg };
            var config = {
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + token,
                    'Accept': '*/*',
                    'Accept-Endcoding': 'gzip, deflate, br',
                    'Connection': 'keep-alive',
                    'Content-type': 'application/json'
                },
                body: JSON.stringify(data)
            }
            var hisServer = response.server;
            var len = hisServer.length;
            // if ther second user has different server, then send
        
            if (hisServer[len - 1] != '/')
                hisServer = hisServer + '/';
            if (dataServer.indexOf(hisServer) < 0 && (hisServer).indexOf(dataServer) < 0) {
                fetch(hisServer + "api/transfer/", config);
            }

            try {
                await connection.invoke("SendMsg", props.username, msg, props.chosenChat.id);
            }
            catch (e) {
                console.log(e);
            }
            updateLastMsgInGui();
        }
    };

    async function onSend(e) {
        await sendMessage();
    }

    async function onEnter(e) {
        if (e.key === "Enter") {
            await sendMessage();
        }
    }

    var updateAudioInGuiMessages = function () {
        const audioUrl = URL.createObjectURL(new Blob(audioPieces, { 'type': 'audio/webm' }));
        setRecordInfo({
            isRecording: false,
            canRecord: true,
            url: audioUrl
        });

        isRecordActive = false;

        var newMessages = [...msgList];
        var newId;
        var msgListInDb;
        // get last message - for audio
        chats.forEach(chatData => {
            chatData.participicants.forEach(participicant => {
                if (participicant === props.chosenChat.id && chatData.participicants.includes(id)) {
                    newId = Math.max.apply(Math, chatData.messages.map((msg => {
                        msgListInDb = chatData.messages;
                        return msg.id;
                    })));
                    return;
                }
            })
        });

        newId += 1;
        var newMsg = {
            id: newId,
            type: "audio",
            content: audioUrl,
            senderUsername: id,
            created: new Date(),
            fileName: "record" + newId + ".mp3"
        };

        if ((msgListInDb.filter(msg => msg.content === newMsg.content).length === 0)
            && canAddRecord) {
            newMessages.push(newMsg);
            setMsgList(newMessages);
            canAddRecord = false;
            updateLastMsgInGui();
            setTimeout(updateScroll, 125);
        }
    }

    const setModalFileToShow = (e) => {
        setShowFileModal(true);
    }

    const startRecord = (e) => {
        if (!recordInfo.isRecording) {
            audioPieces = [];
            setShowAudioModal(true);
            navigator.mediaDevices.getUserMedia({ audio: true }).then((m) => {
                window.mStream = m;
                try {
                    var audioRecorder = new MediaRecorder(m, {
                        mimeType: "audio/webm"
                    });
                    setStream({
                        ...stream,
                        hasAccessToMic: true,
                        voiceRecorder: audioRecorder
                    });
                    audioRecorder.start();
                } catch (error) {
                }
                canAddRecord = true;
                audioRecorder.ondataavailable = function (event) {
                    var newPieces = [...audioPieces];
                    newPieces.push(event.data);
                    audioPieces = newPieces;
                    updateAudioInGuiMessages();
                }

                audioRecorder.onstop = updateAudioInGuiMessages;
            });
        }
    }

    const stopRecord = (e) => {
        setShowAudioModal(false);
        stream.voiceRecorder.stop();
        window.mStream.getTracks().forEach(t => {
            t.stop();
        });
    }

    var takeUserPicture = (e) => {
        var canvas = document.getElementById('image-canvas');
        var video = document.getElementById('userCameraVideo');
        canvas.width = video.videoWidth;
        canvas.height = video.videoHeight;
        canvas.getContext('2d').drawImage(video, 0, 0, canvas.width, canvas.height);
        let imageTaken = canvas.toDataURL('image/png');
        addImageToDB(imageTaken);
        setShowPictureModal(false);
        video.pause();
        window.userStream.getVideoTracks().forEach(t => {
            try {
                t.stop()
            } catch {
                t.enabled = false;
            }
        });
    }

    var uploadClicked = (e) => {
        setShowFileModal(false);
        uploadFile(e);
    }

    var uploadVideoClicked = (e) => {
        setShowVideoModal(false);
        uploadFile('chooserVideo');
    }

    var uploadImageClicked = (e) => {
        setShowPictureModal(false);
        uploadFile('chooserImage');
    }

    var getTypeByFileName = (fileName) => {
        var suffix = fileName.split('.')[1].toLowerCase();
        if (audio_extensions.includes(suffix)) {
            return "audio";
        }

        if (image_extensions.includes(suffix)) {
            return "image";
        }

        if (video_extensions.includes(suffix)) {
            return "video";
        }
        return "file";
    }

    var uploadFile = (chooser) => {
        var chooseId = 'chooser';
        if (chooser) {
            if (chooser === 'chooserVideo')
                chooseId = 'chooserVideo';
            else if (chooser === 'chooserImage')
                chooseId = 'chooserImage';
        }
        var input = document.getElementById(chooseId)
        var fileReader = new FileReader()
        fileReader.readAsDataURL(input.files[0])
        fileReader.onload = (event) => {
            var fileSrc = event.target.result
            var newMessages = [...msgList];
            var lastMsgId;
            var msgListInDb;
            // get last message
            chats.forEach(chatData => {
                chatData.participicants.forEach(participicant => {
                    if (participicant === props.chosenChat.id && chatData.participicants.includes(id)) {
                        lastMsgId = Math.max.apply(Math, chatData.messages.map((msg => {
                            msgListInDb = chatData.messages;
                            return msg.id;
                        })));
                        return;
                    }
                })
            });

            var fileName = input.files[0].name
            var newMsg = {
                id: lastMsgId + 1,
                type: getTypeByFileName(fileName),
                content: fileSrc,
                senderUsername: id,
                created: new Date(),
                fileName: fileName
            };

            newMessages.push(newMsg);
            setTimeout(updateScroll, 125);
            setMsgList(newMessages);
            updateLastMsgInGui();
        };
    }

    const addImageToDB = (imageTaken) => {
        const newMessages = [...msgList];
        var lastMessageId, msgListInDb;
        // get last message     
        chats.forEach(chatData => {
            chatData.participicants.forEach(participicant => {
                if (participicant === props.chosenChat.id && chatData.participicants.includes(id)) {
                    lastMessageId = Math.max.apply(Math, chatData.messages.map((msg => {
                        msgListInDb = chatData.messages;
                        return msg.id;
                    })));
                    return;
                }
            })
        });

        var newId = lastMessageId + 1;
        var newMsg = {
            id: newId,
            type: 'image',
            content: imageTaken,
            senderUsername: id,
            created: new Date(),
            fileName: "image" + newId + ".png"
        };

        newMessages.push(newMsg);
        setMsgList(newMessages);
        setTimeout(updateScroll, 125);
        updateLastMsgInGui();
    };

    return (
        <div className="col-9 conversation">
            <div className='conversation-container'>
                <div className='user-title'>
                    <UserImage src={props.chosenChat.profileImage} headOf={props.chosenChat.name} />
                    <div className='user-name'>{props.chosenChat.name}</div>
                </div>
                <div className='message-container' id="chat" scolltop={sTop}>
                    {msgList?.map((msg, key) => (
                        <MessageField type={msg.type} content={msg.content} senderUsername={msg.senderUsername} key={key}
                            fileName={msg.fileName} username={props.username}>
                        </MessageField>
                    ))}
                </div>
                <div className='chat-box'>
                    <div className='search-container'>
                        {/*Take a picture*/}
                        <button className='click-button hide-it'
                            onClick={() => { setShowPictureModal(true); }}>
                            <img className='button-image' src="/images/take-photo.png" alt='button'></img></button>
                        {/*Upload Image Modal*/}
                        <Modal show={showPictureModal} centered dialogClassName="file-modal"
                            onHide={() => setShowPictureModal(false)}>
                            <Modal.Header closeButton>
                                <Modal.Title>Upload image</Modal.Title>
                            </Modal.Header>
                            <Modal.Body>
                                {/*Here the file modal should appear*/}
                                <input type="file" id="chooserImage" accept="image/*"></input>
                            </Modal.Body>
                            <Modal.Footer>
                                <button className='btn btn-primary' onClick={uploadImageClicked}>Upload</button>
                            </Modal.Footer>
                        </Modal>


                        <button className='click-button hide-it'
                            onClick={startRecord}>
                            <img className='button-image' src="/images/record-audio.jpg" alt='button'></img></button>
                        <button className='click-button hide-it' onClick={() => { setShowVideoModal(true); }}>
                            <img className='button-image' src="/images/video-icon.png" alt='button'></img></button>
                        {/*Upload Video Modal*/}
                        <Modal show={showVideoModal} centered dialogClassName="file-modal"
                            onHide={() => setShowVideoModal(false)}>
                            <Modal.Header closeButton>
                                <Modal.Title>Upload video</Modal.Title>
                            </Modal.Header>
                            <Modal.Body>
                                {/*Here the file modal should appear*/}
                                <input type="file" id="chooserVideo" accept="video/mp4,video/x-m4v,video/*"></input>
                            </Modal.Body>
                            <Modal.Footer>
                                <button className='btn btn-primary' onClick={uploadVideoClicked}>Upload</button>
                            </Modal.Footer>
                        </Modal>

                        <button className='click-button hide-it' onClick={setModalFileToShow}>
                            <img className='button-image' src="/images/attach.jpg" alt='button'></img></button>
                        {/*Record Audio Modal*/}
                        <Modal show={showAudioModal} centered onHide={() => setShowAudioModal(false)} id="modalAudio">
                            <Modal.Header closeButton>
                                <Modal.Title>Recording...</Modal.Title>
                            </Modal.Header>
                            <Modal.Body id="modalAudioBody">
                                <button className='stop-button' onClick={stopRecord}>
                                    <img src='/images/stop-button.png' className='stop-button-image' alt='stop-button'>
                                    </img></button>
                            </Modal.Body>
                        </Modal>

                        {/*Upload File Modal*/}
                        <Modal show={showFileModal} centered dialogClassName="file-modal"
                            onHide={() => setShowFileModal(false)}>
                            <Modal.Header closeButton>
                                <Modal.Title>Upload file</Modal.Title>
                            </Modal.Header>
                            <Modal.Body>
                                {/*Here the file modal should appear*/}
                                <input type="file" id="chooser"></input>
                            </Modal.Body>
                            <Modal.Footer>
                                <button className='btn btn-primary' onClick={uploadClicked}>Upload</button>
                            </Modal.Footer>
                        </Modal>

                        <input className='search-textbox' id='textbox' placeholder='Enter a message' autoFocus
                            value={msg} onChange={(event) => setMsg(event.target.value)}
                            onKeyDown={onEnter}></input>
                        <button className='click-button' onClick={onSend}><img src='/images/send-button-it.jpg'
                            className='button-image'
                            alt='button'></img></button>
                    </div>
                </div>
                <canvas id="image-canvas"></canvas>
            </div>

        </div>
    )
}

export default Conversation;