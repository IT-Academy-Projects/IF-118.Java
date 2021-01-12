$( document ).ready(init())

function init() {
    getCurrentUser().then(ans => {
        initButtons(ans)
    })
}

function initButtons(ans) {
    if(!ans.exists) {
        $('#register-button').show();
        $('#login-button').show();
    } else {
        $('#profile-button').show();
        $('#logout-button').show();
    }
}

function getCurrentUser() {
    return $.get("/api/v1/users/is-authenticated")
}