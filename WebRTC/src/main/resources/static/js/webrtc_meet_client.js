
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
    video: {
            width: 1980, // 최대 너비
            height: 1080, // 최대 높이
            frameRate: 60, // 최대 프레임
    } 
};


var cameraOff=false;
var audioOff=false;
var screenOff=false;

var btnAudio = document.getElementById('btnAudio');
var btnVideo = document.getElementById('btnVideo');
var btnScreen = document.getElementById('btnScreen');
  
btnAudio.addEventListener("click", handleMuteClick);
btnVideo.addEventListener("click", handleCameraClick);
btnScreen.addEventListener("click", handleScreenClick);

function handleMuteClick() {
  selfMediaStream
    .getAudioTracks() // 스트림에서 getAudioTrack() 가져오기
    .forEach((track) => (track.enabled = !track.enabled));
  if (audioOff) {
	btnAudio.innerText = "Audio Off";
    audioOff = false;
	}else{
	btnAudio.innerText = "Audio ON";
    audioOff = true;
	}
}
  
function handleCameraClick() {
  selfMediaStream
    .getVideoTracks() // 스트림에서 getVideoTracks() 가져오기
    .forEach((track) => (track.enabled = !track.enabled));
  if (cameraOff) {
    btnVideo.innerText = "Camera Off";
    cameraOff = false;
  } else {
    btnVideo.innerText = "Camera On";
    cameraOff = true;
  }
}

let shareVideo;
async function handleScreenClick() {
	if (screenOff) {
	selfMediaStream = localVideo;
	InMeetRoom.users[hidIsName].localvideo.srcObject = localVideo;
			for(let id of Object.keys(InMeetRoom.users)){
				if(InMeetRoom.users[id].CallerPeerConnection){
				await InMeetRoom.users[id].CallerPeerConnection.getSenders().forEach((sender)=>{ // 연결된 sender 로 보내기위한 반복문
						sender.replaceTrack(localVideo.getTracks()[1]);
			    	})
			    }
			}  
	shareVideo.getTracks().forEach((track) => {
    	track.stop();
    });
	btnScreen.innerText = " screen sharing OFF";
    screenOff = false;
	}
	else{
	navigator.mediaDevices.getDisplayMedia(mediaConstraints)
	.then(async function (stream) {
			shareVideo = stream;
			selfMediaStream = stream;
			
			InMeetRoom.users[hidIsName].localvideo.srcObject = stream;
			
			for(let id of Object.keys(InMeetRoom.users)){
				//if(id != hidIsName){
				if(InMeetRoom.users[id].CallerPeerConnection){
				await InMeetRoom.users[id].CallerPeerConnection.getSenders().forEach((sender)=>{ // 연결된 sender 로 보내기위한 반복문
					sender.replaceTrack(shareVideo.getTracks()[0]);
			
			    })
			    }
			}
		});
	btnScreen.innerText = " screen sharing ON";
    screenOff = true;
	}
}

selectCamera = document.getElementById('selectCamera');
selectAudioInput = document.getElementById('selectAudioInput');
selectAudioOutput = document.getElementById('selectAudioOutput');
async function settinginOutPut() {
  try {
    const devices = await navigator.mediaDevices.enumerateDevices();
    const cameras = devices.filter((device) => device.kind === "videoinput");
    const currentCamera = selfMediaStream.getVideoTracks()[0];
    
    const audeoinputs = devices.filter((device) => device.kind === "audioinput");
    const audeooutputs = devices.filter((device) => device.kind === "audiooutput");
    
    cameras.forEach((camera) => {
      const option = document.createElement("option");
      option.value = camera.deviceId;
      option.innerText = camera.label;
      if (currentCamera.label === camera.label) {
        option.selected = true;
      }
      selectCamera.appendChild(option);
    });
    
    audeoinputs.forEach((audioin) => {
      const option = document.createElement("option");
      option.value = audioin.deviceId;
      option.innerText = audioin.label;
      selectAudioInput.appendChild(option);
    });
    
    audeooutputs.forEach((audioout) => {
      const option = document.createElement("option");
      option.value = audioout.deviceId;
      option.innerText = audioout.label;
      selectAudioOutput.appendChild(option);
    });
    
    
  } catch (e) {
    console.log(e);
  }
}


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
                startICECandidate(message);
                break;

            case "join":
            	console.log('join >> message');
            	console.log(message);
            	
            	for(let id of message.data){
					if(id in InMeetRoom.users){
						
					}else{
						console.log('join create >>'+ id);
						createUser(id);
						createUserPeerConnection(id);
					}
				}
            	//var fromId = message.from;
				//createUser(fromId);
				//createUserPeerConnection(fromId);
            	
                //startPeerConnection(message);
                break;

            default:
                console.log('Wrong type message received from server');
        }
    };

    socket.onopen = async function() {
		
	document.getElementById('roomId').value=InMeetRoom.roomId;
		
	console.log('WebSocket  >> connect to Room: ' + InMeetRoom.roomName);
	createUser(hidIsName); // Add Document
	
	await navigator.mediaDevices
    .getUserMedia(mediaConstraints)
    .then(getUserMediaSuccess).catch(mediaConstraints);
    settinginOutPut();
    
        sendToSocket({
            from: hidIsName,
            userName: hidIsName,
            type: 'join',
            roomId: InMeetRoom.roomId
        });
      
    };
    
    /*
    navigator.mediaDevices.getDisplayMedia({
		audio: true,
		video: true
	}).then(function(stream){
		//success
	}).catch(function(e){
		//error;
	});
	*/

    socket.onclose = function(message) {
		console.log('Socket closed');
    };
    
    socket.onerror = function(message) {
		console.error(message);
    };
});
////////////
let selfMediaStream
let localVideo
function getUserMediaSuccess(mediaStream){
	  selfMediaStream=mediaStream;
	  localVideo = mediaStream;
	  InMeetRoom.users[hidIsName].localvideo.srcObject = selfMediaStream;
}
function getUserMediaError(error){
	console.log(' getUserMediaError >> error name :  '+ error.name);
	console.log(' getUserMediaError >> error message :  '+ error.message);
}


