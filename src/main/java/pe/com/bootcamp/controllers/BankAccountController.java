package pe.com.bootcamp.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import pe.com.bootcamp.configurations.WebClientInstance1Config;
import pe.com.bootcamp.domain.aggregate.BankAccount;
import pe.com.bootcamp.domain.fallback.BankAccountFallback;
import pe.com.bootcamp.utilities.ResultBase;
import pe.com.bootcamp.utilities.UnitResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/BankAccount")
@Import(WebClientInstance1Config.class)
public class BankAccountController extends BankAccountFallback {
			
	private final ReactiveCircuitBreaker circuitBreakerDefault;
	//private final ReactiveCircuitBreaker circuitBreakerDefaultBulk;
	private final WebClient webClient;	
	
	public BankAccountController(
			ReactiveCircuitBreakerFactory<?, ?> circuitBreakerFactory,
			@Qualifier("BankAccount") WebClient.Builder loadBalancerBankAccount) {
		this.circuitBreakerDefault = circuitBreakerFactory.create("Default");		
		this.webClient = loadBalancerBankAccount.build();
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)		
	public Mono<UnitResult<BankAccount>> create(@RequestBody BankAccount entity){				
		return webClient.post()
				.uri("/").bodyValue(entity).retrieve()
				.bodyToMono(new UnitResult<BankAccount>().getClassOfT())
				.transform(fn -> this.circuitBreakerDefault						
						.run(fn, throwable -> Mono.just(new UnitResult<BankAccount>(true, throwable.getMessage()))));
	}
	@RequestMapping(value = "/", method = RequestMethod.PUT)		
	public Mono<UnitResult<BankAccount>> update(@RequestBody BankAccount entity){
		return webClient.put().uri("/")
				.bodyValue(entity)
				.retrieve().bodyToMono(new UnitResult<BankAccount>().getClassOfT())
				.transform(fn -> this.circuitBreakerDefault.run(fn, throwable -> super.fallback_update(entity, throwable)));
	}
	@RequestMapping(value = "/batch", method = RequestMethod.POST)	
	public Mono<UnitResult<BankAccount>> saveAll(@RequestBody Flux<BankAccount> entities){
		return webClient.post().uri("/batch")
				.bodyValue(entities)
				.retrieve().bodyToMono(new UnitResult<BankAccount>().getClassOfT())
				.transform(fn -> this.circuitBreakerDefault.run(fn, throwable -> super.fallback_saveAll(entities, throwable)));
	}
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)	
	public Mono<UnitResult<BankAccount>> findById(@PathVariable String id){
		return webClient.get().uri("/{id}", id)
				.retrieve().bodyToMono(new UnitResult<BankAccount>().getClassOfT())
				.transform(fn -> this.circuitBreakerDefault.run(fn, throwable -> super.fallback_findById(id, throwable)));
	}
	@RequestMapping(value = "/{dni}", method = RequestMethod.GET)	
	public Mono<UnitResult<BankAccount>> findByClientIdentNum(@PathVariable String dni){
		return webClient.get().uri("/{dni}", dni)
				.retrieve().bodyToMono(new UnitResult<BankAccount>().getClassOfT())
				.transform(fn -> this.circuitBreakerDefault.run(fn, throwable -> super.fallback_findByClientIdentNum(dni, throwable)));
	}
	@RequestMapping(value = "/{accountNumber}", method = RequestMethod.POST)	
	public Mono<UnitResult<BankAccount>> findByAccountNumber(@PathVariable String accountNumber){
		return webClient.get().uri("/{accountNumber}", accountNumber)
				.retrieve().bodyToMono(new UnitResult<BankAccount>().getClassOfT())
				.transform(fn -> this.circuitBreakerDefault.run(fn, throwable -> super.fallback_findByAccountNumber(accountNumber, throwable)));
	}
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public Mono<UnitResult<BankAccount>> findAll(){		
		return webClient.get().uri("/").retrieve().bodyToMono(new UnitResult<BankAccount>().getClassOfT())
				.transform(fn -> this.circuitBreakerDefault.run(fn, throwable -> super.fallback_findAll(throwable)));
	}	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)	
	public Mono<ResultBase> deleteById(@PathVariable String id){
		return webClient.delete().uri("/{id}", id).retrieve().bodyToMono(ResultBase.class)
				.transform(fn -> this.circuitBreakerDefault.run(fn, throwable -> super.fallback_deleteById(id, throwable)));
	}
	@RequestMapping(value = "/stream", method = RequestMethod.GET,produces = MediaType.TEXT_EVENT_STREAM_VALUE)	
	public Flux<BankAccount> findAllStreaming(){
		return webClient.get().uri("/stream").retrieve().bodyToFlux(BankAccount.class);
	}
}