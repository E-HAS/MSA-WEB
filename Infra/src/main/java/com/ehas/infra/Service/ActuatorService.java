package com.ehas.infra.Service;

import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActuatorService {
	public String onRefresh(String url) {
		RestTemplate restTemplate = new RestTemplate();
		
	 	RequestEntity<Void> requestEntity = RequestEntity.post(url)
	 													 .contentType(MediaType.APPLICATION_JSON)
	 													 .build();
	 	
        ResponseEntity<String> response = restTemplate.exchange(requestEntity,String.class);
        
        if (response.getStatusCode().is2xxSuccessful()) {
            return "Refresh Successed";
        } else {
            return "Refresh failed";
        }
	}

	public String onPerformanceMonitoring(String url) {

		RestTemplate restTemplate = new RestTemplate();

		RequestEntity<Void> requestEntity = RequestEntity.get(url)
														 .build();
		
		ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
				requestEntity,
				new ParameterizedTypeReference<Map<String, Object>>() {}
		);

		if (responseEntity.getStatusCode().is2xxSuccessful()) {
			Map<String, Object> body = responseEntity.getBody();
			if (body != null) {
				if (body.containsKey("measurements")) {
					List<Map<String, Object>> measurements = (List<Map<String, Object>>) body.get("measurements");
					if (measurements != null && !measurements.isEmpty()) {
						return measurements.get(0).get("value").toString();
					}
				}
			}
		}
		return "0";
	}
}
