package com.example.assessmentddd.domain.pricing.policy;

import com.example.assessmentddd.domain.pricing.model.Price;

import java.util.List;
import java.util.Optional;

@FunctionalInterface
public interface PriceSelector {
    Optional<Price> getApplicablePrice(List<Price> prices);
}
