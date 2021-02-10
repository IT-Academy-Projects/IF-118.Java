refreshEvents();

function connect() {
    var socket = new SockJS('/api/v1/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, () => {});

}

function onConnected() {
    stompClient.subscribe(`/api/v1/ws/event/notification/${currentUser.id}`);
}

function refreshEvents() {
    let startPageNumber = 0;
    getCurrentUser().then((user) => {
        currentUser = user;
        $.get(`/api/v1/events/${user.id}?pageNo=${startPageNumber}`).then(notifications => {
            notifications.forEach(notification => {
                let type = notification.type;
                if(type == "INVITE"){
                    showInvitationEvent(notification);
                } else{
                    showAnswerEvent(notification);
                }
            })
        });
        connect();
    });
}

function  showAnswerEvent(notification){
    let subId = notification.subjectId;

    $.get(`/api/v1/assignment-answers/${subId}`).then(answer => {
        $.get(`api/v1/assignments/${answer.id}`).then(assignment => {
            if(notification.type = "GRADE_ANSWER") {
                showGradeEventMessage(notification.creator.name, assignment.name);
            } else if(notification.type = "SUBMIT_ANSWER"){
                showSubmitEventMessage(notification.creator.name, assignment.name);
            } else{
                showRejectEventMessage(notification.creator.name, assignment.name);
            }
        });
    });
}

function showGradeEventMessage(teacherName, assignmentName){
    $('#invitations').append(`
       <p class="card-text">${teacherName} grade your answer to ${assignmentName} assignment</p>
  `);
}

function showSubmitEventMessage(studentName, assignmentName){
    $('#invitations').append(`
       <p class="card-text">${studentName} submit answer to ${assignmentName} assignment</p>
  `);
}

function showRejectEventMessage(teacherName, assignmentName){
    $('#invitations').append(`
            <p class="card-text">${teacherName} reject your answer to ${assignmentName} assignment</p>
  `);
}

function  showInvitationEvent(notification){
    let tbodyRef = document.getElementById('eventTable').getElementsByTagName('tbody')[0];
    let newRow = tbodyRef.insertRow();
    let newCell = newRow.insertCell();

    let subId = notification.subjectId;

    $.get(`/api/v1/invitation/find/${subId}`).then(invitation => {
        if( invitation.courseOrGroup == 'course'){
            $.get(`api/v1/courses/${invitation.courseOrGroupId}`).then(course => {
                var eventMsg = document.createTextNode(notification.creator.name + " invite you to " + course.name);
                newCell.appendChild(eventMsg);
            });
        } else{
            $.get(`api/v1/groups/${invitation.courseOrGroupId}`).then(group => {
                var eventMsg = document.createTextNode(notification.creator.name + " invite you to " + group.name);
                newCell.appendChild(eventMsg);
            });
        }
    });
}

function getCurrentUser() {
    return $.get("/api/v1/users/me")
}

