const urlParams = new URLSearchParams(window.location.search);
const id = urlParams.get('id');

let currentUser;
let tempAssignments;
let canEdit;
let tempAssignmentId;
let tempAnswerId;
let assignmentAnswerId;
let tempGroupId;

showGroupInfo();

function showGroupInfo() {

    $.get(`api/v1/users/me`).then(user => {
        currentUser = user;
        if (user.roles.find(role => role.name === "STUDENT")) {
            $('#invite-to-group-button').hide();
            $('#statistic-btn').show();
        } else {
            $('#group-statistic-btn').show();
        }
    });

    $('#table-content').html('');
    $.ajax(`/api/v1/groups/${id}`).then(group => {
        $('#group-name').text(group.name);
        tempGroupId = group.id;
        tempAssignments = group.assignments
        let role = currentUser.roles.find(role => role.name === "TEACHER");
        if (role !== null && role !== undefined && currentUser.id === group.ownerId) {
            canEdit = true;
        } else {
            canEdit = false;
            $('#add-assignment-btn').hide();
            $('#expiration-date-block').hide();
            $('#open-material-block').hide();
        }

        $.get(`/api/v1/users/${group.ownerId}`).then(user => {
            $('#owner').text(user.name);
        })
        $('#students').append(`
            <table class="table borderless">
                <tbody id="student-${tempGroupId.id}-table">
                </tbody>
            </table>
        `)
        showStudents(group.users)

        $('#courses').append(`
            <table class="table borderless">
                <tbody id="course-${tempGroupId.id}-table">
                </tbody>
            </table>
        `)
        showCourses(group.courses)

        showAssignments()
    });

}

function handleChatButton() {
    window.location.replace(`/group-chat?id=${id}`);
}

function handleMyStatisticButton() {
    window.location.replace(`/my-group-statistic?id=${id}`);
}

function handleGroupStatisticButton() {
    window.location.replace(`/group-statistic?id=${id}`);
}

function showStudents(students) {
    let studentTable = "#student-" + tempGroupId.id + "-table";
    $(studentTable).innerHTML = ''
    students.forEach(student => {
        $(studentTable).append(`
        <tr>
            <td><span style="font-weight: bold" class="h3">${student.name}</span></td>
        </tr>
        `);
    });
}

function showCourses(courses) {
    let courseTable = "#course-" + tempGroupId.id + "-table";
    $(courseTable).innerHTML = ''
    courses.forEach(course => {
        $(courseTable).append(`
        <tr>
            <td><span><a href="/course?id=${course.id}">${course.name}</a></span></td>
        </tr>
        `);
    });
}

