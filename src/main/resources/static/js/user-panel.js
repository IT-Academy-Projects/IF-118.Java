let user
init();
function init() {
    getRequest(`api/v1/users/me`).done(data => {
        $('#table-title').text('User');
        $('#table-content').html('');
        user = JSON.stringify(data);
        showUser();
    })
}

function showUser() {
    $('#user-name').text(user.name)
    $('#user-email').text(user.email)
}
function getRequest(url) {
    return $.get(url);
}