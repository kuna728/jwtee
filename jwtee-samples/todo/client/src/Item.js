import React from "react";
import {Form, ListGroup} from "react-bootstrap";

export default function Item({id, name, done, updateItemHandler}) {

    const [isChecked, setChecked] = React.useState(done);

    const handleCheck = () => {
        setChecked(!isChecked);
        updateItemHandler(id, !isChecked);
    }

    return (
        <ListGroup.Item className="d-flex justify-content-between align-items-start">
            <div>
                {name}
            </div>
            <div>
                <Form.Check type="checkbox" checked={isChecked} onChange={handleCheck}/>
            </div>
        </ListGroup.Item>
    )

}