<script>
    import {onMount} from "svelte";
    import {user} from "$lib/UserStore.js";

    const userObj = localStorage.getItem("user");
    if(userObj)
        user.set(JSON.parse(userObj));
</script>

<div class="container-fluid">
    <nav class="navbar bg-primary navbar-expand-lg" data-bs-theme="dark">
        <div class="container">
            <span class="navbar-brand">JWTee</span>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link" href="/">Home</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/docs.html">Docs</a>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle active" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            Samples
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="/samples/todo">Todo</a></li>
                            <li><a class="dropdown-item dropdown-active" href="/samples/auth">Auth</a></li>
                        </ul>
                    </li>
                </ul>
            </div>
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
    .dropdown-active {
        font-weight: bold;
    }
</style>