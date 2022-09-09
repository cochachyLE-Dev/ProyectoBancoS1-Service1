package pe.com.bootcamp.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.InstanceInfo.InstanceStatus;
import com.netflix.discovery.EurekaClient;

import lombok.Data;

@Configuration
@LoadBalancerClient(name = WebClientInstance1Config.InstanceName, configuration = ServiceInstance1ListSupplier.class)
public class WebClientInstance1Config {
	
	public static final String InstanceName = "MICRO-SERVICE1";
	
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
	
	@Autowired
    @Lazy
    private EurekaClient eurekaClient;
	
	@Bean
	@Primary	
	ServiceInstanceListSupplier serviceInstanceListSupplier() throws Exception {
		List<InstanceInfo> instances = eurekaClient.getApplications()
				.getRegisteredApplications(WebClientInstance1Config.InstanceName)
				.getInstances();
		
		if(instances == null || instances.size() == 0)
			throw new Exception("instances not found.");
		
		Integer[] ports = eurekaClient.getApplications()
				.getRegisteredApplications(WebClientInstance1Config.InstanceName)
				.getInstances().stream()
				.filter(e -> e.getStatus().equals(InstanceStatus.UP))
				.map(e -> e.getPort()).toArray(Integer[]::new);
		
		if(ports.length == 0)
			throw new Exception("no available instances found.");
		
		return new MicroServiceInstanceListSupplier(WebClientInstance1Config.InstanceName, config.getHostname(), ports);
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