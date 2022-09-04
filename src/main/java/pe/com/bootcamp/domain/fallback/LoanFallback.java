package pe.com.bootcamp.domain.fallback;

import pe.com.bootcamp.domain.aggregate.Loan;
import pe.com.bootcamp.utilities.UnitResult;
import reactor.core.publisher.Mono;

public class LoanFallback {
	Mono<UnitResult<Loan>> fallback_findAll() {
		int code = 1002;
		String[] causes = new String[1];
		causes[0] = "Timeout expired";
		
		return Mono.just(new UnitResult<Loan>(true,"Request fails. It takes long time to response", code, causes));			
	}
}
