import React from 'react';
import {Button, Col, Container, Form, InputGroup, ListGroup, Navbar, Row} from "react-bootstrap";
import NewItem from "./components/NewItem.";
import ItemsList from "./components/ItemsList";
import TodoContext from "./context/TodoContext";
import {toast, ToastContainer} from "react-toastify";

export default function App() {

    const todo = React.useContext(TodoContext);

    return (
        <Container fluid style={{padding: 0}}>
            <Navbar expand="lg" bg="primary" variant="dark">
                <Container>
                    <Navbar.Brand href="#" style={{fontSize: 25, fontWeight: "bolder", letterSpacing: 3}}>TODO</Navbar.Brand>
                    <Navbar.Toggle />
                    <Navbar.Text style={{color: "white"}}>jwtee samples</Navbar.Text>
                </Container>
            </Navbar>
            <Container>
                <Row style={{marginTop: 50}}>
                    <Col>
                        <NewItem />
                    </Col>
                </Row>
                <Row style={{marginTop: 50}}>
                    <Col>
                        <h2>Items to do</h2>
                        <ItemsList items={todo.items.filter(i => !i.done)} />
                    </Col>
                </Row>
                <Row style={{marginTop: 50}}>
                    <Col>
                        <h2>Items done</h2>
                        <ItemsList items={todo.items.filter(i => i.done)} />
                    </Col>
                </Row>
            </Container>
            <ToastContainer position="top-center" autoClose={2000}/>
        </Container>
    )
}