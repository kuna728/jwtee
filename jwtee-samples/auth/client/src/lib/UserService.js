import {API_URL, fetchPost, fetchPostWithToken} from "$lib/utils.js";
import {user} from "$lib/UserStore.js";
import {get} from "svelte/store";

export const updateCounter = counter => {
    return fetchPostWithToken("counter", get(user).token, {counter})
        .then(res => {
            if(res.ok) {
                user.update(u => ({...u, counter}));
                return res;
            }
            throw new Error(res.status);
        })
}

export const login = (username, password, rememberMe) => {
    return fetchPost('auth', {username, password}).then(res => {
        if(res.ok)
            return res.json();
        throw new Error();
    }).then(json => {
        if(json.success) {
            const userObj = {
                firstName: json.firstName,
                token: json.token,
                counter: json.counter
            };
            user.set(userObj);
            if(rememberMe)
                localStorage.setItem("user", JSON.stringify(userObj));
        }
        return json;
    })
}

export const logout = () => {
    localStorage.removeItem("user");
    user.set({
        firstName: undefined,
        token: undefined,
        counter: undefined
    })
}