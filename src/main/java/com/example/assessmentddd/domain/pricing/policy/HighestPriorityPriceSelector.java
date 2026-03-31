package com.example.assessmentddd.domain.pricing.policy;

import com.example.assessmentddd.domain.pricing.model.Price;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class HighestPriorityPriceSelector implements PriceSelector {
    @Override
    public Optional<Price> getApplicablePrice(List<Price> prices) {
        return prices.stream()
                .max(Comparator.comparingInt(Price::getPriority));

    }
}

