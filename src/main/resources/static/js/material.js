let materialId;
let noMaterialText = '<span style="font-size:36px; color: grey; align-self: center; margin-top: 10px">Oops... Cannot find such material.</span>';

let canEdit;
let tempAssignmentId;
let currentUser;

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
                    <div class="material-name">${material.name}</div>
                    <div class="material-description">${material.description}</div>
                    <div class="material-download">Download: <a href="/api/v1/materials/${material.id}/file">${material.name}</a></div>
                </div>
            `);

            material.assignments.forEach(assignment => {

                let buttons = canEdit ? `<button class="btn btn-outline-info btn-sm" onclick="toggleAnswers(${assignment.id})">View answers</button>` :
                    `<button type="button" id="answer-${assignment.id}-btn" class="btn btn-outline-success btn-sm"
                        data-toggle="modal" data-target="#create-answer-modal" onclick="tempAssignmentId = ${assignment.id}">Answer</button>`;

                $('#assignments').append(`
                    <div class="assignment">
                        <div class="assignment-name">${assignment.name}</div>
                        <div class="assignment-description">${assignment.description}</div>
                         <div style="margin-top: 10px">
                              ${buttons}
                         </div>
                         <div id="answer-${assignment.id}-review" class="answer-body">
                                <section>
                                    <div class="container">
                                        <div class="row">
                                            <div class="col-sm-5 col-md-6 col-12 pb-4" id="answer-${assignment.id}-review-body">
                                            </div>
                                        </div>
                                    </div>
                                </section>
                         </div>
                         </br>
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
    postRequest(`/api/v1/assignments`, data).then(res => {
        $('#assignments').html('');
        $('#materials').html('');
        getMaterial(materialId);
    })
}

function checkAnswerFile() {
    if ($('#answer-file').val().length > 0) {
        $('#create-answer-btn').removeAttr('disabled');
    } else {
        $('#create-answer-btn').attr('disabled', true);
    }
}

function showMyAnswer(answer, assignmentId) {
    let answerReviewBody = "#answer-" + assignmentId+ "-review-body"
    $(answerReviewBody).append(`
        <div style="display: flex">
              <span style="font-size: 36px">∟</span>
              <div class="comment mt-4 text-justify" style="font-size: 22px">
                  <span style="font-weight: bold" class="h3">${currentUser.name}</span><br>
                  <a href="/api/v1/assignment-answers/${answer.id}/file">Answer</a>
               </div>
        </div>`);

    $(`#answer-${assignmentId}-review`).toggle();
    $(`#answer-${assignmentId}-btn`).hide();
}

function showAnswers(assignment) {
    let answerReviewBody = "#answer-" + assignment.id+ "-review-body";
    assignment.assignmentAnswers.forEach(answer => {
        getRequest(`/api/v1/users/${answer.ownerId}`).then(user => {
            $(answerReviewBody).append(`
        <div style="display: flex">
              <span style="font-size: 36px">∟</span>
              <div class="comment mt-4 text-justify" style="font-size: 22px">
                  <span style="font-weight: bold" class="h3">${user.name}</span><br>
                  <a href="/api/v1/assignment-answers/${answer.id}/file">Answer</a>
               </div>
        </div>`);
        });
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
    postFileRequest(`/api/v1/assignment-answers`, formData).then(res => {
        showMyAnswer(res, tempAssignmentId);
    })
}

function checkAssignment() {
    if ($('#assignment-name').val().length > 0 && $('#assignment-description').val().length > 0) {
        $('#create-assignment-btn').removeAttr('disabled');
    } else {
        $('#create-assignment-btn').attr('disabled', true);
    }
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

function postFileRequest(url, data) {
    return $.ajax({
        url: url,
        type: 'POST',
        data: data,
        processData: false,
        contentType: false,
        enctype: 'multipart/form-data'
    });
}
