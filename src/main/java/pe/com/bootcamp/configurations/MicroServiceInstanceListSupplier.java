package pe.com.bootcamp.configurations;

import java.util.ArrayList;
import java.util.List;

import org.javatuples.Triplet;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;

import reactor.core.publisher.Flux;

public class MicroServiceInstanceListSupplier implements ServiceInstanceListSupplier  
{	
	private final String serviceId;
	private final List<Triplet<String, Integer, Boolean>> instances;
	
	MicroServiceInstanceListSupplier(String serviceId, List<Triplet<String, Integer, Boolean>> instances) {
	    this.serviceId = serviceId;	    
	    this.instances = instances;	   
	}
	
	@Override
	public Flux<List<ServiceInstance>> get() {		
		return Flux.just(instances)
				.map(i -> {
					List<ServiceInstance> list = new ArrayList<ServiceInstance>();					
					i.forEach(e -> list.add(new DefaultServiceInstance(serviceId + "-" + e.getValue1(), serviceId, e.getValue0(), e.getValue1(), e.getValue2())));					
					return list;
				});
	}

	@Override
	public String getServiceId() {
		return serviceId;
	}
}
