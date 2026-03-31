package com.example.assessmentddd.domain.pricing.port;

import com.example.assessmentddd.domain.pricing.model.Price;

import java.time.LocalDateTime;
import java.util.List;

public interface PricePort {
    List<Price> findApplicablePrices(LocalDateTime date, Integer productId, Integer brandId);
}

