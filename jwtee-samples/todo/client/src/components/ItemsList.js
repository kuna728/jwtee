import {ListGroup} from "react-bootstrap";
import Item from "./Item";
import React from "react";

export default function ItemsList({items}) {

    return (
        <ListGroup>
            {items.length === 0 ? <p>No items</p> : items.map(item =>
                <Item key={item.id.toString()} {...item} />
            )}
        </ListGroup>
    )
}