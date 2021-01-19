init();

function init() {
    getRequest(`api/v1/users/me`).then(user => {
        showUser(user);
        let role = user.roles.find(role => role.name === "TEACHER");
        if (role === undefined) {
            showStudentCourses(user.courses);
            showStudentGroups(user.groups);
            $('#create-course-button, #create-group-button').hide();
        } else {
            showTeacherCourses();
            showTeacherGroups();
        }
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
            `);
            $('#courses').append(`
                <div class="form-check">
                    <input class="form-check-input" id="course-${course.id}" type="checkbox" value="${course.id}">
                    <label class="form-check-label" for="course-${course.id}">
                        ${course.name}
                    </label>
                </div>
            `)
        })
    });
}

function showStudentGroups(groups) {
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

function showTeacherGroups() {
    $('#group-table-head').html(`
         <th scope="col">Name</th>
    `);
    $.get(`/api/v1/groups/owner/`+$('#user-id').text()).then(groups => {
        groups.forEach(group => {
            $('#group-table-content').append(`
            <tr>
                <td><a href="/group?id=${group.id}">${group.name}</a></td>
            </tr>
            `)
        })
    });
}

function showUser(user) {
    $('#user-name').text(user.name);
    $('#user-email').text(user.email);
    $('#user-id').text(user.id);
}

function getRequest(url) {
    return $.get(url);
}
