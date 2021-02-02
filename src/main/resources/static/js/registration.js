function handleRegister() {
    let email = $("#email").val();
    let password = $("#password").val();
    let passwordRepeat = $("#passwordRepeat").val();
    let name = $("#name").val();
    let role = $("#role").val();

    if (email === '' || password === '' || passwordRepeat === '' || name === '') {
        makeRed();
        showInfo("Enter all required information")
    } else if (password !== passwordRepeat) {
        $('#passwordRepeat').css("border", "2px solid red").css("box-shadow", "0 0 3px red");
        showInfo("Password confirmation is not correct")
    } else if (!validateEmail(email)) {
        $('#email').css("border", "2px solid red").css("box-shadow", "0 0 3px red");
        showInfo("Email is not correct")
    } else {
        registerRequestJson({
            "email": email,
            "name": name,
            "password": password,
            "pickedRole": role.toUpperCase()
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
            showInfo(request.responseJSON.message)
            makeRed();
        }
    });
}

function showInfo(text) {
    let label = $("#info-label");
    label.text(text);
    label.show();
}

function validateEmail(email) {
    const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(String(email).toLowerCase());
}

function makeRed() {
    $('#email').css("border", "2px solid red").css("box-shadow", "0 0 3px red");
    $('#password').css("border", "2px solid red").css("box-shadow", "0 0 3px red");
    $('#passwordRepeat').css("border", "2px solid red").css("box-shadow", "0 0 3px red");
    $('#name').css("border", "2px solid red").css("box-shadow", "0 0 3px red");
}