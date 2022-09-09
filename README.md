![img](https://github.com/cochachyLE-Dev/ProyectoBancoS1-Service1/blob/main/Diagram-Arquitecture.PNG)

![img](https://github.com/cochachyLE-Dev/ProyectoBancoS1-Service1/blob/main/Service1-Hystrix-Stream.PNG)

![img](https://github.com/cochachyLE-Dev/ProyectoBancoS1-Service1/blob/main/Service1-Hystrix-Stream-Postman-2.PNG)

![img](https://github.com/cochachyLE-Dev/ProyectoBancoS1-Service1/blob/main/Service1-Hystrix-Stream-Postman-3.PNG)


No se registra el servicio "Service1" en Eureka, es innecesario. Se usa EurekaClient para acceder a las instancias del servicio para luego usarlas en el Balanceador de carga.

```yaml
eureka:   
  client:    
    register-with-eureka: false  # No registra el servicio en Eureka
    serviceUrl:
      defaultZone: http://localhost:8761/eureka
```

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

![img](https://github.com/cochachyLE-Dev/ProyectoBancoS1-Service1/blob/main/Service1-Instances.PNG)

![img](https://github.com/cochachyLE-Dev/ProyectoBancoS1-Service1/blob/main/Service1-EurekaClient.PNG)