function showAssignments() {

    $('#assignments').innerHTML = ''
    $.ajax(`/api/v1/groups/${id}`).then(group => {
        tempAssignments = group.assignments
        tempAssignments.forEach(assignment => {

            let buttons = canEdit ? `<button class="btn" style="background-color: #FFC312" onclick="toggleAnswers(${assignment.id})">View answers</button>` :
                `<button type="button" id="answer-${assignment.id}-btn" class="btn" style="background-color: #FFC312"
                data-toggle="modal" data-target="#create-answer-modal" onclick="tempAssignmentId = ${assignment.id}">Answer</button>`;
            let url = assignment.fileReference !== null ? `Download: <a href="/api/v1/assignments/${assignment.id}/file">${assignment.name}</a>` : '';

            $('#assignments').append(`
            <div class="assignment">
                <div class="assignment-buttons rounded-top">
                    <button class="btn bg-transparent btn-block text-left" type="button" data-toggle="collapse" data-target="#collapse-${assignment.id}" aria-expanded="false" aria-controls="collapse">
                        <span class="assignment-name">
                            ${assignment.name}
                        </span>
                    </button>
                    ${canEdit ? `<button class="btn bg-transparent" type="button" 
                                        data-toggle="modal" data-target="#update-assignment-modal"
                                        onclick="tempAssignmentId = ${assignment.id}; $('#new-assignment-name').val('${assignment.name}'); 
                                        $('#new-assignment-description').val('${assignment.description}');checkAssignment()"><div class="fas fa-edit"></div></button>
                                 <button class="btn bg-transparent" type="button" onclick="tempAssignmentId = ${assignment.id};
                                 deleteAssignment()"><div class="fas fa-times"></div></button>` : ``}
                </div>
                <div class="collapse" id="collapse-${assignment.id}">
                    <div class="card card-body">
                        <div class="assignment-description">${assignment.description}</div>
                        <div class="assignment-description">${url}</div>
                        <div style="margin-top: 10px">
                             ${buttons}
                        </div>
                        <div id="answer-${assignment.id}-review" class="answer-body">
                            <div id="answer-${assignment.id}-review-body">
                                <table class="table borderless">
                                    <tbody id="answer-${assignment.id}-table">
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>`);
            if (!canEdit) {
                let myAnswer = assignment.assignmentAnswers.find(answer => answer.ownerId === currentUser.id);
                if (myAnswer) {
                    showMyAnswer(myAnswer, assignment.id);
                }
            } else {
                showAnswers(assignment)
            }
        })
    })
}

function createAssignment() {
    let data = {
        name: $('#assignment-name').val(),
        description: $('#assignment-description').val(),
        groupId: tempGroupId
    }

    let formData = new FormData();
    formData.append('assignment', JSON.stringify(data));
    let file = $('#assignment-file').prop('files')[0];
    if (file !== null) {
        formData.append('file', file);
    }
    postFileRequest(`/api/v1/assignments`, formData, (res) => {
        $('#assignments').html('');
        $('#create-assignment-modal').modal('hide');
        showAssignments();
    })
}

function updateAssignment() {
    let data = {
        name: $('#new-assignment-name').val(),
        description: $('#new-assignment-description').val(),
        groupId: tempGroupId
    }

    let formData = new FormData();
    formData.append('assignment', JSON.stringify(data));
    let file = $('#new-assignment-file').prop('files')[0];
    if (file !== null) {
        formData.append('file', file);
    }
    patchFileRequest(`/api/v1/assignments/${tempAssignmentId}`, formData, (res) => {
        $('#assignments').html('');
        $('#materials').html('');
        $('#update-assignment-modal').modal('hide');
        showAssignments();
    })
}

function deleteAssignment() {
    deleteRequest(`/api/v1/assignments/${tempAssignmentId}`, null, () => {
        showAssignments();
    })
}

function checkAnswerFile() {
    if ($('#answer-file').val().length > 0) {
        $('#create-answer-btn').removeAttr('disabled');
    } else {
        $('#create-answer-btn').attr('disabled', true);
    }
    if ($('#new-answer-file').val().length > 0) {
        $('#update-answer-btn').removeAttr('disabled');
    } else {
        $('#update-answer-btn').attr('disabled', true);
    }
}

function showMyAnswer(answer, assignmentId) {
    let answerTable = "#answer-" + assignmentId + "-table"
    $(answerTable).innerHTML = ''
    $(answerTable).append(`
            <tr>
                <td><span style="font-weight: bold" class="h3">${currentUser.name}</span></td>
                <td><a href="/api/v1/assignment-answers/${answer.id}/file">Answer</a></td>
                ${answer.status === 'REJECTED' ? `<td><span>Rejected</span></td>` : `<td><span>${answer.grade}</span></td>`}
                <td><button type="button" id="change-${answer.id}-btn" class="btn btn-danger"
                        data-toggle="modal" data-target="#update-answer-modal" onclick="tempAnswerId = ${answer.id}">Change answer</button></td>
                <td><button type="button" id="submit-${answer.id}-btn" class="btn btn-success" 
                        onclick=submit(${answer.id})>Submit</button></td>
            </tr>
        `);
    $('#submit-' + answer.id + '-btn').attr('disabled', answer.status !== 'NEW')
    $('#change-' + answer.id + '-btn').attr('disabled', answer.status === 'SUBMITTED' || answer.status === 'GRADED')

    $(`#answer-${assignmentId}-review`).toggle();
    $(`#answer-${assignmentId}-btn`).hide();
}

function showAnswers(assignment) {
    let answerTable = "#answer-" + assignment.id + "-table";
    $(answerTable).innerHTML = ''
    console.log(assignment)
    assignment.assignmentAnswers.forEach(answer => {
        if (answer.status === 'SUBMITTED' || answer.status === 'GRADED') {
            getRequest(`/api/v1/users/${answer.ownerId}`).then(user => {
                $(answerTable).append(`
                <tr>
                    <td><span style="font-weight: bold" class="h3">${user.name}</span></td>
                    <td><a href="/api/v1/assignment-answers/${answer.id}/file">Answer</a></td>
                    <td><span id="grade-${answer.id}">${answer.grade}</span></td>
                    <td><button type="button" id="grade-${answer.id}-btn" class="btn btn-success" 
                        data-toggle="modal" data-target="#grade-modal" onclick="assignmentAnswerId = ${answer.id}">Grade</button></td>
                    <td><button type="button" id="reject-${answer.id}-btn" class="btn btn-danger" 
                        onclick="reject(${answer.id})">Reject</button></td>
                </tr>
                `);
                $('#reject-' + answer.id + '-btn').attr('disabled', answer.status === 'GRADED')
                $('#grade-' + answer.id + '-btn').attr('disabled', answer.status === 'REJECTED')
            });
        }
    });
}

function toggleAnswers(id) {
    $(`#answer-${id}-review`).toggle();
}

function createAnswer() {
    let data = {
        ownerId: currentUser.id,
        assignmentId: tempAssignmentId
    }

    let file = $('#answer-file').prop('files')[0];

    let formData = new FormData();
    formData.append('file', file);
    formData.append('assignmentAnswer', JSON.stringify(data));
    postFileRequest(`/api/v1/assignment-answers`, formData, res => {
        showMyAnswer(res, tempAssignmentId);
        $('#create-answer-modal').modal('hide');
    })
}

function updateAnswer() {

    let file = $('#new-answer-file').prop('files')[0];

    let formData = new FormData();
    formData.append('file', file);
    patchFileRequest(`/api/v1/assignment-answers/${tempAnswerId}`, formData).then(res => {
        showMyAnswer(res, tempAssignmentId);
        $('#update-answer-modal').modal('hide');
        file = '';
    })
    let submit = '#submit-' + tempAnswerId + '-btn'
    $(submit).removeAttr('disabled')
}

function gradeAssignmentAnswer() {
    let gr = parseInt($('#grade-textarea').val());
    let data = {grade: gr};
    patchRequest(`/api/v1/assignment-answers/${assignmentAnswerId}/grade`, data).then(() => {
        $(`#grade-${assignmentAnswerId}`)[0].innerHTML = gr;
        $('#grade-modal').modal('hide');
        gr = '';
    })
}

function checkAssignment() {
    if ($('#assignment-name').val().length > 0 && $('#assignment-description').val().length > 0) {
        $('#create-assignment-btn').removeAttr('disabled');
    } else {
        $('#create-assignment-btn').attr('disabled', true);
    }
    if ($('#new-assignment-name').val().length > 0 && $('#new-assignment-description').val().length > 0) {
        $('#update-assignment-btn').removeAttr('disabled');
    } else {
        $('#update-assignment-btn').attr('disabled', true);
    }
}

function submit(id) {
    patchRequest(`/api/v1/assignment-answers/${id}/submit`).then(() => {
        $('#submit-' + id + '-btn').attr('disabled', true)
        $('#change-' + id + '-btn').attr('disabled', true)
    })
}

function reject(id) {
    patchRequest(`/api/v1/assignment-answers/${id}/reject`).then(() => {
        $('#reject-' + id + '-btn').attr('disabled', true)
        $('#grade-' + id + '-btn').attr('disabled', true)
    })
}

function getRequest(url) {
    return $.get(url);
}

function postFileRequest(url, data, callback) {
    return $.ajax({
        url: url,
        type: 'POST',
        data: data,
        processData: false,
        contentType: false,
        enctype: 'multipart/form-data',
        success: function (res) {
            if (callback)
                callback(res)
        }
    });
}

function patchFileRequest(url, data, callback) {
    return $.ajax({
        url: url,
        type: 'PATCH',
        data: data,
        processData: false,
        contentType: false,
        enctype: 'multipart/form-data',
        success: function (res) {
            if (callback)
                callback(res)
        }
    });
}

function patchRequest(url, data, callback) {
    return $.ajax({
        url: url,
        type: 'PATCH',
        data: JSON.stringify(data),
        contentType: 'application/json; charset=utf-8',
        success: function (res) {
            if (callback)
                callback(res)
        }
    })
}

function deleteRequest(url, data, callback) {
    return $.ajax({
        url: url,
        type: 'DELETE',
        data: JSON.stringify(data),
        contentType: 'application/json; charset=utf-8',
        success: function (res) {
            if (callback)
                callback(res)
        }
    })
}