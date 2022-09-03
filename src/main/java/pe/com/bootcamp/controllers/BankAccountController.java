package pe.com.bootcamp.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import pe.com.bootcamp.utilities.UnitResult;

@RestController
@RequestMapping("/BankAccount")
public class BankAccountController {

	@RequestMapping(value = "/")
	@HystrixCommand(fallbackMethod = "fallback_hello", commandProperties = {
			   @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")
			})
	public UnitResult<String> hello() throws InterruptedException {
	   Thread.sleep(3000);
	   return new UnitResult<String>(false,"Welcome Hystrix");
	}
	public UnitResult<String> fallback_hello() {
		int code = 1002;
		String[] causes = new String[1];
		causes[0] = "Timeout expired";
		
		return new UnitResult<String>(true,"Request fails. It takes long time to response", code, causes) {};
	}
}