$(document).ready(setTimeout(() => {
    connect()
}, 1000))

var stompClient = null;

function connect() {

    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, onError);

}


function onConnected() {
    stompClient.subscribe('/topic/public');

    stompClient.send("/group/1/chat",
        {},
        JSON.stringify({content: "asdasd"})
    )
}

function onError(error) {

}
