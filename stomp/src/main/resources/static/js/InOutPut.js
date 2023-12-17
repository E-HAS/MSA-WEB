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
let hostName = window.location.protocol + '//' + window.location.host;
selectCamera = document.getElementById('selectCamera');
selectAudioInput = document.getElementById('selectAudioInput');
selectAudioOutput = document.getElementById('selectAudioOutput');

 async function settinginOutPut() {
  try {
  
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

let elMeterAudioInput = document.querySelector('#meterAudioInput');
let elRangeMike  = document.querySelector('#rangeMike');
function settingMikeVolume(stream,value){
  // We assume only one audio track per stream
  const audioTrack = stream.getAudioTracks()[0]
  var ctx = new AudioContext()
  var src = ctx.createMediaStreamSource(new MediaStream([audioTrack]))
  var dst = ctx.createMediaStreamDestination()
  var gainNode = ctx.createGain()
  //gainNode.gain.value = value; // mike sound
  gainNode.gain.value = 1; // 일시적으로 유지    n * n * ... 현상이있음
  // Attach src -> gain -> dst
  [src, gainNode, dst].reduce( function(a, b) { return a && a.connect(b) });  
  stream.removeTrack(audioTrack)
  stream.addTrack(dst.stream.getAudioTracks()[0])
}

function checkMikeVolume(stream){
	const audioContext = new AudioContext();
    const analyser = audioContext.createAnalyser();
    const microphone = audioContext.createMediaStreamSource(stream);
    const scriptProcessor = audioContext.createScriptProcessor(2048, 1, 1);

    analyser.smoothingTimeConstant = 0.8;
    analyser.fftSize = 1024;

    microphone.connect(analyser);
    analyser.connect(scriptProcessor);
    scriptProcessor.connect(audioContext.destination);
    scriptProcessor.onaudioprocess = function() {
      const array = new Uint8Array(analyser.frequencyBinCount);
      analyser.getByteFrequencyData(array);
      const arraySum = array.reduce((a, value) => a + value, 0);
      const average = arraySum / array.length;
      var sizeInputMike = Math.round(average);
      elMeterAudioInput.value = Math.round(sizeInputMike);
    };
}


var playMikeOff=true;
var playStream;
function playMike(){
	 var tagImg = document.querySelector('#imgPlay');
	 
	 var tagVideo= document.querySelector('#localVideo');
	 var selectVideo = document.querySelector('#selectCamera');
	 var checkVideo = selectVideo.value ?  {
										    width: { min: 1280 },
										    height: { min: 720 },
								            frameRate: 60, // 최대 프레임
								            deviceId : {exact: tagVideo.value}
							    	} : false ;
	 var selectMike = document.querySelector('#selectAudioInput');
	 var selectSpeacker = document.querySelector('#selectAudioOutput');
	 
	 if(playMikeOff){	 				
								tagVideo.play();			
								navigator.mediaDevices
							    .getUserMedia({
								video: checkVideo,
							    audio: {
							            deviceId : {
											exact: selectMike.value
										}
										//sampleRate 	fixed: 48000
							    	}
								})
								.then((stream) => {
								 playStream=stream
								 tagVideo.srcObject = playStream 
								 tagVideo.setSinkId(selectSpeacker.value)
								 
								checkMikeVolume(stream);
    							settingMikeVolume(stream,1);
								 })
								.catch( (e) => { console.log("error : "+e)})
								
								tagImg.src=hostName+'/img/svg/play_on.svg';
								playMikeOff=false;
	}else{	
	    playStream
	    .getAudioTracks() // 스트림에서 getAudioTrack() 가져오기
	    .forEach((track) => (track.enabled = !track.enabled));
		tagImg.src=hostName+'/img/svg/play_off.svg';
		playMikeOff=true;
	}
}

let intervalSettingOption;
function funcClickSettingOption(){
	var elCollapse = document.querySelector('#collapseSettingIO');
	
	var className = elCollapse.getAttribute('class');
	
	if(className=='collapse'){
		clearInterval(intervalSettingOption);
	}else{
		intervalSettingOption= setInterval(function() {
		console.log(selfMediaStream.getAudioTracks());
		console.log(elRangeMike.value);
		settingMikeVolume(selfMediaStream,elRangeMike.value);
		console.log(selfMediaStream);
		}, 1000);
	}
	
}



