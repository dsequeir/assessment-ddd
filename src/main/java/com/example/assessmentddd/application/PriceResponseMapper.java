package com.example.assessmentddd.application;

import com.example.assessmentddd.application.dto.PriceResponse;
import com.example.assessmentddd.domain.pricing.model.Price;

public interface PriceResponseMapper {

    PriceResponse toResponse(Price price);
}
