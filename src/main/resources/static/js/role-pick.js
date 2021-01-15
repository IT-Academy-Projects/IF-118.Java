function handleRolePick() {
    let pickedRole = $("#pickedRole").val();

    rolePickRequest({
            "pickedRole": pickedRole.toUpperCase()
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

        success: function (request, textStatus, xhr) {
            showSuccess("Role picked successfully. Redirecting...")
            setTimeout(function(){  window.location.replace('/user'); }, 1500);
        },

        error: function (request, textStatus, errorThrown) {
            showError(request.responseJSON.message)
        }
    });
}

function showError(text) {
    let label = $("#alert");
    label.addClass("alert-danger");
    label.text(text);
    label.show();
}

function showSuccess(text) {
    let label = $("#alert");
    label.addClass("alert-success");
    label.text(text);
    label.show();
}
