function guid() {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
            let r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
        });
}

function sendToSocket(msg) {
    let msgJSON = JSON.stringify(msg);
    socket.send(msgJSON);
}


var hidIsName = document.getElementById('hidIsName').value;
var hidIsAdmin = document.getElementById('hidIsAdmin').value;

let InMeetRoom ={
	roomId : document.getElementById('hidRoomId').value,
	roomName : document.getElementById('hidRoomName').value,
	roomPassword : document.getElementById('hidRoomPassword').value,
	users :{
	}
}

const remoteVideo = document.getElementById('remote_video');

let CallerPeerConnection;
// WebRTC STUN servers
const peerConnectionConfig = {
    'iceServers': [
        {'urls': 'stun:stun.stunprotocol.org:3478'},
        {'urls': 'stun:stun.l.google.com:19302'},
    ]
};

// WebRTC media
const mediaConstraints = {
    audio: true,
    video: true
};



const socket = new WebSocket("wss://"+window.location.host+"/signal");

$(function(){
		
	console.log(hidRoomId);
	console.log(hidRoomPassword);
	console.log(hidRoomName);
	console.log(hidIsName);
	console.log(hidIsAdmin);
	
     socket.onmessage = function(msg) {
        let message = JSON.parse(msg.data);
        switch (message.type) {
            case "text":
                console.log('Text message from ' + message.from + ' received: ' + message.data);
                break;

            case "offer":
            	console.log('offer >> message');
                startOffer(message);
                break;

            case "answer":
            	console.log('answer >> message');
                startAnswer(message);
                break;

            case "ice":
            	console.log('ice >> message');
                startICECandidate(message)
                break;

            case "join":
            	console.log('join >> message');
                startPeerConnection(message);
                break;

            default:
                console.log('Wrong type message received from server');
        }
    };

    socket.onopen = function() {
		
	console.log('WebSocket  >> connect to Room: ' + InMeetRoom.roomName);
	createUser(hidIsName); // Add Document
	
        sendToSocket({
            from: hidIsName,
            type: 'join',
            data: InMeetRoom.roomId
        });
    };

    socket.onclose = function(message) {
		console.log('Socket closed');
    };
    
    socket.onerror = function(message) {
		console.error(message);
    };
});


function startPeerConnection(message){
	InMeetRoom.users[hidIsName].CallerPeerConnection = new RTCPeerConnection(peerConnectionConfig);
	// set order Candidate -> stream
    InMeetRoom.users[hidIsName].CallerPeerConnection.onicecandidate = setOnIceCandiate;
    // sent ice
    
    //InMeetRoom.users[hidIsName].CallerPeerConnection.ontrack = setOnTrack;
    // set remote
     
    navigator.mediaDevices
    .getUserMedia(mediaConstraints)
    .then(getUserMediaSuccess).catch(getUserMediaError);
    // set local, stream
    console.log("getUserMediaSuccess");
    
	InMeetRoom.users[hidIsName].CallerPeerConnection.onnegotiationneeded = setOnNegotiationNeeded;
    // sent offer(sdp), set localDescription
}

////////////
function setOnIceCandiate(args) {
    if (args.candidate) {
		console.log("Sending ice packet to other peer");
        sendToSocket({
            from: hidIsName,
            type: 'ice',
            candidate: args.candidate
        });
       console.log('onicecandidate >> Sent ICE Candiate');
    }
}
function setOnTrack(args) {
    console.log('ontrack >> remoteVide set Stream');
    console.log(InMeetRoom.users[hidIsName]);
    //remoteVideo.srcObject = args.streams[0];
    remoteVideo.srcObject = args.streams[0]
}

////////////
function getUserMediaSuccess(mediaStream){
	  InMeetRoom.users[hidIsName].localvideo.srcObject = mediaStream;
      InMeetRoom.users[hidIsName].CallerPeerConnection.addStream(mediaStream); //input mediaStream;
      console.log("getUserMediaSuccess srcObject");
}
function getUserMediaError(error){
	console.log(' getUserMediaError >> error name :  '+ error.name);
	console.log(' getUserMediaError >> error message :  '+ error.message);
}

