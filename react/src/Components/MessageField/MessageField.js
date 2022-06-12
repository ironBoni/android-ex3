import React, { useState, useRef } from 'react'
import './MessageField.css'
import { Modal } from 'react-bootstrap';

export default function MessageField(props) {
    var myid = props.username;
    const [showImageModal, setShowImageModal] = useState(false);
    const audioRef = useRef()
    var content;
    if (props.type === "text")
        content = props.content

    else if (props.type === "audio") {
        content = (<audio className='audio' controls src={props.content} />)
    }
    else if (props.type === "image") {
        content = (<button className='click-button' onClick={() => setShowImageModal(true)}>
            <img src={props.content} className="photo" alt='button'></img></button>)
    } else if (props.type === "video") {
        content = (<video className="video-message" controls src={props.content}></video>)
    }
    else {
        content = ((<a href={props.content} download={props.fileName} className='link'>
            <div className={props.senderUsername === myid ? 'file-begin' : 'file-end'}>
                <div id={props.senderUsername === myid ? 'file-div' : 'file-div-end'}>
                    <img src='/images/file-icon.png' className='file-image' alt='file'></img>
                    <div className='name-div'>{props.fileName}</div>
                    {/*<aside className='file-ext'>{props.fileName.split('.')[1].toUpperCase()}</aside>*/}
                </div>
            </div>
        </a>))
    }
    return (
        props.type !== 'file' ?
            (<div className={props.senderUsername === myid ? 'empty' : 'right-message'}>
                <div className={props.senderUsername === myid ? 'message-div' : 'message-div-end'}>
                    {props.type != 'audio' ?
                        <div id="main-div" className={props.senderUsername === myid ? 'message' : 'message-not-mine'}>
                            {content}
                        </div>
                        : (
                            <div id="main-div" className={props.senderUsername === myid ? 'message-audio' : 'message-not-mine-audio'}>
                                <audio ref={audioRef} className='audio' controls src={props.content} />
                            </div>
                        )}
                </div>
                <Modal show={showImageModal} centered onHide={() => setShowImageModal(false)}
                    contentClassName='picture-modal-class' dialogClassName='picture-modal-width'>
                    <Modal.Header closeButton>
                    </Modal.Header>
                    <Modal.Body>
                        <div className='centered-div'>
                            <img src={props.content} className='big-image-field' alt='big'></img>
                        </div>
                    </Modal.Body>
                </Modal>
            </div>) :
            (<div className={props.senderUsername === myid ? 'empty-file' : 'right-message-file'}>
                <div className={props.senderUsername === myid ? 'message-father' : 'message-not-mine-father'}>
                    <div className={props.senderUsername === myid ? 'message-d' : 'message-d-end'}>
                        {content}
                    </div>
                </div>
            </div>)
    )
}