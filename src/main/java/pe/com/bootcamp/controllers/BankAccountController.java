package pe.com.bootcamp.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

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
			
	private final WebClient webClient;
	
	public BankAccountController(@Qualifier("BankAccount") WebClient.Builder loadBalancerBankAccount) {
		webClient = loadBalancerBankAccount.build();
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	@HystrixCommand(
			fallbackMethod = "fallback_create",
			commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")})
	public Mono<UnitResult<BankAccount>> create(@RequestBody BankAccount entity){
		return webClient.post().uri("/")
				.bodyValue(entity)
				.retrieve().bodyToMono(new UnitResult<BankAccount>().getClassOfT());
	}
	@RequestMapping(value = "/", method = RequestMethod.PUT)	
	@HystrixCommand(
			fallbackMethod = "fallback_update",
			commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")})
	public Mono<UnitResult<BankAccount>> update(@RequestBody BankAccount entity){
		return webClient.put().uri("/")
				.bodyValue(entity)
				.retrieve().bodyToMono(new UnitResult<BankAccount>().getClassOfT());
	}
	@RequestMapping(value = "/batch", method = RequestMethod.POST)
	@HystrixCommand(
			fallbackMethod = "fallback_saveAll",
			commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")})
	public Mono<UnitResult<BankAccount>> saveAll(@RequestBody Flux<BankAccount> entities){
		return webClient.post().uri("/batch")
				.bodyValue(entities)
				.retrieve().bodyToMono(new UnitResult<BankAccount>().getClassOfT());
	}
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@HystrixCommand(
			fallbackMethod = "fallback_findById",
			commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")})
	public Mono<UnitResult<BankAccount>> findById(@PathVariable String id){
		return webClient.get().uri("/{id}", id)
				.retrieve().bodyToMono(new UnitResult<BankAccount>().getClassOfT());
	}
	@RequestMapping(value = "/{dni}", method = RequestMethod.GET)
	@HystrixCommand(
			fallbackMethod = "fallback_findByClientIdentNum",
			commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")})
	public Mono<UnitResult<BankAccount>> findByClientIdentNum(@PathVariable String dni){
		return webClient.get().uri("/{dni}", dni)
				.retrieve().bodyToMono(new UnitResult<BankAccount>().getClassOfT());
	}
	@RequestMapping(value = "/{accountNumber}", method = RequestMethod.POST)
	@HystrixCommand(
			fallbackMethod = "fallback_findByAccountNumber",
			commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")})
	public Mono<UnitResult<BankAccount>> findByAccountNumber(@PathVariable String accountNumber){
		return webClient.get().uri("/{accountNumber}", accountNumber)
				.retrieve().bodyToMono(new UnitResult<BankAccount>().getClassOfT());
	}
	@RequestMapping(value = "/", method = RequestMethod.GET)
	@HystrixCommand(
			fallbackMethod = "fallback_findAll",
			commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")})
	public Mono<UnitResult<BankAccount>> findAll(){
		//Thread.sleep(3000);
		return webClient.get().uri("/").retrieve().bodyToMono(new UnitResult<BankAccount>().getClassOfT());
	}	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@HystrixCommand(
			fallbackMethod = "fallback_deleteById",
			commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")})
	public Mono<ResultBase> deleteById(@PathVariable String id){
		return webClient.delete().uri("/{id}", id).retrieve().bodyToMono(ResultBase.class);
	}
	@RequestMapping(value = "/stream", method = RequestMethod.GET,produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	@HystrixCommand(
			fallbackMethod = "fallback_findAllStreaming",
			commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")})
	public Flux<BankAccount> findAllStreaming(){
		return webClient.get().uri("/stream").retrieve().bodyToFlux(BankAccount.class);
	}
}