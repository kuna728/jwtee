<script>
    export let onUpdate;
    import {user} from "$lib/UserStore.js";

    let value = $user.counter;
    $: actionButtonsDisabled = value === $user.counter;
    let isLoading = false;

    let onSave = () => {
        isLoading = true;
        onUpdate(value).then(() => {
            isLoading = false;
        }).catch(() => {
            isLoading = false;
        })
    }
</script>

<div class="input-group">
    <button on:click="{() => value--}" class="btn btn-outline-secondary">-1</button>
    <button on:click="{() => value++}" class="btn btn-outline-secondary">+1</button>
    <input type="number" class="form-control" bind:value disabled/>
    <button class="btn btn-outline-success"
            disabled="{actionButtonsDisabled}"
            on:click="{onSave}"
    >
        Save
    </button>
    <button class="btn btn-outline-danger"
            on:click="{() => value = $user.counter}"
            disabled="{actionButtonsDisabled}"
    >
        Reset
    </button>
</div>

<style>
    .input-group {
        margin-bottom: 20px;
    }
    .form-control {
        background-color: white;
    }
</style>