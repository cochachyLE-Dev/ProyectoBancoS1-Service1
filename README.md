![img](https://github.com/cochachyLE-Dev/ProyectoBancoS1-Service1/blob/main/Diagram-Arquitecture.PNG)

![img](https://github.com/cochachyLE-Dev/ProyectoBancoS1-Service1/blob/main/Service1-Hystrix-Stream.PNG)

![img](https://github.com/cochachyLE-Dev/ProyectoBancoS1-Service1/blob/main/Service1-Hystrix-Stream-Postman-2.PNG)

![img](https://github.com/cochachyLE-Dev/ProyectoBancoS1-Service1/blob/main/Service1-Hystrix-Stream-Postman-3.PNG)


No se registra el servicio "Service1" en Eureka, es innecesario. Se usa EurekaClient para acceder a las instancias del servicio para luego usarlas en el Balanceador de carga.

Configuración básica de EurekaClient
```yaml
eureka:   
  client:    
    register-with-eureka: false  # No registra el servicio en Eureka
    serviceUrl:
      defaultZone: http://localhost:8761/eureka
```

Configuración personalizada del Balanceador de carga [Service1.yml].
Es necesario especificar el tipo de protocolo "http" o "https" con la propiedad <b>EnabledSSL</b>

```yaml
vaetech:
  load-balancer:
    instance1:
      enabledSSL: false    # Especificar True cuando las instancias tienen un enlace seguro.
    instance2:
      enabledSSL: false    # Especificar True cuando las instancias tienen un enlace seguro.
```

Configuration personalizada de Balanceador de carga para microservicio1, microservicio2, ...
Se considera el despliegue de los microservicios en multiples servidores.
![img](https://github.com/cochachyLE-Dev/ProyectoBancoS1-Service1/blob/main/Service1-LoadBalancer-Microservice1.PNG)

Configuración personalizada del Balanceador de carga.

```java
class ServiceInstanceListSupplier{
	
	@Autowired
	LoadBalancerInstanceConfig config;
	
	@Autowired
    @Lazy
    EurekaClient eurekaClient;
	
	@Bean
	@Primary	
	ServiceInstanceListSupplier serviceInstanceListSupplier() throws Exception {
		List<InstanceInfo> instanceInfoList = eurekaClient.getApplications()
				.getRegisteredApplications(WebClientInstanceConfig.InstanceName)
				.getInstances();
		
		if(instanceInfoList == null || instanceInfoList.size() == 0)
			throw new Exception("instances not found.");
		
		Boolean isSecure = config.getEnabledSSL();
		List<Triplet<String, Integer, Boolean>> instances = eurekaClient.getApplications()
				.getRegisteredApplications(WebClientInstanceConfig.InstanceName)
				.getInstances().stream()
				.filter(e -> e.getStatus().equals(InstanceStatus.UP))
				.map(e -> new Triplet<String, Integer, Boolean>(e.getIPAddr(), e.getPort(), isSecure)).toList();
		
		if(instances.size() == 0)
			throw new Exception("no available instances found.");
		
		return new MicroServiceInstanceListSupplier(WebClientInstanceConfig.InstanceName, instances);
	}
}
```

Controlador de instancias para EurekaClient. La idea es mostrar las instancias de Service-Discovery en el Admin-Server [ProyectoBancoS1-AdminServer](https://github.com/cochachyLE-Dev/ProyectoBancoS1-AdminServer)

![img](https://github.com/cochachyLE-Dev/ProyectoBancoS1-Service1/blob/main/Service1-Instances.PNG)

![img](https://github.com/cochachyLE-Dev/ProyectoBancoS1-Service1/blob/main/Service1-EurekaClient.PNG)

Con está vista se tendría una visión más completa del Service-Discovery.
![img](https://github.com/cochachyLE-Dev/ProyectoBancoS1-Service1/blob/main/Service1-AdminServer.PNG)


Configuración básica de Circuit Breaker

```Java
@Bean	
Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
    return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
            .circuitBreakerConfig(CircuitBreakerConfig
                    .custom()
                    // slidingWindowSize: Configura el tamaño de la ventana deslizante que se utiliza para registrar el resultado de las llamadas cuando se cierra el disyuntor.
                    .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.TIME_BASED)
                    .slidingWindowSize(10)
                    // minimumNumberOfCalls: Configura el número máximo de llamadas después de la primera exception (disyuntor medio abierto).
                    .minimumNumberOfCalls(5) 
                    // waitDurationInOpenState: Configura la duración del tiempo de espera antes de pasar de abierto a medio abierto.
                    .waitDurationInOpenState(Duration.ofSeconds(15))
                    // failureRateThreshold: Configura el umbral de tasa de errores en porcentaje.
                    .failureRateThreshold(25)
                    .build())
            .timeLimiterConfig(TimeLimiterConfig.custom()
                    // timeoutDuration: Configura la duración del tiempo de espera de respuesta del microservicio.
                    .timeoutDuration(Duration.ofSeconds(1)) 
                    .build())
            .build());
}
```

Configuración básica de Circuit Breaker - <b>timeoutDuration</b>
![img](https://github.com/cochachyLE-Dev/ProyectoBancoS1-Service1/blob/main/Service1-CircuitBreaker-1.PNG)

Configuración básica de Circuit Breaker - <b>minimumNumberOfCalls</b>
![img](https://github.com/cochachyLE-Dev/ProyectoBancoS1-Service1/blob/main/Service1-CircuitBreaker-2.PNG)
