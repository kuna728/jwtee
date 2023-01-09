import {writable} from "svelte/store";

export const user = writable({
    firstName: undefined,
    token: undefined,
    counter: undefined
})