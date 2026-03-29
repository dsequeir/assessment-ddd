package com.example.assessmentddd.domain.port;

import com.example.assessmentddd.domain.model.Price;

import java.time.LocalDateTime;
import java.util.List;

public interface PricePort {
    List<Price> findApplicablePrices(LocalDateTime date, Integer productId, Integer brandId);
}

