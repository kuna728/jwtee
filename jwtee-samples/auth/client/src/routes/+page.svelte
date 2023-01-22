<script>
    import {user} from "$lib/UserStore.js";
    import Counter from "./Counter.svelte";
    import {goto} from "$app/navigation";
    import {slide} from "svelte/transition";
    import {logout, updateCounter} from "$lib/UserService.js";
    import {base} from "$app/paths";

    if($user.token == null)
        goto(`${base}/login`);

    let isSuccess = null;
    let isLoading = false;

    const onUpdate = counter => {
        isLoading = true;
        return updateCounter(counter).then(() => {
            isLoading = false;
            isSuccess = true;
        }).catch(e => {
            console.log(e.message)
            if(e.message === "401") {
                logout();
                goto(`${base}/login#session-expired`);
            }
            isLoading = false;
            isSuccess = false;
        })
    }

    const onLogout = () => {
        logout();
        goto(`${base}/login`);
    }
</script>

<h1>Welcome {$user.firstName}</h1>
{#if isSuccess === true}
    <div class="alert alert-success alert-dismissible" role="alert" transition:slide|local>
        A simple success alert—check it out!
        <button type="button" class="btn-close" aria-label="Close" on:click="{() => isSuccess = null}"></button>
    </div>
{:else if isSuccess === false}
    <div class="alert alert-danger alert-dismissible" role="alert" transition:slide|local>
        A simple danger alert—check it out!
        <button type="button" class="btn-close" aria-label="Close" on:click="{() => isSuccess = null}"></button>
    </div>
{/if}
<p>You can edit your personal counter below.</p>
<Counter onUpdate="{onUpdate}"/>
<div class="d-grid gap-2">
    <button class="btn btn-danger" on:click="{onLogout}" disabled="{isLoading}">Logout</button>
</div>