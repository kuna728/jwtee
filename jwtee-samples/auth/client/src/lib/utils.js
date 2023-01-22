import {base} from "$app/paths";

export const API_URL = `${base}/api`;

export const fetchPost = (resource, data) => {
    return fetch(`${API_URL}/${resource}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data),
    })
}

export const fetchPostWithToken = (resource, token, data) => {
    return fetch(`${API_URL}/${resource}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(data),
    })
}