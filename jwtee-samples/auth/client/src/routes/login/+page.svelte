<script>
    import {goto} from "$app/navigation";
    import {user} from "$lib/UserStore.js";
    import {slide} from "svelte/transition";
    import {fetchPost} from "$lib/utils.js";
    import {login} from "$lib/UserService.js";
    import {json} from "@sveltejs/kit";
    import { page } from '$app/stores';
    import {onMount} from "svelte";

    if($user.token != null)
        goto("/");

    let username = "";
    let password = "";
    let rememberMe = false;
    let sessionExpired = null;
    let failAlert = null;
    let wasSubmitted = false;
    let isLoading = false;
    let form;

    const onSubmit = () => {
        wasSubmitted = true;
        if(form.checkValidity()) {
            isLoading = true;
            login(username, password, rememberMe).then(json => {
                isLoading = false;
                sessionExpired = false;
                if(json.success)
                    goto("/");
                else
                    failAlert = "Credentials you provided are not correct";
            }).catch(error => {
                isLoading = false;
                sessionExpired = false;
                failAlert = "Something went wrong. Try again later.";
            })
        }
    }

    onMount(() => {
        sessionExpired = $page.url.hash === "#session-expired";
    })
</script>

<h1 class="mb-3">Sign in</h1>
{#if sessionExpired}
    <div class="alert alert-warning alert-dismissible" role="alert" transition:slide|local>
        Your session has expired. Please sign in again.
        <button type="button" class="btn-close" aria-label="Close" on:click="{() => sessionExpired = false}"></button>
    </div>
{/if}
{#if failAlert !== null}
    <div class="alert alert-danger alert-dismissible" role="alert" transition:slide|local>
        {failAlert}
        <button type="button" class="btn-close" aria-label="Close" on:click="{() => failAlert = null}"></button>
    </div>
{/if}
<form novalidate class="{wasSubmitted && 'was-validated'}" bind:this={form}>
    <div class="mb-3">
        <label for="username" class="form-label">Username</label>
        <input type="text" id="username" bind:value={username} class="form-control" required minlength="4" maxlength="12"/>
        <div class="invalid-feedback">
            Please provide a valid username
        </div>
    </div>
    <div class="mb-3">
        <label for="password" class="form-label">Password</label>
        <input type="password" id="password" bind:value={password} class="form-control" required minlength="6" maxlength="16"/>
        <div class="invalid-feedback">
            Please provide a valid password
        </div>
    </div>
    <div class="mb-3 form-check">
        <input type="checkbox" class="form-check-input" id="rememberMe" bind:checked={rememberMe}>
        <label class="form-check-label" for="rememberMe">Remember me</label>
    </div>
    <div class="d-grid gap-2">
        <button class="btn btn-primary" on:click="{onSubmit}" disabled="{isLoading}">
            {#if isLoading}
                <span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
            {/if}
            Sign in
        </button>
    </div>
</form>
