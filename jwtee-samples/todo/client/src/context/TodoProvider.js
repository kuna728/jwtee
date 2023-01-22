import React from "react";
import TodoContext from "./TodoContext";

const RESOURCE_URL = "api/item"

const getHeaders = token => ({
    'Accept': 'application/json',
    'Content-Type': 'application/json',
    'Token': token
})

export default function TodoProvider({children}) {
    const [token, setToken] = React.useState(localStorage.getItem("token"));
    const [items, setItems] = React.useState([]);

    React.useEffect(() => {
        fetchItems();
    }, [token]);

    const fetchItems = () => fetch(RESOURCE_URL, {
        headers: getHeaders(token)
    }).then(res => {
        if(res.ok)
            return res.json()
        throw new Error();
    }).then(json => {
        setItems(json);
    });

    const addItem = name => delayRand().then(() => fetch(RESOURCE_URL, {
        method: 'POST',
        headers: getHeaders(token),
        body: JSON.stringify({name})
    }).then(res => updateToken(res)));

    const updateItem = (id, done) => delayRand().then(() => fetch(RESOURCE_URL + `/${id}`, {
        method: 'PATCH',
        headers: getHeaders(token),
        body: JSON.stringify({done})
    }).then(res => updateToken(res)));

    const deleteItem = id => delayRand().then(() => fetch(RESOURCE_URL + `/${id}`, {
        method: 'DELETE',
        headers: getHeaders(token)
    }).then(res => updateToken(res)));

    const updateToken = res => {
        const tokenFromHeader = res.headers.get('Token');
        if(tokenFromHeader) {
            setToken(tokenFromHeader);
            localStorage.setItem("token", tokenFromHeader);
        }
        return res;
    }

    const delayRand = () => new Promise(resolve => setTimeout(resolve, Math.random()*500+2000));

    return (
        <TodoContext.Provider value={{token, items, fetchItems, addItem, updateItem, deleteItem}}>
            {children}
        </TodoContext.Provider>
    )
}