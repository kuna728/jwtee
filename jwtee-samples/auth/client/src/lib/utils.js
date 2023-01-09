
export const API_URL = "http://localhost:8080/auth-1.0-SNAPSHOT/api";

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