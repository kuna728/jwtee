import React from 'react';
import {Button, Col, Container, Form, InputGroup, ListGroup, Navbar, Row} from "react-bootstrap";
import Item from "./Item";

const RESOURCE_URL = "/todo-1.0-SNAPSHOT/api/item"

export default function App() {

    const [items, setItems] = React.useState([]);
    const [token, setToken] = React.useState(localStorage.getItem("token"));
    const [name, setName] = React.useState("");

    const updateToken = res => {
        if(res.headers.get('Token')) {
            console.log("[updateToken] set token for " + res.headers.get('Token'))
            setToken(res.headers.get('Token'));
            localStorage.setItem("token", res.headers.get('Token'));
        }
        console.log("[updateToken] res is " + res)
        return res.json();
    }

    const fetchItems = () => fetch(RESOURCE_URL, {
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Token': token
        }
    }).then(res => res.json()).then(json => {
        setItems(json);
    })

    const addItem = () => fetch(RESOURCE_URL, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Token': token
        },
        body: JSON.stringify({name})
    }).then(res => updateToken(res));

    const updateItem = (id, done) => fetch(RESOURCE_URL + `/${id}`, {
        method: 'PATCH',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Token': token
        },
        body: JSON.stringify({done})
    }).then(res => updateToken(res))

    React.useEffect(() => {
        fetchItems();
    }, [token])

    return (
        <Container>
            <Navbar expand="lg" bg="primary" variant="dark">
                <Container>
                    <Navbar.Brand href="#">jwtee samples - TODO</Navbar.Brand>
                </Container>
            </Navbar>
            <Row style={{marginTop: 50}}>
                <Col>
                    <h2>New item</h2>
                    <InputGroup>
                        <Form.Control type="text" value={name} onChange={e => setName(e.target.value)}/>
                        <Button disabled={name===""} variant="outline-secondary" onClick={() => addItem()}>Add</Button>
                    </InputGroup>
                </Col>
            </Row>
            <Row style={{marginTop: 50}}>
                <Col>
                    <h2>Items to do</h2>
                    <ListGroup>
                        {items.filter(item => !item.done).length === 0 ? "No items" : items.filter(item => !item.done).map(item =>
                            <Item key={item.id.toString()} {...item} updateItemHandler={updateItem}/>
                        )}
                    </ListGroup>
                </Col>
            </Row>
            <Row style={{marginTop: 50}}>
                <Col>
                    <h2>Items done</h2>
                    <ListGroup>
                        {items.filter(item => item.done).length === 0 ? "No items" : items.filter(item => item.done).map(item =>
                            <Item key={item.id.toString()} {...item} updateItemHandler={updateItem}/>
                        )}
                    </ListGroup>
                </Col>
            </Row>
        </Container>
    )
}