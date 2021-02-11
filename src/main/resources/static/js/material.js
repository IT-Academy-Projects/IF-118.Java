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
            } else {
                canEdit = false;
                $('#add-assignment-btn').hide();
            }
            $('#materials').append(`
                <div class="material">
                    <div class="row">
                        <div class="col-lg-4">
                            <div class="material-name">Title: ${material.name}</div>
                            <div class="material-description">Description: ${material.description}</div>
                            <div class="material-download">Download: <a href="/api/v1/materials/${material.id}/file">${material.name}</a></div>
                        </div>
                    </div>
                </div>
            `);
            if (canEdit) {
                $('#materials .material .row').append(`
                    <div class="col-lg-4" id="open-material-block">
                        <form id="open-material">
                            Open ${material.name} for groups:
                            <div class="form-check"></div>
                            <button id="open-material-btn" type="button" class="btn btn-outline-success show"
                                    onclick="openMaterial(${material.id})">Submit
                            </button>
                        </form>
                    </div>
                    <div class="col-lg-4" id="opened-for-groups">
                        <table class="table">
                            <thead class="thead-dark">
                                <tr>
                                    <th>Group</th>
                                    <th>Expiration date</th>
                                </tr>
                            </thead>
                            <tbody></tbody>
                        </table>                        
                    </div>
                    
                `);
                getGroupsWithClosedMaterial(id);
                $.when(getGroupsWithOpenedMaterial(id).then(groups => {
                        for (let i = 0; i < groups.length; i++) {
                            $('#opened-for-groups table tbody').append(`
                                <tr id="open-group-${groups[i].id}">
                                    <td id="name">${groups[i].name}</td>
                                    <td id="exp-date">No expiration date yet.
                                        <form id="set-expiration">
                                            <input id="expiration-date-${groups[i].id}-${material.id}" type="date" min="` + getDayAfterToday(0) + `" value="` + getDefaultExpirationDate() + `">
                                            <button id="set-expiration-date-btn" type="button" class="btn btn-outline-success show"
                                                    onclick="setExpiration(${material.id}, ${groups[i].id})">Set date
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            `);
                        }
                    })
                ).done(function () {
                    getExpirations(id).then(expirations => {
                        for (let i = 0; i < expirations.length; i++) {
                            $('#open-group-' + expirations[i].groupId + ' #exp-date').html(expirations[i].expirationDate + `
                                <form id="set-expiration">
                                    <input id="expiration-date-${expirations[i].id}" type="date" min="` + getDayAfterToday(0) + `" value="` + getDefaultExpirationDate() + `">
                                    <button id="set-expiration-date-btn" type="button" class="btn btn-outline-success show"
                                            onclick="updateExpiration(${expirations[i].id})">Update
                                    </button>
                                </form>
                            `)
                        }
                    })
                });
            } else {
                getExpirations(id).then(expirations => {
                    for (let i = 0; i < expirations.length; i++) {
                        $('.material-description').append(`<p>Expiration time: ${expirations[i].expirationDate}</p>`);
                    }
                });
            }
            // material.assignments.forEach(assignment => {
            //
            //     let buttons = canEdit ? `<button class="btn btn-outline-info" onclick="toggleAnswers(${assignment.id})">View answers</button>` :
            //         `<button type="button" id="answer-${assignment.id}-btn" class="btn btn-outline-success"
            //             data-toggle="modal" data-target="#create-answer-modal" onclick="tempAssignmentId = ${assignment.id}">Answer</button>`;
            //
            //     let url = assignment.fileReference !== null ? `Download: <a href="/api/v1/assignments/${assignment.id}/file">${assignment.name}</a>` : '';
            //
            //     $('#assignments').append(`
            //         <div class="assignment">
            //             <div class="assignment-buttons">
            //                 <button class="btn btn-primary btn-block text-left" style="margin-right: 10px" type="button" data-toggle="collapse" data-target="#collapse-${assignment.id}" aria-expanded="false" aria-controls="collapse">
            //                     <span class="assignment-name">
            //                         ${assignment.name}
            //                     </span>
            //                 </button>
            //                 ${canEdit ? `<button class="btn btn-primary" style="margin-right: 10px" type="button"
            //                                     data-toggle="modal" data-target="#update-assignment-modal"
            //                                     onclick="tempAssignmentId = ${assignment.id}; $('#new-assignment-name').val('${assignment.name}');
            //                                     $('#new-assignment-description').val('${assignment.description}');checkAssignment()">Edit</button>
            //                              <button class="btn btn-primary" type="button" onclick="tempAssignmentId = ${assignment.id};
            //                              deleteAssignment()">Delete</button>` : ``}
            //             </div>
            //             <div class="collapse" id="collapse-${assignment.id}">
            //                <div class="card card-body">
            //                          <div class="assignment-description">${assignment.description}</div>
            //                          <div class="assignment-description">${url}</div>
            //                    <div style="margin-top: 10px">
            //                          ${buttons}
            //                    </div>
            //                    <div id="answer-${assignment.id}-review" class="answer-body">
            //                         <div id="answer-${assignment.id}-review-body">
            //                            <table class="table">
            //                                <tbody id="answer-${assignment.id}-table">
            //                               </tbody>
            //                             </table>
            //                          </div>
            //                      </div>
            //                </div>
            //             </div>
            //         </div>`);
            //     //     if (!canEdit) {
            //     //         let myAnswer = assignment.assignmentAnswers.find(answer => answer.ownerId === currentUser.id);
            //     //         if (myAnswer) {
            //     //             showMyAnswer(myAnswer, assignment.id);
            //     //         }
            //     //     } else {
            //     //         showAnswers(assignment)
            //     //     }
            //     //
            //     // })
            //
            // });
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

