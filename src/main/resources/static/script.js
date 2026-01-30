
// 1. create connection using SockJS
// Use relative URL so it works on any host/port where the page is served



var stompClient = null ;
var client = null ;

function createConnection() {
  console.log("Button clicked");

   client  = document.getElementById('sender').value ;

  if(stompClient && stompClient.connected){
  console.log('already connected ') ;
  return ;
  }

  var socket = new SockJS("http://localhost:8080/myChat");
   stompClient  = Stomp.over(socket) ;
   stompClient.debug = function(str) {
     console.log("STOMP >>", str);
   };

  stompClient.connect(
    {
      login: 'prasad2201',
      passcode: 'kkbk@980',
      'message-id': 'id@909',
      'client-name': client,
      'client-chat-id': '123@UHJ'
    },
    callBack
  );
}

function callBack(frame){
  stompClient.subscribe('/topic/public' , messageReceived) ;
  var joinRequest = {
    'sender' : client ,
    'type' :  'JOIN'
   }
  stompClient.send( '/app/chat.join' , {} , JSON.stringify(joinRequest) );
}

function makeDisconnect(){
   stompClient.disconnect( disconnectCallback ) ;
}
function disconnectCallback(frame){
alert(`${client} disconnected`) ;
console.log('FRAME ' , frame) ;
}
function sendMessage(){
var msg = document.getElementById('chatMessage').value ;
if (!msg || !msg.trim()) {
  return; // Don't send empty messages
}
var payloadDetails = {
        'sender' : client ,
        'chatContent' : msg ,
        'type' : 'CHAT'
       }
stompClient.send('/app/chat.send' ,
     {},
     JSON.stringify(payloadDetails)

 );
// Clear input after sending
document.getElementById('chatMessage').value = '';
}

// Allow sending message with Enter key
document.addEventListener('DOMContentLoaded', function() {
  var chatInput = document.getElementById('chatMessage');
  if (chatInput) {
    chatInput.addEventListener('keypress', function(e) {
      if (e.key === 'Enter') {
        sendMessage();
      }
    });
  }
});
function messageReceived(message) {
  if (!message.body) return;

  console.log("RAW MESSAGE:", message);

const chat = JSON.parse(message.body);
const chatBox = document.getElementById("chatBox");
const li = document.createElement("li");
li.className = "message-item";

if(chat.type=="JOIN"){
  // Show notification
  showTopNotification(`${chat.sender} joined the chat`);
  // Also add to chat box as system message
  li.className = "message-item system";
  li.innerHTML = '<span class="message-content">' + chat.sender + ' joined the chat</span>';
  chatBox.appendChild(li);
  chatBox.scrollTop = chatBox.scrollHeight;
  return;
}
if(chat.type=="LEAVE"){
  // Show notification
  showTopNotification(`${chat.sender} left the chat`);
  // Also add to chat box as system message
  li.className = "message-item system";
  li.innerHTML = '<span class="message-content">' + chat.sender + ' left the chat</span>';
  chatBox.appendChild(li);
  chatBox.scrollTop = chatBox.scrollHeight;
  return;
}

// Regular chat messages - determine if sender or receiver
if(chat.sender === client) {
  // This is a message sent by current user (right side)
  li.className = "message-item sender";
  li.innerHTML = '<div class="message-sender">You</div><div class="message-content">' + chat.chatContent + '</div>';
} else {
  // This is a message received from others (left side)
  li.className = "message-item receiver";
  li.innerHTML = '<div class="message-sender">' + chat.sender + '</div><div class="message-content">' + chat.chatContent + '</div>';
}

chatBox.appendChild(li);
// Auto scroll to bottom
chatBox.scrollTop = chatBox.scrollHeight;
}

function showTopNotification(text) {
  const topDiv = document.getElementById("topNotification");

  topDiv.innerText = text;
  topDiv.style.display = "block";
  topDiv.classList.add("show");
  
  // Auto hide after 3 seconds
  setTimeout(function() {
    topDiv.style.display = "none";
    topDiv.classList.remove("show");
  }, 3000);
}








