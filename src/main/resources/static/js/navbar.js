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
        $('#notification-button').show();
        $('#panel-button').show();
        $('#logout-button').show();
        $('#courses-btn').show();
        $('#groups-btn').show();
    }
}

function isAuthenticated() {
    return $.get("/api/v1/users/authenticated")
}
