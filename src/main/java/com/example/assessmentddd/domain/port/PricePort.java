package com.example.assessmentddd.domain.port;

import com.example.assessmentddd.domain.model.Price;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PricePort {
    Optional<Price> findByApplicationDateProductAndBrand(LocalDateTime date, Integer productId, Integer brandId);
}

