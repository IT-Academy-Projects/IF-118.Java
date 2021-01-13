init();

function init() {
    getRequest(`api/v1/users/me`).then(user => {
        showUser(user);
        let role = user.roles.find(role => role.name === "TEACHER");
        if (role === undefined) {
            showStudentCourses(user.courses);
            $('#create-course-button, #create-group-button').hide();
        } else {
            showTeacherCourses();
        }
        showGroups(user.groups);
    })
}

function showStudentCourses(courses) {
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

function showTeacherCourses() {
    $('#course-table-head').html(`
         <th scope="col">Name</th>
    `);
    $.get(`/api/v1/courses`).then(courses => {
        courses.forEach(course => {
            $('#course-table-content').append(`
            <tr>
                <td><a href="/course?id=${course.id}">${course.name}</a></td>
            </tr>
            `)
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
