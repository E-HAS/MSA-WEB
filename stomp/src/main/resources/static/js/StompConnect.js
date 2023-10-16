window.onload = function () {
	connect();
}

const randId = Math.random().toString(16).substr(2, 8);


let InMeetRoom ={
	users :{
	}
}

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


let RoomId = 'abcdef';
let RommName = 'roomName';
let UserName =  randId;

let stompClient;

let stompSendUrl = '/app/Stream/Send/'+RoomId;
 function connect() {
	 
        var socket = new SockJS("https://"+window.location.host+"/Stomp");

        stompClient = Stomp.over(socket);

        // connect(header,연결 성공시 콜백,에러발생시 콜백)
        stompClient.connect({}, async function () {
	
	            createUser(UserName);
                await navigator.mediaDevices
			    .getUserMedia(mediaConstraints)
			    .then(getUserMediaSuccess).catch(mediaConstraints);
			    
                // subscribe(subscribe url, function)
                stompClient.subscribe('/topic/Stream/Receive/'+RoomId+'/Join/'+UserName, function ( receive ) {
					msg = JSON.parse(receive.body)
					
					for(let joinId of msg.data){
						if(joinId in InMeetRoom.users){
							
						}else{
							console.log('join create >>'+ joinId);
							createUser(joinId);
							createUserPeerConnection(joinId);
						}
					}
					
                });
                
                stompClient.subscribe('/queue/Stream/Receive/'+RoomId+'/Offer/'+UserName, async function ( receive ) {
					msg = JSON.parse(receive.body)
					startOffer(msg);
                });
                
                stompClient.subscribe('/queue/Stream/Receive/'+RoomId+'/Answer/'+UserName, async function ( receive ) {
					msg = JSON.parse(receive.body)
					startAnswer(msg);
                });
                
                stompClient.subscribe('/queue/Stream/Receive/'+RoomId+'/Ice/'+UserName, function ( receive ) {
					msg = JSON.parse(receive.body)
					startICECandidate(msg);
                });
                
                stompClient.subscribe('/topic/Stream/Receive/'+RoomId+'/Msg', function ( receive ) {
					msg = JSON.parse(receive.body)
                });
			    
                var data ={
					roomId : RoomId,
					from : UserName,
					to : UserName,
					type : 0,
					Data : 'Join'
				};
                sendServer(stompSendUrl+'/Join',{},data);
            },
            function(e){
                alert('stome connect error : ' + e);
            }
        );
    }
    
    function sendServer(url, header, data){
        // send(prefix + MessageMapping url, 헤더, 페이로드)
        stompClient.send(url, header, JSON.stringify(data));
    }
    
    
let selfMediaStream
let localVideo
function getUserMediaSuccess(mediaStream){
	  selfMediaStream=mediaStream;
	  localVideo = mediaStream;
	  InMeetRoom.users[UserName].localvideo.srcObject = selfMediaStream;
}

function getUserMediaError(error){
	console.log(' getUserMediaError >> error name :  '+ error.name);
	console.log(' getUserMediaError >> error message :  '+ error.message);
}

function startICECandidate(message) {
	var fromId = message.from;
    let candidate = new RTCIceCandidate(message.candidate);
    console.log("Adding received ICE candidate: ");
    console.log("startICECandidate from : "+fromId+" to : "+message.to);
      
    InMeetRoom.users[fromId]
    .CallerPeerConnection
    .addIceCandidate(candidate)
    .catch(function(error){
		console.log(' addIceCandidate >> error name :  '+ error.name);
		console.log(' addIceCandidate >> error message :  '+ error.message);
	});
}

////////// Other
async function startOffer(message) {
	var fromId = message.from;
		if( fromId in InMeetRoom.users){
			console.log('startOffer already create >>'+ fromId)
			return;
		}else{
			await createUser(fromId);
			await createUserPeerConnection(fromId);
			console.log('startOffer create >>'+ fromId);
		}
		
	let desc = new RTCSessionDescription(message.sdp);
	 if (desc != null && message.sdp != null) {
		console.log("offer >> CallerPeerConnection.setRemoteDescription");
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
			sendServer(stompSendUrl+'/Answer',{},{
			from: UserName,
			to: fromId,
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
	CreateUserVideoTag(isName, isName); // Add Document
	InMeetRoom.users[isName]={};
	InMeetRoom.users[isName].localvideo = document.getElementById('video'+isName);
}

function CreateUserVideoTag(divName, videoName){
	var div = document.createElement('div');
	div.setAttribute('class', 'col-auto mb-3');	
	div.setAttribute('id', 'div'+divName);	
	
	var video =  document.createElement('video');
	video.setAttribute('id', 'video'+videoName);	
	div.setAttribute('class', 'h-25');	
	video.setAttribute('autoplay', 'autoplay');
	div.append(video);
	var list = document.getElementById('MeetVideos');
	list.appendChild(div);
}

async function createUserPeerConnection(isName){
	InMeetRoom.users[isName].CallerPeerConnection = new RTCPeerConnection(peerConnectionConfig);
	InMeetRoom.users[isName].CallerPeerConnection.addStream(selfMediaStream); //input mediaStream; apply -> onnegotiationneeded
	InMeetRoom.users[isName].CallerPeerConnection.onicecandidate = function (args) {
																		    if (args.candidate) {
																				console.log("Sending ice packet to other peer");
																				sendServer(stompSendUrl+'/Ice',{},{
																		            from: UserName,
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
																			    sendServer(stompSendUrl+'/Offer',{},{
																		            from: UserName,
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



