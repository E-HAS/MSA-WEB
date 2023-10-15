window.onload = function () {

}

let RoomId;

 function connect() {
	 
        var socket = new SockJS("https://"+window.location.host+"/Stomp");

        stompClient = Stomp.over(socket);

        // connect(header,연결 성공시 콜백,에러발생시 콜백)
        stompClient.connect({}, function () {
                // subscribe(subscribe url, function)
                stompClient.subscribe('/Stream/Room/Join/'+RoomId, function ( receive ) {
					msg = JSON.parse(receive.body)
                });
                
                stompClient.subscribe('/Stream/Room/offer/'+RoomId, function ( receive ) {
					msg = JSON.parse(receive.body)
                });
                
                stompClient.subscribe('/Stream/Room/answer/'+RoomId, function ( receive ) {
					msg = JSON.parse(receive.body)
                });
                
                stompClient.subscribe('/Stream/Room/ice/'+RoomId, function ( receive ) {
					msg = JSON.parse(receive.body)
                });
                
                stompClient.subscribe('/Stream/Room/ice/'+RoomId, function ( receive ) {
					msg = JSON.parse(receive.body)
                });
                
            },
            function(e){
                alert('error : ' + e);
            }
        );
    }
    
    function sendServer(url, header, data){
        // send(prefix + MessageMapping url, 헤더, 페이로드)
        stompClient.send(url, header, data);
    }