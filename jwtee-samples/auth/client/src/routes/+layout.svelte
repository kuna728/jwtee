<script>
    import {onMount} from "svelte";
    import {user} from "$lib/UserStore.js";

    const userObj = localStorage.getItem("user");
    if(userObj)
        user.set(JSON.parse(userObj));
</script>

<div class="container-fluid">
    <nav class="navbar bg-primary">
        <div class="container">
            <a class="navbar-brand" href="#">AUTH</a>
            <span class="navbar-text">
                jwtee samples
            </span>
        </div>
    </nav>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-sm-12 col-lg-6 col-xl-4">
                <div class="card">
                    <div class="card-body">
                        <slot></slot>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<style>
    .container-fluid {
        padding: 0;
    }
    .navbar {
        margin-bottom: 50px;
    }
    .navbar-brand {
        color: white;
        font-size: 25px;
        font-weight: bolder;
        letter-spacing: 3px;
    }
    .navbar-text {
        color: white;
    }
</style>