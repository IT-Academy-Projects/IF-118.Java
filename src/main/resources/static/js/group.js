const urlParams = new URLSearchParams(window.location.search);
const id = urlParams.get('id');

showGroupInfo();

function showGroupInfo() {

    $.get(`api/v1/users/me`).then(user => {
        if (user.roles.find(role => role.name === "STUDENT")) {
            $('#invite-to-group-button').hide();
        }
    });

    $('#table-content').html('');
    $.ajax(`/api/v1/groups/${id}`).then(group => {
        $('#group-name').text(group.name);

        $.get(`/api/v1/users/${group.ownerId}`).then(user => {
            $('#owner').text(user.name);
        })
        group.users.forEach(user => {
            $('#students').append(user.name + ' | ');
        });

        group.courses.forEach((course) => {
            $('#courses').append(`<span><a href="/course?id=${course.id}">`+ course.name +`</a> | </span>`);
        });
    });
}

function handleChatButton() {
    window.location.replace(`/group-chat?id=${id}`);
}