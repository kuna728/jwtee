import React from "react";
import {Button, Form, ListGroup} from "react-bootstrap";
import TodoContext from "../context/TodoContext";
import {toast} from "react-toastify";

export default function Item({id, name, done}) {

    const todo = React.useContext(TodoContext);

    const [isChecked, setChecked] = React.useState(done);

    const handleCheck = () => {
        toast.promise(todo.updateItem(id, !done), {
            pending: "Updating item...",
            success: "Item updated successfully",
            error: "Item could not be updated due to unexpected error"
        });
    }

    const handleDelete = () => {
        toast.promise(todo.deleteItem(id), {
            pending: "Deleting item...",
            success: "Item deleted successfully",
            error: "Item could not be deleted due to unexpected error"
        });
    }

    return (
        <ListGroup.Item className="d-flex justify-content-between align-items-start todo-item">
            <div style={{wordBreak: "break-all"}}>
                {name}
            </div>
            <div>
                <Form.Check type="checkbox" checked={isChecked} onChange={handleCheck} className="checkbox"/>
                <i className="bi bi-trash3 delete-icon" onClick={handleDelete}></i>
            </div>
        </ListGroup.Item>
    )

}