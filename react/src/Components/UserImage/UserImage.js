import React, { useState } from 'react'
import './UserImage.css'
import { Modal } from 'react-bootstrap';

export default function UserImage(props) {
  const [showImageModal, setShowImageModal] = useState(false);

  return (
    <div>
      <button className='click-button' onClick={() => setShowImageModal(true)}>
        <img src={props.src} className='profile-image'></img></button>
      <Modal show={showImageModal} centered onHide={() => setShowImageModal(false)}>
        <Modal.Header closeButton className='header'>
          {props.headOf}
        </Modal.Header>
        <Modal.Body><img src={props.src} alt='bit' className='big-image'></img></Modal.Body>
      </Modal>
    </div>
  )
}