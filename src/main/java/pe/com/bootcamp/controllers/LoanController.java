package pe.com.bootcamp.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import pe.com.bootcamp.configurations.WebClientInstance2Config;
import pe.com.bootcamp.domain.aggregate.Loan;
import pe.com.bootcamp.domain.fallback.LoanFallback;
import pe.com.bootcamp.utilities.ResultBase;
import pe.com.bootcamp.utilities.UnitResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/Loan")
@Import(WebClientInstance2Config.class)
public class LoanController extends LoanFallback
{	
	private final WebClient webClient;
	
	public LoanController(@Qualifier("Loan") WebClient.Builder loanBalancerClient) {
		webClient = loanBalancerClient.build();
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public Mono<UnitResult<Loan>> create(@RequestBody Loan entity){
		return webClient.post()
				.uri("/")
				.bodyValue(entity)
				.retrieve().bodyToMono(new UnitResult<Loan>().getClassOfT());
	}
	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public Mono<UnitResult<Loan>> update(@RequestBody Loan entity){
		return webClient.put()
				.uri("/")
				.bodyValue(entity)
				.retrieve().bodyToMono(new UnitResult<Loan>().getClassOfT());
	}
	@RequestMapping(value = "/batch", method = RequestMethod.POST)
	public Mono<UnitResult<Loan>> saveAll(@RequestBody Flux<Loan> entities){
		return webClient.put()
				.uri("/batch")
				.bodyValue(entities)
				.retrieve().bodyToMono(new UnitResult<Loan>().getClassOfT());
	}
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Mono<UnitResult<Loan>> findById(@PathVariable String id){
		return webClient.get()
				.uri("/{id}", id)				
				.retrieve().bodyToMono(new UnitResult<Loan>().getClassOfT());
	}
	@RequestMapping(value = "/{dni}", method = RequestMethod.GET)
	public Mono<UnitResult<Loan>> findByClientIdentNum(@PathVariable String dni){
		return webClient.get()
				.uri("/{dni}", dni)				
				.retrieve().bodyToMono(new UnitResult<Loan>().getClassOfT());
	}
	@RequestMapping(value = "/{accountNumber}", method = RequestMethod.POST)
	public Mono<UnitResult<Loan>> findByAccountNumber(@PathVariable String accountNumber){
		return webClient.get()
				.uri("/{accountNumber}", accountNumber)				
				.retrieve().bodyToMono(new UnitResult<Loan>().getClassOfT());
	}
	@RequestMapping(value = "/", method = RequestMethod.GET)
	@HystrixCommand(fallbackMethod = "fallback_findAll", commandProperties = {
			   @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")
			})
	public Mono<UnitResult<Loan>> findAll(){
		return webClient.get()
				.uri("/")
				.retrieve().bodyToMono(new UnitResult<Loan>().getClassOfT());
	}	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public Mono<ResultBase> deleteById(@PathVariable String id){
		return webClient.get()
				.uri("/{id}", id)				
				.retrieve().bodyToMono(ResultBase.class);
	}
}
