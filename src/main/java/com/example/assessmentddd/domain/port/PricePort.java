package com.example.assessmentddd.domain.port;

import com.example.assessmentddd.domain.model.PriceDto;

import java.time.LocalDateTime;
import java.util.List;

public interface PricePort {
    List<PriceDto> findApplicablePrices(LocalDateTime date, Integer productId, Integer brandId);
}

