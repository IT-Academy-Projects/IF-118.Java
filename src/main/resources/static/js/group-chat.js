$(document).ready(setTimeout(() => {
    connect()
}, 1000))

var stompClient = null;

function connect() {
    const urlParams = new URLSearchParams(window.location.search);
    groupId = urlParams.get('id');

    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, onError);

}

function onConnected() {

    stompClient.subscribe(`/group/${groupId}/chat/sub`, onMessageReceived);
    $.get("chat/{pageNo}")
}

function onError(error) {

}

function sendMessage(event) {
    let message = $("#chat-input").val()

    stompClient.send(`/group/${groupId}/chat`,
        {},
        JSON.stringify({content: message, groupId: groupId})
    )
}

function onMessageReceived(payload) {
    let message = JSON.parse(payload.body);
    let content = message.content

    console.log(message);

    $("#message").text(content);
}