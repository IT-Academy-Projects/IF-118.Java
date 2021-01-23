let currentUserId;
const urlCreator = window.URL || window.webkitURL;

// init();

function init() {
    const urlParams = new URLSearchParams(window.location.search);
    let content = urlParams.get('content');
    getRequest(`api/v1/users/me`).then(user => {
        currentUserId = user.id;
        let invitationCode = user.invitationCode;
        if (invitationCode !== null) {
            getRequest(`api/v1/invitation/${invitationCode}`).then(invitation => {
                    if (invitation.courseOrGroupId !== null) {
                        fetch(`api/v1/users/${user.id}/delete/${invitation.id}`, {method: 'PATCH'});
                        window.location.replace(`${invitation.courseOrGroup}?id=${invitation.courseOrGroupId}`);
                    }
                }
            )

        }

        let role = user.roles.find(role => role.name === "TEACHER");
        if (content === 'groups') {
            initGroupContent(role, user);
            $('#groups-btn').addClass('active');
        } else {
            initCourseContent(role, user);
            $('#courses-btn').addClass('active');
        }

    })
}

function initCourseContent(role, user) {
    $('#courses-wrapper').show();
    if (role === undefined) {
        showStudentCourses(user.courses);
    } else {
        showTeacherCourses();
    }
}

function initGroupContent(role, user) {
    $('#groups-wrapper').show();
    if (role === undefined) {
        showStudentGroups(user.groups);
    } else {
        showTeacherGroups();
    }
}

function showStudentCourses(courses) {
    if (courses.length < 1) {
        showNoCoursesText();
    } else {
        courses.forEach(course => {
            addCourseCard(course)
        });
    }
}

function showTeacherCourses() {
    $('#courses-wrapper').append(`
            <div class="card custom-card" data-toggle="modal" data-target="#create-course-modal" style="width: 280px; height: 100%; border: solid #343a408f 1px; display: flex; justify-content: center; align-items: center">
                <img src="img/plus.png" alt="Course image" width="128" height="128">
            </div>
        `);
    getRequest(`/api/v1/courses`).then(courses => {
        courses.forEach(course => {
            addCourseCard(course);
        })
    })
}

function showStudentGroups(groups) {
    if (groups.length < 1) {
        showNoGroupsText();
    } else {
        groups.forEach(group => {
            addGroupCard(group)
        });
    }
}

function showTeacherGroups() {
    $('#groups-wrapper').append(`
            <div class="card custom-card" data-toggle="modal" data-target="#create-group-modal" style="width: 280px; height: 100%; border: solid #343a408f 1px; display: flex; justify-content: center; align-items: center">
                <img src="img/plus.png" alt="Course image" width="128" height="128">
            </div>
    `);
    addCheckboxOfCourses();
    $.get(`/api/v1/groups/owner/${currentUserId}`).then(groups => {
        groups.forEach(group => {
            addGroupCard(group);
        });
    });
}

function addGroupCard(group) {
    let avatar = group.hasAvatar ? `/api/v1/groups/${group.id}/avatar` : `img/group-placeholder.jpg`;
    $('#groups-wrapper').prepend(`
                <div class="card custom-card">
                    <a class="custom-link" href="/group?id=${group.id}">
                        <img class="card-img-top" src="${avatar}" alt="Course image" height="280">
                    </a>
                    <div class="card-body">
                       <h5 class="card-title">${group.name}</h5>
                       <a href="/group?id=${group.id}" class="btn btn-primary">Follow</a>
                    </div>
                </div>
    `);
}

function addCourseCard(course) {
    let avatar = course.hasAvatar ? `/api/v1/courses/${course.id}/avatar` : `img/course-placeholder.jpg`;
    $('#courses-wrapper').prepend(`
        <div class="card custom-card">
            <a class="custom-link" href="/course?id=${course.id}">
                <img class="card-img-top" src="${avatar}" alt="Course image" height="280">
            </a>
            <div class="card-body">
                <h5 class="card-title">${course.name}</h5>
                <p class="card-text">${course.description}</p>
                <a href="/course?id=${course.id}" class="btn btn-primary">Learn</a>
            </div>
        </div>
    `);
}

function addCheckboxOfCourses() {
    $.get(`/api/v1/courses`).then(courses => {
        courses.forEach(course => {
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

function showNoCoursesText() {
    let noCoursesText = '<div style="height: 70vh; display: flex; justify-content: center"><span style="font-size:36px; color: grey; align-self: center; margin-top: 10px">You haven`t active courses</span></div>';
    $('#wrapper').html(noCoursesText);
}

function showNoGroupsText() {
    let noGroupText = '<div style="height: 70vh; display: flex; justify-content: center"><span style="font-size:36px; color: grey; align-self: center; margin-top: 10px">You are not member of any group</span></div>';
    $('#wrapper').html(noGroupText);
}

function getRequest(url) {
    return $.get(url);
}
