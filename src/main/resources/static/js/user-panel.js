let currentUserId;

init();

function init() {
    const urlParams = new URLSearchParams(window.location.search);
    let content = urlParams.get('content');

    getRequest(`api/v1/users/me`).then(user => {
        currentUserId = user.id;
        let role = user.roles.find(role => role.name === "TEACHER");
        if (content === 'groups') {
            initGroupContent(role, user);

        } else {
            initCourseContent(role, user);
        }
    })
}

function initCourseContent(role, user) {
    let courseWrapper = $('#courses-wrapper');
    courseWrapper.show();
    if (role === undefined) {
        showStudentCourses(user.courses);
    } else {
        courseWrapper.append(`
            <div class="card custom-card" data-toggle="modal" data-target="#create-course-modal" style="width: 280px; height: 100%; border: solid #343a408f 1px; display: flex; justify-content: center; align-items: center">
                <img src="images/plus.png" alt="Course image" width="128" height="128">
            </div>
        `);
        showTeacherCourses();
    }
}

function initGroupContent(role, user) {
    let groupsWrapper = $('#groups-wrapper');
    groupsWrapper.show();
    if (role === undefined) {
        showStudentGroups(user.groups);
    } else {
        groupsWrapper.append(`
            <div class="card custom-card" data-toggle="modal" data-target="#create-group-modal" style="width: 280px; height: 100%; border: solid #343a408f 1px; display: flex; justify-content: center; align-items: center">
                <img src="images/plus.png" alt="Course image" width="128" height="128">
            </div>
        `);
        showTeacherGroups();
    }
}

function showStudentCourses(courses) {
    if (courses.length < 1) {
        showNoCoursesText();
    } else {
        courses.forEach(course => {
            $('#courses-wrapper').append(`
                 <div class="card custom-card">
                        <a class="custom-link" href="/course?id=${course.id}">
                            <img class="card-img-top" src="images/course-placeholder.jpg" alt="Course image" height="280">
                        </a>
                        <div class="card-body">
                            <h5 class="card-title">${course.name}</h5>
                            <p class="card-text">${course.description}</p>
                            <a href="/course?id=${course.id}" class="btn btn-primary">Learn</a>
                        </div>
                 </div>
            `);
        });
    }
}

function showTeacherCourses() {
    getRequest(`/api/v1/courses`).then(courses => {
        courses.forEach(course => {
            $('#courses-wrapper').prepend(`
                    <div class="card custom-card">
                        <a class="custom-link" href="/course?id=${course.id}">
                            <img class="card-img-top" src="images/course-placeholder.jpg" alt="Course image" height="280">
                        </a>
                            <div class="card-body">
                                <h5 class="card-title">${course.name}</h5>
                                <p class="card-text">${course.description}</p>
                                <a href="/course?id=${course.id}" class="btn btn-primary">Learn</a>
                            </div>
                    </div>
            `);
        })
    })
}

function showStudentGroups(groups) {
    if (groups.length < 1) {
        showNoGroupsText();
    } else {
        groups.forEach(group => {
            $('#groups-wrapper').append(`
                <div class="card custom-card">
                    <a class="custom-link" href="/group?id=${group.id}">
                        <img class="card-img-top" src="images/group-placeholder.jpg" alt="Course image" height="280">
                    </a>
                    <div class="card-body">
                       <h5 class="card-title">${group.name}</h5>
                       <a href="/group?id=${group.id}" class="btn btn-primary">Follow</a>
                    </div>
                </div>
            `);
        });
    }
}

function showTeacherGroups() {
    $.get(`/api/v1/groups/owner/${currentUserId}`).then(groups => {
        groups.forEach(group => {
            $('#groups-wrapper').prepend(`
                <div class="card custom-card">
                    <a class="custom-link" href="/group?id=${group.id}">
                        <img class="card-img-top" src="images/group-placeholder.jpg" alt="Course image" height="280">
                    </a>
                    <div class="card-body">
                       <h5 class="card-title">${group.name}</h5>
                       <a href="/group?id=${group.id}" class="btn btn-primary">Follow</a>
                    </div>
                </div>
            `);
        });
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
