/*
var cameraOff=false;
var audioOff=false;
var screenOff=false;

var btnAudio = document.getElementById('btnAudio');
var btnVideo = document.getElementById('btnVideo');
var btnScreen = document.getElementById('btnScreen');
  
btnAudio.addEventListener("click", handleMuteClick);
btnVideo.addEventListener("click", handleCameraClick);
btnScreen.addEventListener("click", handleScreenClick);
 */

selectCamera = document.getElementById('selectCamera');
selectAudioInput = document.getElementById('selectAudioInput');
selectAudioOutput = document.getElementById('selectAudioOutput');

 async function settinginOutPut() {
  try {
  navigator.mediaDevices.enumerateDevices().then((devices) => {
    devices.forEach((device) => {
		console.log('device : '+device);
		console.log('device deviceId :'+device.deviceId);
		console.log('device groupId :'+device.groupId);
		console.log('device kind :'+device.kind);
		console.log('device label :'+device.label);
    });
  });
  
  
    const devices = await navigator.mediaDevices.enumerateDevices();
    const cameras = devices.filter((device) => device.kind === "videoinput");
    const audeoinputs = devices.filter((device) => device.kind === "audioinput");
    const audeooutputs = devices.filter((device) => device.kind === "audiooutput");
    
    cameras.forEach((camera) => {
      const option = document.createElement("option");
      option.value = camera.deviceId;
      option.innerText = camera.label;
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

var playCameraOff=true;
var playCameraStream;
function playCamera(videoId, selectId, imgId){
	 selectEl = document.querySelector('#'+selectId);
	 imgEl = document.querySelector('#'+imgId);
	 selectVideo= document.querySelector('#'+videoId);
	 
	 if(playCameraOff){	 				
								selectVideo.play();			
								navigator.mediaDevices
							    .getUserMedia({
							    video: {
							            width: 1980, // 최대 너비
							            height: 1080, // 최대 높이
							            frameRate: 60, // 최대 프레임
							            deviceId : {
											exact: selectEl.value
										}
							    	} 
								})
								.then((stream) => {
								 playCameraStream=stream
								 selectVideo.srcObject = playCameraStream })
								.catch( (e) => { console.log("error : "+e)})
								
								imgEl.src='/img/svg/play_on.svg';
								playCameraOff=false;
	}else{	
		playCameraStream
    	.getVideoTracks() // 스트림에서 getVideoTracks() 가져오기
    	.forEach((track) => (track.enabled = !track.enabled));
		imgEl.src='/img/svg/play_off.svg';
		playCameraOff=true;
	}
}

var playCameraOff=true;
var playCameraStream;
function playCamera(videoId, selectId, imgId){
	 selectEl = document.querySelector('#'+selectId);
	 imgEl = document.querySelector('#'+imgId);
	 selectVideo= document.querySelector('#'+videoId);
	 
	 if(playCameraOff){	 				
								selectVideo.play();			
								navigator.mediaDevices
							    .getUserMedia({
							    video: {
									    width: { min: 1280 },
									    height: { min: 720 },
							            frameRate: 60, // 최대 프레임
							            deviceId : {
											exact: selectEl.value
										}
							    	} 
								})
								.then((stream) => {
								 playCameraStream=stream
								 selectVideo.srcObject = playCameraStream })
								.catch( (e) => { console.log("error : "+e)})
								
								imgEl.src='/img/svg/play_on.svg';
								playCameraOff=false;
	}else{	
		playCameraStream
    	.getVideoTracks() // 스트림에서 getVideoTracks() 가져오기
    	.forEach((track) => (track.enabled = !track.enabled));
		imgEl.src='/img/svg/play_off.svg';
		playCameraOff=true;
	}
}

var playMikeOff=true;
var playMikeStream;
function playMike(videoId, selectId, imgId){
	 selectEl = document.querySelector('#'+selectId);
	 imgEl = document.querySelector('#'+imgId);
	 selectVideo= document.querySelector('#'+videoId);
	 
	 selectOutEl = document.querySelector('#selectAudioOutput');
	 
	 if(playMikeOff){	 				
								selectVideo.play();			
								navigator.mediaDevices
							    .getUserMedia({
							    audio: {
							            deviceId : {
											exact: selectEl.value
										}
										//sampleRate 	fixed: 48000
							    	}
								})
								.then((stream) => {
								 playMikeStream=stream
								 selectVideo.srcObject = playMikeStream 
								 selectVideo.setSinkId(selectOutEl.value)
								 })
								.catch( (e) => { console.log("error : "+e)})
								
								imgEl.src='/img/svg/play_on.svg';
								playMikeOff=false;
	}else{	
	    playMikeStream
	    .getAudioTracks() // 스트림에서 getAudioTrack() 가져오기
	    .forEach((track) => (track.enabled = !track.enabled));
		imgEl.src='/img/svg/play_off.svg';
		playMikeOff=true;
	}
}
