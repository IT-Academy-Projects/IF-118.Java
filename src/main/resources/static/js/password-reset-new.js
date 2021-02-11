const urlParams = new URLSearchParams(window.location.search);
let token = urlParams.get('token');

function handleNewPassword() {
    let password = $("#password").val();
    let passwordRepeat = $("#password-repeat").val();

    if (password === '' || passwordRepeat === '') {
        $('#password-repeat').css("border", "2px solid red").css("box-shadow", "0 0 3px red");
        $('#password').css("border", "2px solid red").css("box-shadow", "0 0 3px red");
        showInfo("Enter password");
    } else {
        newPasswordRequest({
            "newPassword": password,
            "token": token
        })
    }
}

function newPasswordRequest(data) {
    return $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: 'POST',
        url: "/api/v1/password-reset/new",
        data: JSON.stringify(data),

        success: function () {
            window.location.replace("/login");
        },

        error: function (request) {
            showInfo(request.responseJSON.message);
        },
    });
}

function showInfo(text) {
    $("#info-label").text(text);
}
