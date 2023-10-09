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
