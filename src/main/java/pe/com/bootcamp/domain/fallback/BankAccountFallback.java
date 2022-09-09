package pe.com.bootcamp.domain.fallback;

import pe.com.bootcamp.domain.aggregate.BankAccount;
import pe.com.bootcamp.utilities.ResultBase;
import pe.com.bootcamp.utilities.UnitResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class BankAccountFallback {
	
	Mono<UnitResult<BankAccount>> fallback_create(BankAccount entity){
		int code = 1002;
		String[] causes = new String[] { "Timeout expired" };				
		return Mono.just(new UnitResult<BankAccount>(true,"Request fails. It takes long time to response", code, causes));			
	}
	
	Mono<UnitResult<BankAccount>> fallback_update(BankAccount entity){
		int code = 1002;
		String[] causes = new String[] { "Timeout expired" };				
		return Mono.just(new UnitResult<BankAccount>(true,"Request fails. It takes long time to response", code, causes));			
	}
	
	Mono<UnitResult<BankAccount>> fallback_saveAll(Flux<BankAccount> entities){
		int code = 1002;
		String[] causes = new String[] { "Timeout expired" };				
		return Mono.just(new UnitResult<BankAccount>(true,"Request fails. It takes long time to response", code, causes));			
	}
	
	Mono<UnitResult<BankAccount>> fallback_findById(String id){
		int code = 1002;
		String[] causes = new String[] { "Timeout expired" };				
		return Mono.just(new UnitResult<BankAccount>(true,"Request fails. It takes long time to response", code, causes));			
	}
	
	Mono<UnitResult<BankAccount>> fallback_findByClientIdentNum(String dni){
		int code = 1002;
		String[] causes = new String[] { "Timeout expired" };				
		return Mono.just(new UnitResult<BankAccount>(true,"Request fails. It takes long time to response", code, causes));			
	}
	
	Mono<UnitResult<BankAccount>> fallback_findByAccountNumber(String accountNumber){
		int code = 1002;
		String[] causes = new String[] { "Timeout expired" };				
		return Mono.just(new UnitResult<BankAccount>(true,"Request fails. It takes long time to response", code, causes));			
	}
	
	Mono<UnitResult<BankAccount>> fallback_findAll(){
		int code = 1002;
		String[] causes = new String[] { "Timeout expired" };				
		return Mono.just(new UnitResult<BankAccount>(true,"Request fails. It takes long time to response", code, causes));			
	}
	
	Flux<BankAccount> fallback_findAllStreaming() throws Exception{
		throw new Exception("Timeout expired");			
	}
	
	Mono<ResultBase> fallback_deleteById(String id){
		int code = 1002;
		String[] causes = new String[] { "Timeout expired" };				
		return Mono.just(new UnitResult<BankAccount>(true,"Request fails. It takes long time to response", code, causes));			
	}
}
