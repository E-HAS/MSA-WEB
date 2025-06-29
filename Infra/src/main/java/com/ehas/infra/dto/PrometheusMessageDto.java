package com.ehas.infra.dto;

import java.util.List;

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
public class PrometheusMessageDto {
	private String time;
	private Integer serverSeq;
	private String serverName;
	private Integer second;
	private List<PrometheusDto> lists;
}
