let user;
init();

function init() {
    getRequest(`api/v1/users/me`).then(data => {
        user = data;
        showUser(user);
    })
}

function saveChanges() {
    const obj = {
        name: '', email: ''
    }
    let name = $('#update-name').val();
    let email = $('#update-email').val();
    if (name !== "") {
        obj.name = name;
    } else {
        obj.name = user.name;
    }
    if (email !== "") {
        obj.email = email;
    } else {
        obj.email = user.email;
    }
    if (obj.name !== user.name || obj.email !== user.email) {
        $.ajax({
            url: `/api/v1/users/${user.id}/profile?` + $.param(obj),
            type: 'PATCH'
        })
    }
    changeOldPassword();
}
function changeOldPassword() {
    let userPasswordRequest = {oldPassword: '', newPassword: ''};
    const obj = {
        oldPassword: '', newPassword1: '', newPassword2: ''
    }
    obj.oldPassword = $('#old-pass').val();
    obj.newPassword1 = $('#new-pass1').val();
    obj.newPassword2 = $('#new-pass2').val();

    if (obj.newPassword1 === obj.newPassword2) {
        userPasswordRequest.oldPassword = obj.oldPassword
        userPasswordRequest.newPassword = obj.newPassword1

        $.ajax({
            type: "PUT",
            url: `/api/v1/users/${user.id}/updatePass`,
            data: JSON.stringify(userPasswordRequest),
            contentType: "application/json; charset=utf-8",
        })
    }
    else {
        $('#new-pass2').css("border", "2px solid red").css("box-shadow", "0 0 3px red");
        showError("Passwords do not match");
    }
}

function showError(text) {
    let label = $("#error-label");
    label.text(text);
    label.show();
    $('#new-pass1').css("border", "2px solid red").css("box-shadow", "0 0 3px red");
    $('#new-pass2').css("border", "2px solid red").css("box-shadow", "0 0 3px red");
}

function showUser(user) {
    $('#user-name').text(user.name);
    $('#user-email').text(user.email);
}

function getRequest(url) {
    return $.get(url);
}

