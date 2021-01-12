init();

function init() {
    getRequest(`api/v1/users/me`).then(user => {
       showUser(user);
       showCourses(user.courses);
       showGroups(user.groups);
    })
}

function showCourses(courses) {
    $('#course-table-head').html(`
         <th scope="col">Name</th>
         <th scope="col">Teacher</th>
    `);
    courses.forEach(course => {
        getRequest(`/api/v1/users/${course.ownerId}`).then(owner => {
            $('#course-table-content').append(`
            <tr>
                <td><a href="/course?id=${course.id}">${course.name}</a></td>
                <td>${owner.name}</td>
            </tr>
        `);
        })
    });

}

function showGroups(groups) {
    $('#group-table-head').html(`
         <th scope="col">Name</th>
         <th scope="col">Teacher</th>
    `);
    groups.forEach(group => {
        getRequest(`/api/v1/users/${group.ownerId}`).then(owner => {
            $('#group-table-content').append(`
            <tr>
                <td><a href="/group?id=${group.id}">${group.name}</a></td>
                <td>${owner.name}</td>
            </tr>
        `);
        })
    });

}

function showUser(user) {
    $('#user-name').text(user.name);
    $('#user-email').text(user.email);
}
function getRequest(url) {
    return $.get(url);
}
