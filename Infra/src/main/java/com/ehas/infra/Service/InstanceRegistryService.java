package com.ehas.infra.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.netflix.eureka.registry.PeerAwareInstanceRegistry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InstanceRegistryService {
	private final PeerAwareInstanceRegistry registry;
	private final ActuatorService actuatorService;
	
	public List<String> getServices(){
		return registry.getApplications().getRegisteredApplications()
                .stream()
                .map(application -> application.getName())
                .collect(Collectors.toList());
	}
	
	public List<String> getInstanceIdsByService(String _serviceName){
		return registry.getApplication(_serviceName)
                .getInstances()
                .stream()
                .map(instance -> instance.getInstanceId())
                .collect(Collectors.toList());
	}
	
	public List<String> onRefreshByService(String _serviceName){
		List<String> instancesList = new ArrayList<>();
		registry.getApplication(_serviceName).getInstances().forEach(instance -> {
        	String id  = instance.getInstanceId();
            String url = instance.getHomePageUrl();
            instancesList.add(
            		id+" "+actuatorService.onRefresh(String.format("%s/actuator/refresh", url))
            		);
        });
		
		return instancesList;
	}
	
	public Map<String, Map> onMetricsByService(String _serviceName){
		Map<String, Map> instancesList = new HashMap<>();
		
		 registry.getApplication(_serviceName).getInstances().forEach(instance -> {
	            String url = instance.getHomePageUrl();
	            String urlMaxJvmMemory = String.format("%s/actuator/metrics/jvm.memory.max", url);
	            String urlUsedJvmMemory = String.format("%s/actuator/metrics/jvm.memory.used", url);
	            String urlProcessCpuUsage = String.format("%s/actuator/metrics/process.cpu.usage", url);
	            String urlSystemCpuCount = String.format("%s/actuator/metrics/system.cpu.count", url);
	            String urlSystemCpuUsage = String.format("%s/actuator/metrics/system.cpu.usage", url);

	            instancesList.put(url+ "(" + instance.getInstanceId() + ")",
	                    Map.of(
	                            "jvm.memory.max", actuatorService.onPerformanceMonitoring(urlMaxJvmMemory),
	                            "jvm.memory.used", actuatorService.onPerformanceMonitoring(urlUsedJvmMemory),
	                            "process.cpu.usage", actuatorService.onPerformanceMonitoring(urlProcessCpuUsage),
	                            "system.cpu.count", actuatorService.onPerformanceMonitoring(urlSystemCpuCount),
	                            "system.cpu.usage", actuatorService.onPerformanceMonitoring(urlSystemCpuUsage)
	                    ));
	     });
		 
		 return instancesList;
	}
}
