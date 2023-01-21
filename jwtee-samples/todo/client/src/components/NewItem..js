import {Button, Form, InputGroup} from "react-bootstrap";
import React from "react";
import TodoContext from "../context/TodoContext";
import {toast} from "react-toastify";

export default function NewItem() {

    const todo = React.useContext(TodoContext);

    const [name, setName] = React.useState("");

    const handleClick = () => {
        toast.promise(todo.addItem(name).then(() => setName("")), {
            pending: "Adding new item...",
            success: "Item added successfully",
            error: "Item could not be added due to unexpected error"
        });
    }

    return (
        <>
            <h2>New item</h2>
            <InputGroup>
                <Form.Control type="text" value={name} onChange={e => setName(e.target.value)}/>
                <Button disabled={name===""} variant="outline-secondary" onClick={handleClick}>Add</Button>
            </InputGroup>
        </>
    )
}