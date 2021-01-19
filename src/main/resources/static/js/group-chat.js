let stompClient = null;

const urlParams = new URLSearchParams(window.location.search);
let groupId = urlParams.get('id');
let page = 0;
let currentUser;


$(document).ready(init())

function init() {
    setTimeout(() => { //TODO Try to remove
        connect();
    }, 500)
}


function connect() {
    var socket = new SockJS('/api/v1/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);

    getCurrentUser().then((user) => {currentUser = user});
}

function onConnected() {
    stompClient.subscribe(`/api/v1/ws/event/chat/group/${groupId}`, onMessageReceived);
    renderNextPage();
}

function onError(error) {}

function renderNextPage() {
    $.get(`api/v1/chat/group/${groupId}/${page}`).then(messages => {
        messages.forEach(message => {
            appendMessage(message);
        });
    })
    page++;
}

function getCurrentUser() {
    return $.get("/api/v1/users/me")
}

function appendMessage(message) {
    let msg = document.createElement("span");
    let date = new Date(message.createdAt);


    msg.id = "msg-" + message.id; //TODO
    msg.classList.add("msg");
    msg.innerHTML = "<div class='head'> " + message.user.name + " </div>";
    msg.innerHTML += "<p class='body'> " + message.content + " </p>";
    msg.innerHTML += "<div class='footer'> " + date.toDateString().substr(4) + " " + date.toTimeString().substr(0, 5) + " </div>";

    if(currentUser.id === message.user.id) {
        msg.classList.add("msg-own");
    }

    $("#chat-content").append(msg);
}

function sendMessage() {
    let chat = $("#chat-input");
    let message = chat.val()

    stompClient.send(`/api/v1/ws/chat/group/${groupId}`,
        {},
        JSON.stringify({content: message, groupId: groupId})
    )

    chat.val = "";
}

function onMessageReceived(payload) {
    let message = JSON.parse(payload.body);
    appendMessage(message);
}

function handleGroupButton() {
    window.location.replace(`/group?id=${groupId}`);
}