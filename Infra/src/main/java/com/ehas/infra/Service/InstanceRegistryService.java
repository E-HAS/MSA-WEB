package com.ehas.infra.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.netflix.appinfo.InstanceInfo.PortType;
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
	
	public Map<String, List> onPrometheusByService(String _serviceName){
		Map<String, List> instancesList = new HashMap<>();
		
		 registry.getApplication(_serviceName).getInstances().forEach(instance -> {
				String ip = instance.getIPAddr();
				int port = instance.getSecurePort();
				
				String url = instance.isPortEnabled(PortType.SECURE)? "https://" + ip + ":" + port
						: "http://" + ip + ":" + instance.getPort();
	            url += "/actuator/prometheus";
	            
	            instancesList.put(_serviceName+"|"+instance.getHomePageUrl(), actuatorService.onPrometheusMonitoring(url));
	     });
		 
		 return instancesList;
	}
}
