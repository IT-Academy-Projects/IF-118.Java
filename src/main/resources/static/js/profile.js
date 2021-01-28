let user;
init();

function init() {
    getRequest(`api/v1/users/profile`).then(data => {
        user = data;
        showUser(user);
    })
}

function saveName() {
    const userNameUpdateRequest = {
        name: $('#update-name').val()
    }

    $.ajax({
        url: `/api/v1/users/${user.id}/profile/name`,
        type: 'PATCH',
        data: JSON.stringify(userNameUpdateRequest),
        contentType: "application/json; charset=utf-8",
    })
}

function saveEmail() {
    const userEmailUpdateRequest = {
        email: $('#update-email').val()
    }

    $.ajax({
        url: `/api/v1/users/${user.id}/profile/email`,
        type: 'PATCH',
        data: JSON.stringify(userEmailUpdateRequest),
        contentType: "application/json; charset=utf-8",
    })

}

function checkNameCompatible() {
    let name = $('#update-name').val();
    let isNameSame = name === user.name;
    let isNameEmpty = name === '';

    if (isNameEmpty) {
        $('#save-name-btn').attr('disabled', true);
    }

    if (isNameSame) {
        $('#save-name-btn').attr('disabled', true);
        $('#sameNameError').show()
    } else {
        $('#sameNameError').hide()
    }

    if (!isNameSame && !isNameEmpty) {
        $('#save-name-btn').removeAttr('disabled');
    }
}

function checkEmailCompatible() {


    let email = $('#update-email').val();


    let isEmailSame = email === user.email;


    let isEmailEmpty = email === '';

    if (isEmailEmpty) {
        $('#save-chg-btn').attr('disabled', true);
    }


    if (isEmailSame) {
        $('#save-chg-btn').attr('disabled', true);
        $('#sameEmailError').show()
    } else {
        $('#sameEmailError').hide()
    }

    if (!isEmailSame && !isEmailEmpty || !isNameEmpty) {
        $('#save-chg-btn').removeAttr('disabled');
    }

}

function savePasswordChanges() {
    $('#currentPasswordError').hide();
    const userPasswordRequest = {
        oldPassword: $('#old-pass').val(),
        newPassword: $('#new-pass1').val(),
    }

    $.ajax({
        type: "PUT",
        url: `/api/v1/users/${user.id}/updatePass`,
        data: JSON.stringify(userPasswordRequest),
        contentType: "application/json; charset=utf-8",
    }).then(
        res => {
        },
        err => {
            $('#currentPasswordError').show();
        })
}


function checkPasswordsCompatible() {
    if ($('#new-pass1').val() === $('#new-pass2').val()) {
        $('#passwordHelp').hide()
        $('#save-pass-btn').removeAttr('disabled');
    } else {
        $('#passwordHelp').show()
        $('#save-pass-btn').attr('disabled', true);
    }
}

function changeAvatar() {
    let avatar = $('#user-avatar-input').prop('files')[0];

    let formData = new FormData();
    formData.append('avatar', avatar);

    $.ajax({
        type: 'PUT',
        url: `/api/v1/users/${user.id}/avatar`,
        data: formData,
        processData: false,
        contentType: false,
        enctype: 'multipart/form-data'
    }).then(res => {
        location.reload();
    });
}

function showUser(user) {
    if (user.imageId !== null) {
        $('#user-avatar').attr('src', `/api/v1/images/${user.imageId}`);
        $('#avatar-placeholder').hide();
    } else {
        $('#user-avatar').attr('src', `img/no-avatar.png`);
        $('#avatar-placeholder').hide();
    }

    $('#user-name').text(user.name);
    $('#user-email').text(user.email);
}

function getRequest(url) {
    return $.get(url);
}

