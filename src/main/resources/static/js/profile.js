let user;
init();

function init() {
    getRequest(`api/v1/users/profile`).then(data => {
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

    $.ajax({
        url: `/api/v1/users/${user.id}/profile?` + $.param(obj),
        type: 'PATCH'
    })

}

function checkEmailNameCompatible() {

    let name = $('#update-name').val();
    let email = $('#update-email').val();

    let isNameSame = name === user.name;
    let isEmailSame = email === user.email;

    let isNameEmpty = name === '';
    let isEmailEmpty = email === '';

    if (isEmailEmpty || isNameEmpty) {
        $('#save-chg-btn').attr('disabled', true);
    }

    if (isNameSame) {
        $('#save-chg-btn').attr('disabled', true);
        $('#sameNameError').show()
    } else {
        $('#sameNameError').hide()
    }

    if (isEmailSame) {
        $('#save-chg-btn').attr('disabled', true);
        $('#sameEmailError').show()
    } else {
        $('#sameEmailError').hide()
    }

    if ((!isNameSame && !isEmailSame) && (!isEmailEmpty || !isNameEmpty)) {
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
            res => {},
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
    if (user.avatar !== '' && user.avatar !== undefined && user.avatar !== null) {
        $('#user-avatar').attr('src', `data:image/png;base64,${user.avatar}`);
        $('#avatar-placeholder').hide();
    }

    $('#user-name').text(user.name);
    $('#user-email').text(user.email);
}

function getRequest(url) {
    return $.get(url);
}

