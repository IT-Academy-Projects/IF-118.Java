let currentUser;
getEvents();

function connect() {
    var socket = new SockJS('/api/v1/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, () => {});
}

function onConnected() {
    stompClient.subscribe(`/api/v1/ws/event/notification/${currentUser.id}`, onEventReceived);
}

function onEventReceived(payload) {
    let event = JSON.parse(payload.body);

    let type = event.type;
    if(type === "INVITE"){
        showInvitationEvent(event);
    } else if(type === "OPEN_LECTION"){
        showOpenLectionEvent(event);
    } else if(type === "SUBMIT_ANSWER"){
        showSubmitEvent(event);
    } else{
       showAnswerEvent(event);
    }
}

function getEvents() {
    let startPageNumber = 0;
    getCurrentUser().then((user) => {
        currentUser = user;
        $.get(`/api/v1/events/${user.id}?pageNo=${startPageNumber}`).then(notifications => {
            notifications.forEach(notification => {
                let type = notification.type;
                if (type === "INVITE") {
                    showInvitationEvent(notification);
                } else if(type === "OPEN_LECTION"){
                    showOpenLectionEvent(notification);
                } else if(type === "SUBMIT_ANSWER"){
                    showSubmitEvent(notification);
                } else{
                    showAnswerEvent(notification);
                }
            })
        });
        connect();
    });
}


function  showOpenLectionEvent(notification){
    let entId = notification.entityId;

    $.get(`/api/v1/materials/${entId}`).then(lection => {
        let newCell = createNewCell();
        var eventMsg = document.createTextNode(notification.creator.name + " open lection '" + lection.name + "'");
        newCell.appendChild(eventMsg);
    });
}

function  showSubmitEvent(notification){
    let entId = notification.entityId;

    $.get(`api/v1/assignments/${entId}`).then(assignment => {
       showSubmitEventMessage(notification.creator.name, assignment.name);
    });
}

function  showAnswerEvent(notification){
    let entId = notification.entityId;

    $.get(`/api/v1/assignment-answers/${entId}`).then(answer => {
        $.get(`api/v1/assignments/${answer.id}`).then(assignment => {
            if(notification.type === "GRADE_ANSWER") {
                showGradeEventMessage(notification.creator.name, assignment.name);
            } else{
                showRejectEventMessage(notification.creator.name, assignment.name);
            }
        });
    });
}

function showGradeEventMessage(teacherName, assignmentName){
    let newCell = createNewCell();

    var eventMsg = document.createTextNode(teacherName + " grade your answer to " + assignmentName + " assignment");
    newCell.appendChild(eventMsg);
}

function showSubmitEventMessage(studentName, assignmentName){
    let newCell = createNewCell();

    var eventMsg = document.createTextNode(studentName + " submit answer to " + assignmentName + " assignment");
    newCell.appendChild(eventMsg);
}

function showRejectEventMessage(teacherName, assignmentName){
    let newCell = createNewCell();
    var eventMsg = document.createTextNode(teacherName + " reject your answer to " + assignmentName + " assignment");
    newCell.appendChild(eventMsg);
}

function  showInvitationEvent(notification){
    let newCell = createNewCell();

    let entId = notification.entityId;

    $.get(`/api/v1/invitation/find/${entId}`).then(invitation => {
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

function createNewCell(){
    let tbodyRef = document.getElementById('eventTable').getElementsByTagName('tbody')[0];
    let newRow = tbodyRef.insertRow();
    let newCell = newRow.insertCell();

    return newCell;
}