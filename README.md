![img](https://github.com/cochachyLE-Dev/ProyectoBancoS1-Service1/blob/main/Diagram-Arquitecture.PNG)

![img](https://github.com/cochachyLE-Dev/ProyectoBancoS1-Service1/blob/main/Service1-Hystrix-Stream.PNG)

![img](https://github.com/cochachyLE-Dev/ProyectoBancoS1-Service1/blob/main/Service1-Hystrix-Stream-Postman-2.PNG)

![img](https://github.com/cochachyLE-Dev/ProyectoBancoS1-Service1/blob/main/Service1-Hystrix-Stream-Postman-3.PNG)


No se registra el servicio "Service1" en Eureka, es innecesario. Se usa EurekaClient para acceder a las instancias del servicio para luego usarlas en el Balanceador de carga.

Configuración basica de EurekaClient
```yaml
eureka:   
  client:    
    register-with-eureka: false  # No registra el servicio en Eureka
    serviceUrl:
      defaultZone: http://localhost:8761/eureka
```

Configuración personalizada del Balanceador de carga [application.yml]
```yaml
vaetech:
  load-balancer:
    instance1:
      protocol: http
      hostname: localhost
      #ports: [58579, 61940, 61941, 61942] # Los puertos lo obtiene de EurekaClient
    instance2:
      protocol: http
      hostname: localhost
      #ports: [59307] # Los puertos lo obtiene de EurekaClient
```

Configuración personalizada del Balanceador de carga

```java
class ServiceInstance1ListSupplier{
	
	@Autowired
	LoadBalancerInstance1Config config;
	
	@Autowired
    @Lazy
    EurekaClient eurekaClient;
	
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
```

Controlador de instancias para EurekaClient. La idea es mostrar las instancias en el Service-Discovery
 [ProyectoBancoS1-AdminServer](https://github.com/cochachyLE-Dev/ProyectoBancoS1-AdminServer)

![img](https://github.com/cochachyLE-Dev/ProyectoBancoS1-Service1/blob/main/Service1-Instances.PNG)

![img](https://github.com/cochachyLE-Dev/ProyectoBancoS1-Service1/blob/main/Service1-EurekaClient.PNG)
