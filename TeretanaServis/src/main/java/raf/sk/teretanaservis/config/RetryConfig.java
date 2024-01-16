package raf.sk.teretanaservis.config;

import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.crossstore.ChangeSetPersister;
@Configuration
public class RetryConfig {
    @Bean
    public Retry userTrainingRetry() {
        io.github.resilience4j.retry.RetryConfig retryConfig = io.github.resilience4j.retry.RetryConfig.custom()
                .intervalFunction(IntervalFunction.ofExponentialBackoff(2000, 2))
                .maxAttempts(5)
                .ignoreExceptions(ChangeSetPersister.NotFoundException.class)
                .build();

        RetryRegistry registry = RetryRegistry.of(retryConfig);

        return registry.retry("retryPattern");
    }
}
