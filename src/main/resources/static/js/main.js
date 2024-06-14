'use strict';

var chatPage = document.querySelector('#chat-page');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var connectButton = document.querySelector('#connectButton');
var connectPage = document.querySelector('#connect-page');

var topButtons = document.querySelector('#top-right-buttons');

var stompClient = null;
var username = null;

var chatGroupName = null;
var receiverUsername = null;


var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0',
    '#8e44ad', '#2ecc71', '#9b59b6', '#f1c40f',
    '#e74c3c', '#34495e', '#2c3e50', '#95a5a6'
];


function getUsername() {
    console.log("Retrieving username");
    fetch('/api/username')
        .then(response => response.text())
        .then(data => {
            username = data;
        });
}

// JavaScript (file.js)
function connect(event) {

    //username = 'Dhananjai';
    console.log("Inside connect function");
    if (username) {
        console.log("Inside connect function if condition");

        connectPage.classList.add('hidden');
        chatPage.classList.remove('hidden');
        topButtons.classList.remove('hidden');

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    
    event.preventDefault();
}


function onConnected() {
    // Subscribe to the Public Topic, whenever message is sent to this topic, onMessageReceived callback will be executed
    if(chatGroupName != null){  //group chat
        stompClient.subscribe('/topic/' + chatGroupName, onMessageReceived);
    }else{ //private chat
        var privateTopic = generateHashString(username, receiverUsername);
        stompClient.subscribe('/topic/' + "pvt_" + privateTopic, onMessageReceived);
    }

    // Tell your username to the server
    stompClient.send("/app/chat.addUser",
            {},
            JSON.stringify({sender: username, messageType: 'JOIN', chatGroupName: chatGroupName ? chatGroupName : receiverUsername})
        )

    connectingElement.classList.add('hidden');
    document.getElementById('loader').style.display = 'block';
    setTimeout(function() {
        document.getElementById('loader').style.display = 'none';
        getOldMessages();
    }, 20000);
}


function generateHashString(str1, str2) {
    var combinedStr = [str1, str2].sort().join('');
    var hash = 0;
    for (var i = 0; i < combinedStr.length; i++) {
        var char = combinedStr.charCodeAt(i);
        hash = ((hash << 5) - hash) + char;
        hash |= 0; // Convert to 32bit integer
    }

    var hashStr = Math.abs(hash).toString(36).toUpperCase();
    return hashStr.substring(0, 6);
}


function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    if(messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            messageType: 'CHAT',
            chatGroupName: chatGroupName ? chatGroupName : "pvt_" + generateHashString(username, receiverUsername)
        };
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    console.log(message);
    var messageElement = document.createElement('li');

    if(message.messageType === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' joined!';
    } else if (message.messageType === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left!';
    } else {
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

function addElement(message){
	var messageElement = document.createElement('li');

    if(message.messageType === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' joined!';
    } else if (message.messageType === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left!';
    } else {
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

function getOldMessages() {
    console.log("Retrieving old messages");
    fetch('/messages')
        .then(response => response.json())
        .then(messages => {
            messages.forEach(message => {
                console.log("Previous message: ", message);
                addElement(message);
            });
        })
        .catch(error => {
            console.error('There has been a problem with your fetch operation:', error);
        });
}


function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i) + i;
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}

//window.onload = function() {
//    connect();
//}

//connectButton.addEventListener('submit', connect, true)
//document.getElementById('connectButton').addEventListener('submit', connect, true);

document.addEventListener('DOMContentLoaded', function() {

    document.getElementById('loader').style.display = 'none';
    chatGroupName = chatgroup;
    receiverUsername = receiver;
    if(chatGroupName != null){
        document.getElementById('chat-header').textContent = chatGroupName
    }else{
        document.getElementById('chat-header').textContent = "Chatting with " + receiverUsername
    }
    getUsername();
    
    var connectButton = document.getElementById('connectButton');
    connectButton.addEventListener('submit', connect);
    messageForm.addEventListener('submit', sendMessage)
    
    var homeButton = document.getElementById('homeButton');
	var logoutButton = document.getElementById('logoutButton');
    homeButton.addEventListener('click', function() {
        window.location.href = '/showChatPage';
    });

    logoutButton.addEventListener('click', function() {
        fetch('/logout', { method: 'POST' })
            .then(() => window.location.href = '/showLoginForm');
    });
});
