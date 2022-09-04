package pe.com.bootcamp.domain.fallback;

import pe.com.bootcamp.domain.aggregate.Card;
import pe.com.bootcamp.utilities.UnitResult;
import reactor.core.publisher.Mono;

public class BankCardFallback {
	Mono<UnitResult<Card>> fallback_findAll() {
		int code = 1002;
		String[] causes = new String[1];
		causes[0] = "Timeout expired";
		
		return Mono.just(new UnitResult<Card>(true,"Request fails. It takes long time to response", code, causes));			
	}
}
