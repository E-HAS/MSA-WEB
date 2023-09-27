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

const localRoom = 1;
const localUserName = guid();

const localVideo = document.getElementById('local_video');
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

    // add an event listener to get to know when a connection is open
    socket.onopen = function() {
	console.log('WebSocket  >> connect to Room: #' + localRoom);
        sendToSocket({
            from: localUserName,
            type: 'join',
            data: localRoom
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
	CallerPeerConnection = new RTCPeerConnection(peerConnectionConfig);
	// set order Candidate -> stream
    CallerPeerConnection.onicecandidate = setOnIceCandiate;
    // sent ice
    
    CallerPeerConnection.ontrack = setOnTrack;
    // set remote
     
    navigator.mediaDevices
    .getUserMedia(mediaConstraints)
    .then(getUserMediaSuccess).catch(getUserMediaError);
    // set local, stream
    
	CallerPeerConnection.onnegotiationneeded = setOnNegotiationNeeded;
    // sent offer(sdp), set localDescription
}

////////////
function setOnIceCandiate(args) {
    if (args.candidate) {
		console.log("Sending ice packet to other peer");
        sendToSocket({
            from: localUserName,
            type: 'ice',
            candidate: args.candidate
        });
       console.log('onicecandidate >> Sent ICE Candiate');
    }
}
function setOnTrack(args) {
    console.log('ontrack >> remoteVide set Stream');
    //remoteVideo.srcObject = args.streams[0];
    remoteVideo.srcObject = args.streams[0];
}

////////////
function getUserMediaSuccess(mediaStream){
	  localVideo.srcObject = mediaStream;
      CallerPeerConnection.addStream(mediaStream); //input mediaStream;
}
function getUserMediaError(error){
	console.log(' getUserMediaError >> error name :  '+ error.name);
	console.log(' getUserMediaError >> error message :  '+ error.message);
}

///////////
function setOnNegotiationNeeded(){
	CallerPeerConnection.createOffer()
	.then(function(offer){
	    return CallerPeerConnection.setLocalDescription(offer);
	})
	.then(function(){
			console.log("Sending offer packet to other peer");
		sendToSocket({
            from: localUserName,
            type: 'offer',
            sdp: CallerPeerConnection.localDescription
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
    console.log("Adding received ICE candidate: " + JSON.stringify(candidate));
    CallerPeerConnection.addIceCandidate(candidate).catch(function(error){
		console.log(' addIceCandidate >> error name :  '+ error.name);
		console.log(' addIceCandidate >> error message :  '+ error.message);
	});
}

//////////
function startOffer(message) {
	let desc = new RTCSessionDescription(message.sdp);
	 if (desc != null && message.sdp != null) {
		CallerPeerConnection.setRemoteDescription(desc)
		.then(function(){
			return navigator.mediaDevices.getUserMedia(mediaConstraints);
		})
		.then(function(stream){
			console.log("offer >> CallerPeerConnection.addStream");
			    try {
                    localVideo.srcObject = stream;
                } catch (error) {
                    localVideo.src = window.URL.createObjectURL(stream);
                }
                 CallerPeerConnection.addStream(stream);
		})
		.then(function () {
			console.log("offer >> createAnswer");
			return CallerPeerConnection.createAnswer();
		})
		.then(function (answer) {
			console.log("offer >> CallerPeerConnection.setLocalDescription");
			 return CallerPeerConnection.setLocalDescription(answer);
		})
		.then(function () {
			console.log("Sending answer packet to other peer");
			sendToSocket({
            from: localUserName,
            type: 'answer',
            sdp: CallerPeerConnection.localDescription
        	});
		})
	    .catch(function(error){
			console.log(' startOffer >> error name :  '+ error.name);
			console.log(' startOffer >> error message :  '+ error.message);
		});
	}
}

////////
function startAnswer(message) {
    CallerPeerConnection
    .setRemoteDescription(message.sdp)	    
    .catch(function(error){
			console.log(' startAnswer >> error name :  '+ error.name);
			console.log(' startAnswer >> error message :  '+ error.message);
		});
}
