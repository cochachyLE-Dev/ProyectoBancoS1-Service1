package pe.com.bootcamp.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.InstanceInfo.InstanceStatus;
import com.netflix.discovery.EurekaClient;

import pe.com.bootcamp.domain.aggregate.Instance;
import pe.com.bootcamp.utilities.UnitResult;

@RestController
public class InstanceController {

	@Autowired
    @Lazy
    private EurekaClient eurekaClient;
	
    @RequestMapping(value = "/instances", method = RequestMethod.GET)
	public UnitResult<Instance> getInstances() {
    	UnitResult<Instance> result = new UnitResult<Instance>();
    	try {
			List<Instance> arrays = new ArrayList<Instance>();
					
			eurekaClient.getApplications().getRegisteredApplications().forEach(i ->{						
				i.getInstances().stream()
				//.filter(e -> e.getStatus().equals(InstanceStatus.UP))
				.forEach(e -> {			
					arrays.add(Instance.builder()
							.id(e.getInstanceId())
							.name(e.getAppName())
							.pageUrl(e.getHomePageUrl())
							.hostname(e.getHostName())
							.ipAddress(e.getIPAddr())
							.port(e.getPort())
							.Status(e.getStatus().toString())
							.build());
				});			
			});
			
			result.setList(arrays);			
    	} catch (Exception e) {
    		result.setIbException(true);
    		result.setMessage(e.getMessage());
		}
		return result;
	}
    
    @RequestMapping(value = "/instancesbyname/{appname}", method = RequestMethod.GET)
	public UnitResult<Instance> getInstances(@PathVariable String appname) throws Exception {
    	UnitResult<Instance> result = new UnitResult<Instance>();
    	try {
	    	List<Instance> arrays = new ArrayList<Instance>();
			
	    	List<InstanceInfo> instances = eurekaClient.getApplications()
					.getRegisteredApplications(appname)
					.getInstances();
	    	
	    	if(instances == null || instances.size() == 0)
	    		return new UnitResult<Instance>(true, "instances not found.");
	    	
	    	instances.stream()
				.filter(e -> e.getStatus().equals(InstanceStatus.UP))
				.forEach(e -> {			
					arrays.add(Instance.builder()
							.id(e.getInstanceId())
							.name(e.getAppName())
							.pageUrl(e.getHomePageUrl())
							.ipAddress(e.getIPAddr())
							.port(e.getPort())
							.Status(e.getStatus().toString())
							.build());
				});					
		
	    	if(arrays.size() == 0)
	    		result.setList(arrays);
	    	else	    	
	    	{
	    		result.setIbException(true);
	    		result.setMessage("no available instances found.");
	    	}	    		
		} catch (Exception e) {
			result.setIbException(true);
			result.setMessage(e.getMessage());
		}
		return result;
	}
}
