function handlePasswordReset() {
    let email = $("#email").val();

    if (email === '') {
        $('#email').css("border", "2px solid red").css("box-shadow", "0 0 3px red");
        showInfo("Enter email");
    } else {
        passwordResetRequest({
            "email": email,
        })
    }
}

function passwordResetRequest(data) {
   return $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: 'POST',
        url: "/api/v1/password-reset",
        data: JSON.stringify(data),

        success: function () {
            showInfo("Reset link have been send to your email");
            $('#reset-btn').prop('disabled', true);
        },

        error: function (request) {
            showInfo(request.responseJSON.message);
        },
    });
}

function showInfo(text) {
    $("#info-label").text(text);
}
