$( document ).ready(init())

function init() {
    isAuthenticated().then(ans => {
        initButtons(ans)
    })
}

function initButtons(ans) {
    if(!ans.exists) {
        $('#register-button').show();
        $('#login-button').show();
    } else {
        $('#panel-button').show();
        $('#logout-button').show();
    }
}

function isAuthenticated() {
    return $.get("/api/v1/users/is-authenticated")
}