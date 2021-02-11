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
        $('#invitation-button').show();
        $('#panel-button').show();
        $('#logout-button').show();
        $('#courses-btn').show();
        $('#groups-btn').show();
        $('#search-form').show();
    }
}

function handleSearch() {
    let searchQuery = $('#search-field').val();
    window.location.replace(`/search-result?searchQuery=${searchQuery}`);
}

function isAuthenticated() {
    return $.get("/api/v1/users/is-authenticated")
}
