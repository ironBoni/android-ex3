import React, { useState, useRef } from 'react'
import './Chat.css';
import { users } from '../../Data/data';
import ChatList from '../ChatList/ChatList'
import Conversation from '../Conversation/Conversation'
import Welcome from '../Welcome/Welcome';
import { Nav, Navbar, Container } from "react-bootstrap"

const Chat = (props) => {
    const updateLastArray = useRef(Array(users.length).fill(null));
    const [chosenChat, setChosenChat] = useState();
    const [renderAgain, setRenderAgain] = useState(false);

    function handleClick() {
        props.setIsShowSignIn(true);
        props.setIsSubmittedUser(false);
    }

    return (
        <div>
            <Navbar bg="dark" variant="dark">
                <Container>
                    <Navbar.Brand className="nav" onClick={handleClick}> <img className="logo-nav" src="/images/webIcon.png" /></Navbar.Brand>
                    <Nav className="me-auto">
                        <Nav.Link className="nav" href="http://localhost:5266/Ratings/">Ratings</Nav.Link>
                        <Nav.Link className="nav" onClick={handleClick}>Logout</Nav.Link>
                    </Nav>
                </Container>
            </Navbar>

            <div className="container-fluid">
                <div className='row no-gutters rounded-lg shadow main'>
                    <ChatList setChosenChat={setChosenChat} updateLastProp={updateLastArray}
                        setIsShowSignIn={props.setIsShowSignIn} username={props.username} token={props.token}
                        setToken={props.setToken} renderAgain={renderAgain} />
                    {chosenChat ?
                        <Conversation chosenChat={chosenChat} updateLastProp={updateLastArray} setRenderAgain={setRenderAgain}
                            setIsShowSignIn={props.setIsShowSignIn} username={props.username} token={props.token}
                            renderAgain={renderAgain} />
                        : <Welcome />}
                </div>
            </div>
        </div>
    )
}

export default Chat;