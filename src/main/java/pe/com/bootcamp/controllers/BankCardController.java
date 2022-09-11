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

import pe.com.bootcamp.configurations.CircuitBreakerConfiguration;
import pe.com.bootcamp.configurations.WebClientInstance1Config;
import pe.com.bootcamp.domain.aggregate.Card;
import pe.com.bootcamp.domain.fallback.BankCardFallback;
import pe.com.bootcamp.utilities.ResultBase;
import pe.com.bootcamp.utilities.UnitResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/BankCard")
@Qualifier("serviceInstance1")
@Import({ WebClientInstance1Config.class, CircuitBreakerConfiguration.class })
public class BankCardController extends BankCardFallback{

	private final ReactiveCircuitBreaker circuitBreakerDefault;
	private final WebClient webClient;	
	
	public BankCardController(
			ReactiveCircuitBreakerFactory<?, ?> circuitBreakerFactory,
			@Qualifier("BankCard") WebClient.Builder bankCardWebClientBuilder) {
		this.circuitBreakerDefault = circuitBreakerFactory.create("Default");
		this.webClient = bankCardWebClientBuilder.build();
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public Mono<UnitResult<Card>> create(@RequestBody Card entity){
		return webClient.post()
				.uri("/")
				.bodyValue(entity)
				.retrieve().bodyToMono(new UnitResult<Card>().getClassOfT())
				.transform(fn -> this.circuitBreakerDefault						
						.run(fn, throwable -> Mono.just(new UnitResult<Card>(true, throwable.getMessage()))));
	}
	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public Mono<UnitResult<Card>> update(@RequestBody Card entity){
		return webClient.put()
				.uri("/")
				.bodyValue(entity)
				.retrieve().bodyToMono(new UnitResult<Card>().getClassOfT())
				.transform(fn -> this.circuitBreakerDefault						
						.run(fn, throwable -> Mono.just(new UnitResult<Card>(true, throwable.getMessage()))));
	}
	@RequestMapping(value = "/batch", method = RequestMethod.POST)
	public Mono<UnitResult<Card>> saveAll(@RequestBody Flux<Card> entities){
		return webClient.put()
				.uri("/batch")
				.bodyValue(entities)
				.retrieve().bodyToMono(new UnitResult<Card>().getClassOfT())
				.transform(fn -> this.circuitBreakerDefault						
						.run(fn, throwable -> Mono.just(new UnitResult<Card>(true, throwable.getMessage()))));
	}
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Mono<UnitResult<Card>> findById(@PathVariable String id){
		return webClient.get()
				.uri("/{id}", id)				
				.retrieve().bodyToMono(new UnitResult<Card>().getClassOfT())
				.transform(fn -> this.circuitBreakerDefault						
						.run(fn, throwable -> Mono.just(new UnitResult<Card>(true, throwable.getMessage()))));
	}
	@RequestMapping(value = "/{dni}", method = RequestMethod.GET)
	public Mono<UnitResult<Card>> findByClientIdentNum(@PathVariable String dni){
		return webClient.get()
				.uri("/{dni}", dni)				
				.retrieve().bodyToMono(new UnitResult<Card>().getClassOfT())
				.transform(fn -> this.circuitBreakerDefault						
						.run(fn, throwable -> Mono.just(new UnitResult<Card>(true, throwable.getMessage()))));
	}
	@RequestMapping(value = "/{cardNumber}", method = RequestMethod.POST)
	public Mono<UnitResult<Card>> findByCardNumber(@PathVariable String cardNumber){
		return webClient.get()
				.uri("/{cardNumber}", cardNumber)				
				.retrieve().bodyToMono(new UnitResult<Card>().getClassOfT());
	}
	@RequestMapping(value = "/", method = RequestMethod.GET)	
	public Mono<UnitResult<Card>> findAll(){
		//Thread.sleep(3000);
		return webClient.get()
				.uri("/")
				.retrieve().bodyToMono(new UnitResult<Card>().getClassOfT())
				.transform(fn -> this.circuitBreakerDefault
						.run(fn, throwable -> Mono.just(new UnitResult<Card>(true, throwable.getMessage()))));
	}
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public Mono<ResultBase> deleteById(@PathVariable String id){
		return webClient.delete()
				.uri("/{id}", id)
				.retrieve().bodyToMono(ResultBase.class)
				.transform(fn -> this.circuitBreakerDefault						
						.run(fn, throwable -> Mono.just(new UnitResult<Card>(true, throwable.getMessage()))));
	}
}
