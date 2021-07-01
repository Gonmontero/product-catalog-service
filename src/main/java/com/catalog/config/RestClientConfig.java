package com.catalog.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.core.exception.RetryableException;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Configuration
public class RestClientConfig {

    public static final int MAX_RETRIES = 5;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(2000l);
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(MAX_RETRIES, getRetryableExceptions(), true);
        retryTemplate.setRetryPolicy(retryPolicy);
        return retryTemplate;
    }

    private Map<Class<? extends Throwable>, Boolean> getRetryableExceptions() {
        Map<Class<? extends Throwable>, Boolean> retryExceptions = new HashMap<>();
        retryExceptions.put(IllegalArgumentException.class, false);
        retryExceptions.put(RetryableException.class,true);
        retryExceptions.put(ParseException.class,false);
        retryExceptions.put(HttpClientErrorException.Unauthorized.class, true);
        retryExceptions.put(TimeoutException.class, true);

        return retryExceptions;
    }
}
