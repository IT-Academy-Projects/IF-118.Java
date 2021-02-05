function refreshNotifications() {
    getInvitations();
    getGrades();
    getSubmittedAnswer();
}

 // INVITATIONS
function getInvitations() {
    getRequest(`/api/v1/invitation`).then(invitations => {
        invitations.forEach(invitation => {
            let invId = invitation.id;
            let courseOrGroup = invitation.courseOrGroup;
            let courseOrGroupTeacher;
            let approveBtn = '';
            let deleteBtn = '';

            approveBtn = `<div class="approve-invitation btn">
                            <button type="button" class="btn btn-outline-success show" onclick="approveInvitation(${invId})">Approve</button>
                          </div>`;
            deleteBtn = `<div class="delete-invitation btn">
                            <button type="button" class="btn btn-outline-danger" onclick="deleteInvitation(${invId})">Delete</button>
                         </div>`;

            if( courseOrGroup === 'course'){
                getRequest(`api/v1/courses/${invitation.courseOrGroupId}`).then(course => {
                    let courseName = course.name;
                    courseOrGroupTeacher = course.ownerId;

                    getRequest(`/api/v1/users/${courseOrGroupTeacher}`).then(user => {
                        courseOrGroupTeacher = user.name;
                        showInvitation('Course', courseName, courseOrGroupTeacher, approveBtn, deleteBtn);
                    });
                });
            } else{
                getRequest(`api/v1/groups/${invitation.courseOrGroupId}`).then(group => {
                    let groupName = group.name;
                    courseOrGroupTeacher = group.ownerId;

                    getRequest(`/api/v1/users/${courseOrGroupTeacher}`).then(user => {
                        courseOrGroupTeacher = user.name;
                        showInvitation('Group', groupName, courseOrGroupTeacher, approveBtn, deleteBtn);
                    });
                });
            }
        });
    });
}

function showInvitation(courseOrGroup, courseOrGroupName, teacher, approveBtn, deleteBtn){
    $('#notifications').append(`
        <div class="card text-white bg-dark mb-3" style=" margin-right: 15px;" >
            <div class="card-header">Invitation to</div>
            <div class="card-body">
                <h5 class="card-title">${courseOrGroup} ${courseOrGroupName}</h5>
                <p class="card-text">Teacher: ${teacher}</p>
                <div class="btn" style="display: flex; justify-content: center;">
                    ${approveBtn}
                    ${deleteBtn}
                </div>
            </div>
        </div>
    `);
}

function deleteInvitation(id) {
    deleteRequest(`/api/v1/invitation/${id}`).then(res => {
        $('#notifications').html('');
        refreshNotifications();
    })
}
function approveInvitation(id) {
    patchRequest(`/api/v1/invitation/approve/${id}`).then(res => {
        $('#notifications').html('');
        refreshNotifications();
    })
}

 // GRADING
function getGrades() {
    getRequest(`/api/v1/assignment-answers`).then(answers => {
        answers.forEach(answer => {
            let answerID = answer.id;
            let grade = answer.grade;
            let assignmentID = answer.assignmentId;

            if(answer.grade != 0 && !answer.isStudentSawGrade) {
                getRequest(`/api/v1/assignments/${assignmentID}`).then(assignment => {
                    let assignmentName = assignment.name;
                    let materialID = assignment.materialId;
                    showGradeNotification(grade, assignmentID, assignmentName, answerID, materialID);
                });
            }
        });
    });
}

function showGradeNotification(grade, assignmentID, assignmentName, answerID, materialID){
    let oKBtn = '';
    let goToAssignmentsBtn = '';

    oKBtn = `<div class="btn">
                 <button type="button" class="btn btn-outline-success show" onclick="markAsGradeIsViewed(${answerID});">OK</button>
             </div>`;
    goToAssignmentsBtn = `<div class="btn">
                        <button type="button" class="btn btn-outline-warning show" 
                        onclick="goToAssignments(${materialID}, ${answerID});">Go to topics</button>
                     </div>`;

    $('#notifications').append(`
        <div class="card text-white bg-dark mb-3" style=" margin-right: 15px;" >
            <div class="card-header">Grading</div>
            <div class="card-body">
                <h5 class="card-title">Your answer to topic '${assignmentName}' graded</h5>
                <p class="card-text">Grade: ${grade}</p>
                <div class="btn" style="display: flex; justify-content: center;">
                    ${oKBtn}
                    ${goToAssignmentsBtn}
                </div>
            </div>
        </div>
    `);
}

function markAsGradeIsViewed(assignmentAnswerID){
    patchRequest(`/api/v1/assignment-answers/${assignmentAnswerID}/grade-review`);
    location.reload();
}

function goToAssignments(materialID, assignmentAnswerID ){
    markAsGradeIsViewed(assignmentAnswerID);
    location.replace("/material?id=" + materialID);
}

 // SUBMITTING
function getSubmittedAnswer() {
    getRequest(`/api/v1/assignments`).then(assignments => {
        assignments.forEach(assignment => {
            let assignmentID = assignment.id;
            let materialID = assignment.materialId;
            let assignmentName = assignment.name;
            let assignmentAnswers = assignment.assignmentAnswers;

            assignmentAnswers.forEach(answer=> {
                let assignmentAnswerID = answer.id;
                let studentID = answer.ownerId;
                getRequest(`api/v1/users/${studentID}`).then(student =>{
                    let studentName = student.name;
                    if(answer.isSubmitted && !answer.isReviewedByTeacher && answer.grade == 0){
                        showSubmitNotification(assignmentID, assignmentName, assignmentAnswerID, studentName, materialID);
                    }
                });
            });
        });
    });
}

function showSubmitNotification(assignmentID, assignmentName,assignmentAnswerID, student, materialID){
    let goToAssignmentBtn = '';
    let okBtn = '';

    okBtn = `<div class="btn">
                            <button type="button" class="btn btn-outline-success show" onclick="markAsTeacherViewedAnswer(${assignmentAnswerID});">OK</button>
                          </div>`;
    goToAssignmentBtn = `<div class="btn">
                            <button type="button" class="btn btn-outline-warning show" onclick="goToAssignment(${materialID}, ${assignmentAnswerID});">Go to topics</button>
                          </div>`;

    $('#notifications').append(`
        <div class="card text-white bg-dark mb-3" style=" margin-right: 15px;" >
            <div class="card-header">Submitting</div>
            <div class="card-body">
                <h5 class="card-title">New submitted answer to topic '${assignmentName}'</h5>
                <p class="card-text">from student: ${student}</p>
                <div class="btn" style="display: flex; justify-content: center;">
                    ${okBtn}
                    ${goToAssignmentBtn}
                </div>
            </div>
        </div>
    `);
}

function markAsTeacherViewedAnswer(assignmentAnswerID){
    patchRequest(`/api/v1/assignment-answers/${assignmentAnswerID}/review`);
    location.reload();
}

function goToAssignment(materialID, assignmentAnswerID){
    markAsTeacherViewedAnswer(assignmentAnswerID);
    location.replace("/material?id=" + materialID);
}


function getRequest(url) {
    return $.get(url);
}

function patchRequest(url, data) {
    return $.ajax({
        url: url,
        type: 'PATCH',
        data: JSON.stringify(data),
        contentType: 'application/json; charset=utf-8'
    })
}

function deleteRequest(url) {
    return $.ajax({
        url: url,
        type: 'DELETE'
    });
}