//////////
function startICECandidate(message) {
	var fromId = message.from;
    let candidate = new RTCIceCandidate(message.candidate);
    console.log("Adding received ICE candidate: ");
    //console.log("Adding received ICE candidate: " + JSON.stringify(candidate));
    InMeetRoom.users[fromId].CallerPeerConnection.addIceCandidate(candidate).catch(function(error){
		console.log(' addIceCandidate >> error name :  '+ error.name);
		console.log(' addIceCandidate >> error message :  '+ error.message);
	});
}

////////// Other
async function startOffer(message) {
		if( message.from in InMeetRoom.users){
			console.log('startOffer already create >>'+ message.from);
			return;
		}else{
			await createUser(message.from);
			await createUserPeerConnection(message.from);
			console.log('startOffer create >>'+ message.from);
		}
	var fromId = message.from;
	let desc = new RTCSessionDescription(message.sdp);
	 if (desc != null && message.sdp != null) {
		InMeetRoom.users[fromId].CallerPeerConnection.setRemoteDescription(desc)
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
            from: hidIsName,
            to:fromId,
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
	var fromId = message.from;
    InMeetRoom.users[fromId].CallerPeerConnection
    .setRemoteDescription(message.sdp)	    
    .catch(function(error){
			console.log(' startAnswer >> error name :  '+ error.name);
			console.log(' startAnswer >> error message :  '+ error.message);
		});
}


async function createUser(isName){
	CreateUserVideoTag('div'+isName, 'video'+isName); // Add Document
	InMeetRoom.users[isName]={};
	InMeetRoom.users[isName].localvideo = document.getElementById('video'+isName);
}

async function createUserPeerConnection(isName){
	console.log(isName);
	console.log(selfMediaStream);
	InMeetRoom.users[isName].CallerPeerConnection = new RTCPeerConnection(peerConnectionConfig);
	InMeetRoom.users[isName].CallerPeerConnection.addStream(selfMediaStream); //input mediaStream;
	InMeetRoom.users[isName].CallerPeerConnection.onicecandidate = function (args) {
																		    if (args.candidate) {
																				console.log("Sending ice packet to other peer");
																		        sendToSocket({
																		            from: hidIsName,
																		            to: isName,
																		            type: 'ice',
																		            candidate: args.candidate
																		        });
																		       console.log('onicecandidate >> Sent ICE Candiate');
																		    }
																		};
	 
    InMeetRoom.users[isName].CallerPeerConnection.ontrack = function (args) {
																    console.log('ontrack >> remoteVide set Stream');
																    InMeetRoom.users[isName].localvideo.srcObject = args.streams[0]
																    /*
																        Stream
																	    .getTrack()
																	    .forEach(track => PeerConnection.addTrack(track, myStream)
																    */
															};
															
	InMeetRoom.users[isName].CallerPeerConnection.onnegotiationneeded = function (){
																			InMeetRoom.users[isName].CallerPeerConnection.createOffer()
																			.then(function(offer){
																			    return InMeetRoom.users[isName].CallerPeerConnection.setLocalDescription(offer);
																			})
																			.then(function(){
																					console.log("Sending offer packet to other peer");
																				sendToSocket({
																		            from: hidIsName,
																		            to:isName,
																		            type: 'offer',
																		            sdp: InMeetRoom.users[isName].CallerPeerConnection.localDescription
																		        });
																		    })
																		    .catch(function(error){
																				console.log(' setOnNegotiationNeeded >> error name :  '+ error.name);
																				console.log(' setOnNegotiationNeeded >> error message :  '+ error.message);
																			});
																		};
	InMeetRoom.users[isName].CallerPeerConnection.oniceconnectionstatechange = function (){
																				let status = InMeetRoom.users[isName].CallerPeerConnection.iceConnectionState;
																			
																			    if(status === "connected"){
																			        //log("status : "+status)
																			        //$("#remote_video").show();
																			    }else if(status === "disconnected"){
																			        document.querySelector('#div'+isName).remove();

																			    }
																		};		
}

/////////// HTML
function CreateUserVideoTag(divName, videoName){
	var div = document.createElement('div');
	div.setAttribute('class', 'col-auto mb-3');	
	div.setAttribute('id', divName);	
	
	var video =  document.createElement('video');
	video.setAttribute('id', videoName);	
	div.setAttribute('class', 'h-25');	
	video.setAttribute('autoplay', 'autoplay');
	div.append(video);
	var list = document.getElementById('MeetVideos');
	list.appendChild(div);
}





