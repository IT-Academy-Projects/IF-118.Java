function handleRegister() {
    let email = $("#email").val();
    let password = $("#password").val();
    let passwordRepeat = $("#passwordRepeat").val();
    let name = $("#name").val();
    let pickedRole = $("#pickedRole").val();


    if (email === '' || password === '' || passwordRepeat === '' || name === '') {
        makeRed();
        showError("Fields cannot be empty")
    } else if (password !== passwordRepeat) {
        $('#passwordRepeat').css("border", "2px solid red").css("box-shadow", "0 0 3px red");
        showError("Password confirmation is not correct")
    } else {
        registerRequestJson({
            "email": email,
            "name": name,
            "password": password,
            "pickedRole": pickedRole.toUpperCase()
        });
    }
}


function registerRequestJson(data) {
    return $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: 'POST',
        url: '/api/v1/registration',
        data: JSON.stringify(data),
        dataType: 'json',

        success: function (data, textStatus, xhr) {
            window.location.replace('/login');
        },

        error: function (request, textStatus, errorThrown) {
            showError(request.responseJSON.message)
            makeRed();
        }
    });
}

function showError(text) {
    let label = $("#error-label");
    label.text(text);
    label.show();
}

function makeRed() {
    $('#email').css("border", "2px solid red").css("box-shadow", "0 0 3px red");
    $('#password').css("border", "2px solid red").css("box-shadow", "0 0 3px red");
    $('#passwordRepeat').css("border", "2px solid red").css("box-shadow", "0 0 3px red");
    $('#name').css("border", "2px solid red").css("box-shadow", "0 0 3px red");
}