function handleLogin() {
    let email = $("#email").val();
    let password = $("#password").val();
    let rememberMe = $("#remember-me").val();

    if (email === '' || password === '') {
        $('#email').css("border", "2px solid red").css("box-shadow", "0 0 3px red");
        $('#password').css("border", "2px solid red").css("box-shadow", "0 0 3px red");
        showError("Email and password shouldn't be empty")
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

        success: function (data, textStatus, xhr) {
             window.location.replace('/');
        },

        error: function (request, textStatus, errorThrown) {
            showError(request.responseText);
            $('#email').css("border", "2px solid red").css("box-shadow", "0 0 3px red");
            $('#password').css("border", "2px solid red").css("box-shadow", "0 0 3px red");
        }

    });
}

function showError(text) {
    let label = $("#error-label");
    label.text(text);
    label.show();
}

