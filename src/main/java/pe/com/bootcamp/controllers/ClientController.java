package pe.com.bootcamp.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import pe.com.bootcamp.configurations.WebClientInstance1Config;
import pe.com.bootcamp.domain.aggregate.Client;
import pe.com.bootcamp.domain.fallback.ClientFallback;
import pe.com.bootcamp.utilities.ResultBase;
import pe.com.bootcamp.utilities.UnitResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/Client")
@Import(WebClientInstance1Config.class)
public class ClientController extends ClientFallback 
{
	private final ReactiveCircuitBreaker circuitBreakerDefault;
	private final WebClient webClient;
	
	public ClientController(
			ReactiveCircuitBreakerFactory<?, ?> circuitBreakerFactory,
			@Qualifier("Client") WebClient.Builder clientWebClientBuilder) {
		this.circuitBreakerDefault = circuitBreakerFactory.create("Default");
		this.webClient = clientWebClientBuilder.build();
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	Mono<UnitResult<Client>> create(@RequestBody Client entity){
		return webClient.post()
				.uri("/")
				.bodyValue(entity)
				.retrieve().bodyToMono(new UnitResult<Client>().getClassOfT())
				.transform(fn -> this.circuitBreakerDefault						
						.run(fn, throwable -> Mono.just(new UnitResult<Client>(true, throwable.getMessage()))));
	}
	@RequestMapping(value = "/", method = RequestMethod.PUT)
	Mono<UnitResult<Client>> update(@RequestBody Client entity){
		return webClient.put()
				.uri("/")
				.bodyValue(entity)
				.retrieve().bodyToMono(new UnitResult<Client>().getClassOfT())
				.transform(fn -> this.circuitBreakerDefault						
						.run(fn, throwable -> Mono.just(new UnitResult<Client>(true, throwable.getMessage()))));
	}
	@RequestMapping(value = "/batch", method = RequestMethod.POST)
	Mono<UnitResult<Client>> saveAll(@RequestBody Flux<Client> entities){
		return webClient.post()
				.uri("/batch")
				.bodyValue(entities)
				.retrieve().bodyToMono(new UnitResult<Client>().getClassOfT())
				.transform(fn -> this.circuitBreakerDefault						
						.run(fn, throwable -> Mono.just(new UnitResult<Client>(true, throwable.getMessage()))));
	}
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	Mono<UnitResult<Client>> findById(@PathVariable String id){
		return webClient.get()
				.uri("/{id}", id)				
				.retrieve().bodyToMono(new UnitResult<Client>().getClassOfT())
				.transform(fn -> this.circuitBreakerDefault						
						.run(fn, throwable -> Mono.just(new UnitResult<Client>(true, throwable.getMessage()))));
	}
	@RequestMapping(value = "/{dni}", method = RequestMethod.GET)
	Mono<UnitResult<Client>> findByIdentNum(@PathVariable String dni){
		return webClient.get()
				.uri("/{dni}", dni)				
				.retrieve().bodyToMono(new UnitResult<Client>().getClassOfT())
				.transform(fn -> this.circuitBreakerDefault						
						.run(fn, throwable -> Mono.just(new UnitResult<Client>(true, throwable.getMessage()))));
	}	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	Mono<UnitResult<Client>> findAll(){
		return webClient.get()
				.uri("/")				
				.retrieve().bodyToMono(new UnitResult<Client>().getClassOfT())
				.transform(fn -> this.circuitBreakerDefault						
						.run(fn, throwable -> Mono.just(new UnitResult<Client>(true, throwable.getMessage()))));
	}
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	Mono<ResultBase> deleteById(@PathVariable String id){
		return webClient.delete()
				.uri("/{id}", id)				
				.retrieve().bodyToMono(ResultBase.class)
				.transform(fn -> this.circuitBreakerDefault						
						.run(fn, throwable -> Mono.just(new ResultBase(true, throwable.getMessage()))));
	}
}