///////////
function setOnNegotiationNeeded(){
	InMeetRoom.users[hidIsName].CallerPeerConnection.createOffer()
	.then(function(offer){
	    return InMeetRoom.users[hidIsName].CallerPeerConnection.setLocalDescription(offer);
	})
	.then(function(){
			console.log("Sending offer packet to other peer");
		sendToSocket({
            from: hidIsName,
            type: 'offer',
            sdp: InMeetRoom.users[hidIsName].CallerPeerConnection.localDescription
        });
    })
    .catch(function(error){
		console.log(' setOnNegotiationNeeded >> error name :  '+ error.name);
		console.log(' setOnNegotiationNeeded >> error message :  '+ error.message);
	});
}

//////////
function startICECandidate(message) {
    let candidate = new RTCIceCandidate(message.candidate);
    console.log("Adding received ICE candidate: ");
    //console.log("Adding received ICE candidate: " + JSON.stringify(candidate));
    InMeetRoom.users[hidIsName].CallerPeerConnection.addIceCandidate(candidate).catch(function(error){
		console.log(' addIceCandidate >> error name :  '+ error.name);
		console.log(' addIceCandidate >> error message :  '+ error.message);
	});
}

////////// Other
function startOffer(message) {
	var fromId = message.from;
	createUser(fromId);
	createUserPeerConnection(fromId);
	let desc = new RTCSessionDescription(message.sdp);
	 if (desc != null && message.sdp != null) {
		InMeetRoom.users[fromId].CallerPeerConnection.setRemoteDescription(desc)
		.then(function(){
			return navigator.mediaDevices.getUserMedia(mediaConstraints);
		})
		.then(function(stream){
			console.log("offer >> CallerPeerConnection.addStream");
			    try {
                    InMeetRoom.users[fromId].localvideo.srcObject = stream;
                } catch (error) {
					console.log('offer >> error');
                }
                 InMeetRoom.users[fromId].CallerPeerConnection.addStream(stream);
		})
		.then(function () {
			console.log("offer >> createAnswer");
			return InMeetRoom.users[fromId].CallerPeerConnection.createAnswer();
		})
		.then(function (answer) {
			console.log("offer >> CallerPeerConnection.setLocalDescription");
			 return InMeetRoom.users[fromId].CallerPeerConnection.setLocalDescription(answer);
		})
		.then(function () {
			console.log("Sending answer packet to other peer");
			sendToSocket({
            from: fromId,
            type: 'answer',
            sdp: InMeetRoom.users[fromId].CallerPeerConnection.localDescription
        	});
		})
	    .catch(function(error){
			console.log(' startOffer >> error name :  '+ error.name);
			console.log(' startOffer >> error message :  '+ error.message);
		});
	}
}

//////// Self
function startAnswer(message) {
	
    InMeetRoom.users[hidIsName].CallerPeerConnection
    .setRemoteDescription(message.sdp)	    
    .catch(function(error){
			console.log(' startAnswer >> error name :  '+ error.name);
			console.log(' startAnswer >> error message :  '+ error.message);
		});
}


function createUser(isName){
	CreateUserVideoTag('div'+isName, 'video'+isName); // Add Document
	InMeetRoom.users[isName]={};
	InMeetRoom.users[isName].localvideo = document.getElementById('video'+isName);
}

function createUserPeerConnection(isName){
	InMeetRoom.users[isName].CallerPeerConnection = new RTCPeerConnection(peerConnectionConfig);
	// set order Candidate -> stream
	
    InMeetRoom.users[isName].CallerPeerConnection.ontrack = function (args) {
																    console.log('ontrack >> remoteVide set Stream');
																    //remoteVideo.srcObject = args.streams[0];
																    InMeetRoom.users[isName].localvideo.srcObject = args.streams[0]
																};
    // set remote
}

/////////// HTML
function CreateUserVideoTag(divName, videoName){
	var div = document.createElement('div');
	div.setAttribute('class', 'col-lg-6 mb-3');	
	div.setAttribute('id', divName);	
	
	var video =  document.createElement('video');
	video.setAttribute('id', videoName);	
	video.setAttribute('autoplay', 'autoplay');
	div.append(video);
	var list = document.getElementById('MeetVideos');
	list.appendChild(div);
}





