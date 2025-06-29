package com.ehas.infra.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ehas.infra.dto.PrometheusDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActuatorService {
	@Value("${value.prometheus.check.list}")
	private List<String> prometheusList;
	 
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
	
	public List<PrometheusDto> onPrometheusMonitoring(String url) {
		RestTemplate restTemplate = new RestTemplate();
		String prometheusTexts = restTemplate.getForObject(url, String.class);
		
		if (prometheusTexts == null) {
			return Collections.emptyList();
		}
		
		Map<String, List<PrometheusDto>> prometheusMap = new HashMap<>(); 
		for(String line : prometheusTexts.split("\n")) { // 라인별 읽기
			if(line.isEmpty() || line.startsWith("#")) { // 빈칸 또는 "#" 건너 뛰기
				continue;
			}
			
			int findIndex = line.indexOf(" "); // Label, value 분리
			if(findIndex > 0) {
				String label = line.substring(0, findIndex).trim();
				//String value = line.substring(findIndex+1).trim();
				
				String key=label;
				String[] parts = label.split("_"); 
			    if (parts.length >= 2) {
			    	key = parts[0] + "_" + parts[1]; // ?_? 추출
			    }
			    
				try {
					if(!prometheusMap.containsKey(key)) {
						prometheusMap.put(key, new ArrayList<PrometheusDto>());
					}
					
					prometheusMap.get(key).add(new PrometheusDto(line));
					
				}catch(Exception e) {
					log.info("[Error] Prometheus Read Line :"+line);
				}
			}
		}
		
		List<PrometheusDto> results = new ArrayList<PrometheusDto>();
		for(String findKey : prometheusList) {
		    if (!prometheusMap.containsKey(findKey)) {
		        continue;
		    }
		    
			for(PrometheusDto findDto : prometheusMap.get(findKey)) {
				findDto.onExtract();
			}
			
			results.addAll(prometheusMap.get(findKey));
		}
		
		return results;
	}
}
