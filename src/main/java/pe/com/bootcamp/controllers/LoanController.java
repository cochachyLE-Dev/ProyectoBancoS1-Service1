package pe.com.bootcamp.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import pe.com.bootcamp.configurations.WebClientInstance2Config;
import pe.com.bootcamp.domain.aggregate.Loan;
import pe.com.bootcamp.domain.fallback.LoanFallback;
import pe.com.bootcamp.utilities.UnitResult;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/Loan")
@Import(WebClientInstance2Config.class)
public class LoanController extends LoanFallback
{	
	private final WebClient webClient;
	
	public LoanController(@Qualifier("Loan") WebClient.Builder loadBalancerClient) {
		webClient = loadBalancerClient.build();
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)	
	@HystrixCommand(fallbackMethod = "fallback_findAll", commandProperties = {
			   @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")
			})
	public Mono<UnitResult<Loan>> findAll() throws InterruptedException{		
		//Thread.sleep(3000);
		
		Mono<UnitResult<Loan>> result = webClient.get().uri("/")
		.retrieve().bodyToMono(new UnitResult<Loan>().getClassOfT());
		
		return result;
	}
}
