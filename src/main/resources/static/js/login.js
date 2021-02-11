function handleLogin() {
    let email = $("#email").val();
    let password = $("#password").val();
    let rememberMe = $("#remember-me").val();

    if (email === '') {
        $('#email').css("border", "2px solid red").css("box-shadow", "0 0 3px red");
        showInfo("Enter email");
    } else if (password === '') {
        $('#password').css("border", "2px solid red").css("box-shadow", "0 0 3px red")
        showInfo("Enter password");
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
            showInfo(request.responseJSON.message);
        },
    });
}

function showInfo(text) {
    $("#info-label").text(text);
}
