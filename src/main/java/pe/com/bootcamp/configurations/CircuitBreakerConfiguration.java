package pe.com.bootcamp.configurations;

import java.time.Duration;

import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

@Configuration
public class CircuitBreakerConfiguration {			
			
	@Bean	
    Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .circuitBreakerConfig(CircuitBreakerConfig
                		.custom()
                		.slidingWindowSize(5)
						.slidingWindowType(CircuitBreakerConfig.SlidingWindowType.TIME_BASED)
						.minimumNumberOfCalls(3)
						.waitDurationInOpenState(Duration.ofSeconds(5))
						.failureRateThreshold(25)
						.build())
                .timeLimiterConfig(TimeLimiterConfig.custom()
                		.timeoutDuration(Duration.ofSeconds(1))
                		.build())
                .build());
    }
}