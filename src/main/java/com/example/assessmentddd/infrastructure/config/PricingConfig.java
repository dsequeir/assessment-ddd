package com.example.assessmentddd.infrastructure.config;

import com.example.assessmentddd.domain.pricing.policy.HighestPriorityPriceSelector;
import com.example.assessmentddd.domain.pricing.policy.PriceSelector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PricingConfig {
    @Bean
    public PriceSelector priceSelector() {
        return new HighestPriorityPriceSelector();
    }
}
