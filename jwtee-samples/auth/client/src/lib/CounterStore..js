import {writable} from "svelte/store";

export const counter = () => {
    const {subscribe, set, update} = writable(0);

    return {
        subscribe,
        set,
        increment: () => update(n => n + 1),
        decrement: () => update(n => n - 1),
    }
}