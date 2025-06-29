package com.ehas.infra.dto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PrometheusDto {
	@JsonIgnore
	private String text;
	
	private Integer seq;
	private String label;
	private String opt;
	private Double value;
    
	public PrometheusDto(String text) {
		this.text = text;
	}
	
	public void onExtract() {
		String regex = "(\\w+)(\\{[^}]+\\})?\\s+([0-9.eE+-]+)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(this.text);

		if (matcher.find()) {
		    this.label = matcher.group(1);
		    this.opt = matcher.group(2);
		    this.value = Double.parseDouble(matcher.group(3));
		}
	}
	
	public String findValueInOpt(String findKey) {
		Map<String, String> labelMap = new HashMap<>();

		// 라벨 이름과 값을 추출하는 정규식
		Pattern labelPattern = Pattern.compile("(\\w+)=\"([^\"]+)\"");
		Matcher labelMatcher = labelPattern.matcher(this.opt);

		while (labelMatcher.find()) {
		    String key = labelMatcher.group(1);   // 예: method, uri
		    String value = labelMatcher.group(2); // 예: GET, /hello
		    labelMap.put(key, value);
		}
		
		return labelMap.get(findKey);
	}
}
