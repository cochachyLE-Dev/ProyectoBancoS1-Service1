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
@LoadBalancerClient(name = WebClientInstance1Config.InstanceName, configuration = ServiceInstance1ListSupplier.class)
public class WebClientInstance1Config {
	
	public static final String InstanceName = "Instance1";
	
	@Autowired
	LoadBalancerInstance1Config config;	
	
	@LoadBalanced
	@Bean	
	@Primary
	@Qualifier("BankAccount")
  	WebClient.Builder BankAccountWebClientBuilder() {		
		return WebClient.builder()
				.baseUrl(config.getBaseUrl(InstanceName,"BankAccount"));
	}
	
	@LoadBalanced
	@Bean
	@Qualifier("BankCard")
  	WebClient.Builder BankCardWebClientBuilder() {		
		return WebClient.builder().baseUrl(config.getBaseUrl(InstanceName,"Card"));
	}
	
	@LoadBalanced
	@Bean
	@Qualifier("Client")
  	WebClient.Builder ClientWebClientBuilder() {		
		return WebClient.builder().baseUrl(config.getBaseUrl(InstanceName,"Client"));
	}
}
class ServiceInstance1ListSupplier{
	
	@Autowired
	LoadBalancerInstance1Config config;
	
	@Bean
	@Primary	
	ServiceInstanceListSupplier serviceInstanceListSupplier() {
		return new MicroServiceInstanceListSupplier(WebClientInstance1Config.InstanceName, config.getHostname(), config.getPorts());
	}
}

@Configuration
@ConfigurationProperties(prefix = "vaetech.load-balancer.instance1")
@Data
class LoadBalancerInstance1Config {	
	private String protocol;
	private String hostname;	
	private Integer[] ports;
		
	public final String getBaseUrl(String instanceName, String controllerName) {
		protocol = protocol == null ? "http": protocol;
		return  protocol + "://" + instanceName + "/" + controllerName;
	}
}