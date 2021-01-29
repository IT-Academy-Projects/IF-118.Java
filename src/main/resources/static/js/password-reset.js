function handlePasswordReset() {
    let email = $("#email").val();

    if (email === '') {
        $('#email').css("border", "2px solid red").css("box-shadow", "0 0 3px red");
        showError("Enter email");
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
        dataType: 'json',

        success: function () {
            showError("Message have been send")
        },

        error: function (request) {
            showError(request.responseJSON.message);
        },
    });
}

function showError(text) {
    $("#error-label").text(text);
}
