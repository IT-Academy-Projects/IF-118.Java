let materialId;
let noMaterialText = '<span style="font-size:36px; color: grey; align-self: center; margin-top: 10px">Oops... Cannot find such material.</span>';

let canEdit;
let tempAssignmentId;
let tempAnswerId;
let currentUser;
let assignmentAnswerId;

initPage();


function initPage() {
    const urlParams = new URLSearchParams(window.location.search);
    materialId = urlParams.get('id');
    if (materialId !== undefined && materialId !== null && materialId !== '') {
        getMaterial(materialId);
    } else {
        $('#main').html(noMaterialText)
    }
}

function getMaterial(id) {
    getRequest(`/api/v1/materials/${id}`).then(material => {

        getRequest(`/api/v1/users/me`).then(user => {
            currentUser = user;
            let role = user.roles.find(role => role.name === "TEACHER");
            if (role !== null && role !== undefined && user.id === material.ownerId) {
                canEdit = true;
                $('#due-date').attr('min', getToday());
                $('#due-time').attr('min', getCurrentTime());
            } else {
                canEdit = false;
                $('#add-assignment-btn').hide();
                $('#set-due-date-time-form').hide();
            }

            $('#materials').append(`
                <div class="material">
                    <div class="material-name">${material.name}</div>
                    <div class="material-description">${material.description}</div>
                    <div class="material-download">Download: <a href="/api/v1/materials/${material.id}/file">${material.name}</a></div>
                </div>
            `);
            if(material.dueDateTime !== null) {
                $('.material-description').append(
                    `<div class="material-due-date-time">Finish this lection by <span id="due-date-time">${material.dueDateTime}</span></div>`
                );
            }

            material.assignments.forEach(assignment => {

                let buttons = canEdit ? `<button class="btn btn-outline-info" onclick="toggleAnswers(${assignment.id})">View answers</button>` :
                    `<button type="button" id="answer-${assignment.id}-btn" class="btn btn-outline-success"
                        data-toggle="modal" data-target="#create-answer-modal" onclick="tempAssignmentId = ${assignment.id}">Answer</button>`;

                let url = assignment.fileReference !== null ? `Download: <a href="/api/v1/assignments/${assignment.id}/file">${assignment.name}</a>` : '';

                $('#assignments').append(`
                    <div class="assignment">
                        <button class="btn btn-primary btn-block text-left" type="button" data-toggle="collapse" data-target="#collapse-${assignment.id}" aria-expanded="false" aria-controls="collapse">
                            <span class="assignment-name">
                                ${assignment.name}
                            </span>
                        </button>
                        <div class="collapse" id="collapse-${assignment.id}">
                            <div class="card card-body">
                                <div class="assignment-description">${assignment.description}</div>
                                <div class="assignment-description">${url}</div>
                                <div style="margin-top: 10px">
                                     ${buttons}
                                </div>
                                <div id="answer-${assignment.id}-review" class="answer-body">
                                    <div id="answer-${assignment.id}-review-body">
                                        <table class="table">
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

        });

    })
}

function createAssignment() {
    let data = {
        name: $('#assignment-name').val(),
        description: $('#assignment-description').val(),
        materialId: materialId
    }

    let formData = new FormData();
    formData.append('assignment', JSON.stringify(data));
    let file = $('#assignment-file').prop('files')[0];
    if (file !== null) {
        formData.append('file', file);
    }
    postFileRequest(`/api/v1/assignments`, formData, (res) => {
        $('#assignments').html('');
        $('#materials').html('');
        $('#create-assignment-modal').modal('hide');
        getMaterial(materialId);
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
                <td><span>${answer.grade}</span></td>
                <td><button type="button" id="answer-${answer.id}-btn" class="btn btn-outline-success"
                        data-toggle="modal" data-target="#update-answer-modal" onclick="tempAnswerId = ${answer.id}">Change answer</button></td>
                <td><button type="button" id="submit-${answer.id}-btn" class="btn btn-outline-success" 
                        onclick=submit(${answer.id})>Submit</button></td>
            </tr>
        `);
    let submit = '#submit-' + answer.id + '-btn'
    $(submit).attr('disabled', answer.isSubmitted)

    $(`#answer-${assignmentId}-review`).toggle();
    $(`#answer-${assignmentId}-btn`).hide();
}

function showAnswers(assignment) {
    let answerTable = "#answer-" + assignment.id+ "-table";
    $(answerTable).innerHTML = ''
    assignment.assignmentAnswers.forEach(answer => {
        if (answer.isSubmitted === true) {
            getRequest(`/api/v1/users/${answer.ownerId}`).then(user => {
                $(answerTable).append(`
                <tr>
                    <td><span style="font-weight: bold" class="h3">${user.name}</span></td>
                    <td><a href="/api/v1/assignment-answers/${answer.id}/file">Answer</a></td>
                    <td><span id="grade-${answer.id}">${answer.grade}</span></td>
                    <td><button type="button" id="grade-${answer.id}-btn" class="btn btn-outline-success" 
                        data-toggle="modal" data-target="#grade-modal" onclick="assignmentAnswerId = ${answer.id}">Grade</button></td>
                </tr>
                `);
            });
        }
    });
}

function setDueDateTime() {
    let dueDateTime = $('#due-date').val() + "T" + $('#due-time').val();
    $('#due-date-time').text(dueDateTime);

    patchRequest(`/api/v1/materials/` + materialId + `/duedate`, dueDateTime);
}

function getToday() {
    let d = new Date();

    let month = d.getMonth()+1;
    let day = d.getDate();

    return d.getFullYear() + '-' +
        (month<10 ? '0' : '') + month + '-' +
        (day<10 ? '0' : '') + day;
}

function getCurrentTime() {
    let d = new Date();
    return d.getHours() + ":" + d.getMinutes();
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
    formData.append('answerId', tempAnswerId);
    patchFileRequest(`/api/v1/assignment-answers`, formData).then(res => {
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
}

function submit(id) {
    patchRequest(`/api/v1/assignment-answers/${id}/submit`).then($('#submit-' + id + '-btn').attr('disabled', true));
}

function getRequest(url) {
    return $.get(url);
}

function postRequest(url, data) {
    return $.ajax({
        url: url,
        type: 'POST',
        data: JSON.stringify(data),
        contentType: 'application/json; charset=utf-8'
    });
}

function postFileRequest(url, data, callback) {
    return $.ajax({
        url: url,
        type: 'POST',
        data: data,
        processData: false,
        contentType: false,
        enctype: 'multipart/form-data',
        success: function(res) {
            if (callback)
                callback(res)
        }
    });
}

function patchFileRequest(url, data) {
    return $.ajax({
        url: url,
        type: 'PATCH',
        data: data,
        processData: false,
        contentType: false,
        enctype: 'multipart/form-data'
    });
}

function patchRequest(url, data) {
    return $.ajax({
        url: url,
        type: 'PATCH',
        data: JSON.stringify(data),
        contentType: 'application/json; charset=utf-8'
    })
}