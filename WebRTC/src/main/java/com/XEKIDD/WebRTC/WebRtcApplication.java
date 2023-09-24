package com.XEKIDD.WebRTC;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableWebSocket
public class WebRtcApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebRtcApplication.class, args);
	}

}
