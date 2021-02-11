function handleLogin() {
    let email = $("#email").val();
    let password = $("#password").val();
    let rememberMe = $("#remember-me").val();

    if (email === '') {
        $('#email').css("border", "2px solid red").css("box-shadow", "0 0 3px red");
        showError("Enter email");
    } else if (password === '') {
        $('#password').css("border", "2px solid red").css("box-shadow", "0 0 3px red")
        showError("Enter password");
    } else {
        loginRequest({
            "email": email,
            "password": password,
            "remember-me": rememberMe
        })
    }
}

function loginRequest(data) {
    $.ajax({
        url: "/login",
        type: 'post',
        data: data,

        success: function () {
            window.location.replace("/user");
        },

        error: function (request) {
            showError(request.responseJSON.message);
        },
    });
}

function showError(text) {
    $("#error-label").text(text);
}
