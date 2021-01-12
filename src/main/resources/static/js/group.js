const urlParams = new URLSearchParams(window.location.search);
const id = urlParams.get('id');
showGroupInfo();

function showGroupInfo() {
    $('#table-content').html('');
    $.ajax(`/api/v1/groups/${id}`).then(group => {
        $('#group-name').text(group.name);
        $('#owner').text(group.ownerId);
        group.users.forEach(user => {
            $('#students').append(user.name + ' | ');
        });
        group.courses.forEach((course) => {
            $('#courses').append(course.name + ' | ');
        });
    });
}