function handleRolePick() {
    let role = $("#role").val();

    rolePickRequest({
            "pickedRole": role.toUpperCase()
        });
}

function rolePickRequest(data) {
    return $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: 'PATCH',
        url: '/api/v1/role-pick',
        data: JSON.stringify(data),
        dataType: 'json',

        success: function () {
            window.location.replace('/user');
        },

        error: function (request) {
            showError(request.responseJSON.message)
        }
    });
}

function showError(text) {
    $("#error-label").text(text);
}

