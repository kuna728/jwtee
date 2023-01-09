import React from "react";

const TodoContext = React.createContext({
    token: null,
    items: [],
    fetchItems: () => {},
    addItem: name => {},
    updateItem: (id, done) => {},
    deleteItem: id => {}
})

export default TodoContext;