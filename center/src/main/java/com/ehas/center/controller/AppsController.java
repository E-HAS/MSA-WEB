package com.ehas.center.controller;

import java.util.List;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/apps/api")
@RequiredArgsConstructor
public class AppsController {
	private DiscoveryClient discoveryClient;  
	
	@GetMapping("/ping")  
	public List<ServiceInstance> ping() {  
		log.info("INSTANCES FIND");  
		List<ServiceInstance> instances = discoveryClient.getInstances("CENTERGATEWAY");  
		log.info("INSTANCES: count = {}", instances.size());  
		instances.forEach(it -> log.info("INSTANCE: id={}, prot={}", it.getServiceId(), it.getPort()));  
		return instances;  
	}  
}
