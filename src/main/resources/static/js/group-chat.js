let stompClient = null;

const urlParams = new URLSearchParams(window.location.search);
let groupId = urlParams.get('id');
let chatId;

let page = 0;
let currentUser;

init();

function init() {
    connect();

    $.ajax(`/api/v1/groups/${groupId}`).then((group) => {
        chatId = group.chatRoom.id
    })
}


function connect() {
    var socket = new SockJS('/api/v1/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);

    getCurrentUser().then((user) => {currentUser = user});
}

function onConnected() {
    stompClient.subscribe(`/api/v1/ws/event/chat/${chatId}`, onMessageReceived);
    renderNextPage();
}

function onError(error) {}

function renderNextPage() {
    $.get(`api/v1/chat/${chatId}/${page}`).then(messages => {
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

    msg.id = "msg-" + message.id;
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

    stompClient.send(`/api/v1/ws/chat/${chatId}`,
        {},
        JSON.stringify({content: message, chatId: chatId})
    )

    chat.val("");
}

function onMessageReceived(payload) {
    let message = JSON.parse(payload.body);
    appendMessage(message);
}

function handleGroupButton() {
    window.location.replace(`/group?id=${groupId}`);
}