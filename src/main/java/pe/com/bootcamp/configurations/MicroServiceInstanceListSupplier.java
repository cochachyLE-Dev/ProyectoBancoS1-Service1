package pe.com.bootcamp.configurations;

import java.util.Arrays;
import java.util.List;

import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;

import reactor.core.publisher.Flux;

public class MicroServiceInstanceListSupplier implements ServiceInstanceListSupplier  
{	
	private final String serviceId;
	private final String hostname;
	private final Integer[] ports;
	
	MicroServiceInstanceListSupplier(String serviceId, String hostname, Integer... port) {
	    this.serviceId = serviceId;
	    this.hostname = hostname;
	    this.ports = port;
	}
	
	@Override
	public Flux<List<ServiceInstance>> get() {		
		return Flux.just(ports)
                .map(port -> {
                	return Arrays.asList(new DefaultServiceInstance(serviceId + "-" + port, serviceId, hostname, port, false));
                });
	}

	@Override
	public String getServiceId() {
		return serviceId;
	}
}
