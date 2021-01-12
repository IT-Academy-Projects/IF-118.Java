let user

$.ajax(`api/v1/users/me`).done(data => {
    user = data
    init()
})

function init() {
    $('#profile-name').text(user.name)
    $('#profile-email').text(user.email)
    $(".profile-edit-confirm").hide()
    $(".hide").click(function(){
        $(".profile-edit").hide();
        $(".profile-edit-confirm").show();
    });
    $(".show").click(function(){
        $(".profile-edit").show();
        $(".profile-edit-confirm").hide();
    });
}

function editProfile() {
    $('#profile-name').attr('contenteditable', true)
    $('#profile-email').attr('contenteditable', true)
}

function saveChanges() {
    const obj = {
        name: '', email: ''
    }
    obj.name = $('#profile-name').attr('contenteditable', false).text()
    obj.email = $('#profile-email').attr('contenteditable', false).text()
    if (obj.name !== user.name || obj.email !== user.email) {
        $.ajax({
            url: `/api/v1/users/${user.id}/profile?` + $.param(obj),
            type: 'PATCH'
        })
    }
}

function cancel() {
    $('#profile-name').attr('contenteditable', false).text(user.name)
    $('#profile-email').attr('contenteditable', false).text(user.email)
}