function updateAssignment() {
    let data = {
        name: $('#new-assignment-name').val(),
        description: $('#new-assignment-description').val(),
        materialId: materialId
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
        getMaterial(materialId);
    })
}

function deleteAssignment() {
    deleteRequest(`/api/v1/assignments/${tempAssignmentId}`, null, () => {
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
                ${answer.status === 'REJECTED' ? `<td><span>Rejected</span></td>` : `<td><span>${answer.grade}</span></td>`}
                <td><button type="button" id="answer-${answer.id}-btn" class="btn btn-outline-success"
                        data-toggle="modal" data-target="#update-answer-modal" onclick="tempAnswerId = ${answer.id}">Change answer</button></td>
                <td><button type="button" id="submit-${answer.id}-btn" class="btn btn-outline-success" 
                        onclick=submit(${answer.id})>Submit</button></td>
            </tr>
        `);
    let submit = '#submit-' + answer.id + '-btn'
    $(submit).attr('disabled', answer.status === 'SUBMITTED')

    $(`#answer-${assignmentId}-review`).toggle();
    $(`#answer-${assignmentId}-btn`).hide();
}

function showAnswers(assignment) {
    let answerTable = "#answer-" + assignment.id + "-table";
    $(answerTable).innerHTML = ''
    assignment.assignmentAnswers.forEach(answer => {
        if (answer.status === 'SUBMITTED') {
            getRequest(`/api/v1/users/${answer.ownerId}`).then(user => {
                $(answerTable).append(`
                <tr>
                    <td><span style="font-weight: bold" class="h3">${user.name}</span></td>
                    <td><a href="/api/v1/assignment-answers/${answer.id}/file">Answer</a></td>
                    <td><span id="grade-${answer.id}">${answer.grade}</span></td>
                    <td><button type="button" id="grade-${answer.id}-btn" class="btn btn-outline-success" 
                        data-toggle="modal" data-target="#grade-modal" onclick="assignmentAnswerId = ${answer.id}">Grade</button></td>
                    <td><button type="button" id="reject-${answer.id}-btn" class="btn btn-outline-success" 
                        onclick="reject(${answer.id})">Reject</button></td>
                </tr>
                `);
            });
        }
    });
}

function setExpiration(materialId, groupId) {
    let data = {
        expirationDate: $('#expiration-date-'+groupId+'-'+materialId).val() + 'T' + getCurrentTime(),
        groupId: groupId,
        materialId: materialId
    }
    postRequest(`/api/v1/expirations/`, data);
}

function updateExpiration(expirationId) {
    let data = $('#expiration-date-'+expirationId).val() + 'T' + getCurrentTime();
    patchRequest(`/api/v1/expirations/${expirationId}`, data);
}

function openMaterial(materialId) {
    let checkedIds = []
    $("#open-material input:checked").each(function () {
        checkedIds.push($(this).val());
    });
    patchRequest(`/api/v1/materials/open/${materialId}`, checkedIds);
}

function getGroupsWithClosedMaterial(materialId) {
    getRequest(`/api/v1/groups/closed/${materialId}`).then(groups => {
        for (let i = 0; i < groups.length; i++) {
            $('#open-material .form-check').append(`
                <input class="form-check-input" type="checkbox" value="${groups[i].id}" id="group-${groups[i].id}">
                <label class="form-check-label" for="group-${groups[i].id}">${groups[i].name}</label>
        `)
        }
    })
}

function getGroupsWithOpenedMaterial(materialId) {
    return getRequest(`/api/v1/groups/opened/${materialId}`);
}

function getExpirations(materialId) {
    return getRequest(`/api/v1/expirations/${materialId}`);
}

function getDayAfterToday(days) {
    let d = new Date();

    let month = d.getMonth() + 1;
    let day = d.getDate() + days;

    return d.getFullYear() + '-' +
        (month < 10 ? '0' : '') + month + '-' +
        (day < 10 ? '0' : '') + day;
}

function getDefaultExpirationDate() {
    return getDayAfterToday(1);
}

function getCurrentTime() {
    let hours = new Date().getHours();
    let minutes = new Date().getMinutes();
    return (hours < 10 ? '0' : '') + hours + ":" + (minutes < 10 ? '0' : '') + minutes;
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
    patchRequest(`/api/v1/assignment-answers/${id}/submit`).then($('#submit-' + id + '-btn').attr('disabled', true));
}

function reject(id) {
    patchRequest(`/api/v1/assignment-answers/${id}/reject`).then($('#reject-' + id + '-btn').attr('disabled', true))
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

