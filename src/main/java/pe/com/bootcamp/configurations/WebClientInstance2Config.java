package pe.com.bootcamp.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.Data;

@Configuration
@LoadBalancerClient(name = WebClientInstance2Config.InstanceName, configuration = ServiceInstance2ListSupplier.class)
public class WebClientInstance2Config {
	
	public static final String InstanceName = "Instance2";
	
	@Autowired
	LoadBalancerInstance2Config config;
	
	@LoadBalanced
	@Bean	
	@Primary
	@Qualifier("Loan")
  	WebClient.Builder LoanClientBuilder() {		
		return WebClient.builder()
				.baseUrl(config.getBaseUrl(InstanceName,"Loan"));
	}	
}
class ServiceInstance2ListSupplier{
	@Autowired
	LoadBalancerInstance2Config config;
	
	@Bean
	@Primary	
	ServiceInstanceListSupplier serviceInstanceListSupplier() {
		return new MicroServiceInstanceListSupplier(WebClientInstance2Config.InstanceName, config.getHostname(), config.getPorts());
	}
}
@Configuration
@ConfigurationProperties(prefix = "vaetech.load-balancer.instance2")
@Data
class LoadBalancerInstance2Config {	
	private String protocol;
	private String hostname;	
	private Integer[] ports;
		
	public final String getBaseUrl(String instanceName, String controllerName) {
		protocol = protocol == null ? "http": protocol;
		return  protocol + "://" + instanceName + "/" + controllerName;
	}
}