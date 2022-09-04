package pe.com.bootcamp.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import pe.com.bootcamp.configurations.WebClientInstance1Config;
import pe.com.bootcamp.domain.aggregate.BankAccount;
import pe.com.bootcamp.domain.fallback.BankAccountFallback;
import pe.com.bootcamp.utilities.UnitResult;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/BankAccount")
@Import(WebClientInstance1Config.class)
public class BankAccountController extends BankAccountFallback {
			
	private final WebClient webClient;
	
	public BankAccountController(@Qualifier("BankAccount") WebClient.Builder loadBalancerBankAccount) {
		webClient = loadBalancerBankAccount.build();
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)	
	@HystrixCommand(fallbackMethod = "fallback_findAll", commandProperties = {
			   @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")
			})
	public Mono<UnitResult<BankAccount>> findAll() throws InterruptedException{		
		//Thread.sleep(3000);
		
		Mono<UnitResult<BankAccount>> result = webClient.get().uri("/")
		.retrieve().bodyToMono(new UnitResult<BankAccount>().getClassOfT());
		
		return result;
	}	
}