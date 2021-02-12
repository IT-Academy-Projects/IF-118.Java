let currentUserId;
let searchQuery;
const urlCreator = window.URL || window.webkitURL;

function init() {
    const urlParams = new URLSearchParams(window.location.search);
    searchQuery = urlParams.get('searchQuery');
    getRequest(`api/v1/users/me`).then(user => {
        currentUserId = user.id;
        showCourses();
        showGroups();
    })
}

function showCourses() {
    $('#courses-wrapper').show();
    getRequest(`/api/v1/search/course?searchQuery=${searchQuery}`).then(courses => {
        courses.forEach(course => {
            addCourseCard(course);
        })
    })
}


function showGroups() {
    $('#groups-wrapper').show();
    addCheckboxOfCourses();
    $.get(`/api/v1/search/group?searchQuery=${searchQuery}`).then(groups => {
        groups.forEach(group => {
            addGroupCard(group);
        });
    });
}

function addGroupCard(group) {
    let avatar = group.imageId !== null  ? `/api/v1/images/${group.imageId}` : `img/group-placeholder.jpg`;
    $('#groups-wrapper').prepend(`
                <div class="card custom-card">
                    <a class="custom-link" href="/group?id=${group.id}">
                        <img class="card-img-top" src="${avatar}" alt="Course image" height="
                        ">
                    </a>
                    <div class="card-body">
                       <h5 class="card-title">${group.name}</h5>
                       <a href="/group?id=${group.id}" class="btn btn-warning">Follow</a>
                    </div>
                </div>
    `);
}

function addCourseCard(course) {
    let avatar = course.imageId !== null ? `/api/v1/images/${course.imageId}` : `img/course-placeholder.jpg`;
    $('#courses-wrapper').prepend(`
        <div class="card custom-card">
            <a class="custom-link" href="/course?id=${course.id}">
                <img class="card-img-top" src="${avatar}" alt="Course image" height="280">
            </a>
            <div class="card-body">
                <h5 class="card-title">${course.name}</h5>
                <p class="card-text">${course.description}</p>
                <a href="/course?id=${course.id}" class="btn btn-warning">Learn</a>
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
