package com.example.assessmentddd.application;

import com.example.assessmentddd.application.dto.PriceResponse;
import com.example.assessmentddd.domain.pricing.model.Price;
import org.springframework.stereotype.Component;

@Component
public class DefaultPriceResponseMapper implements  PriceResponseMapper{
    public PriceResponse toResponse(Price price) {
        return new PriceResponse(price.getProductId(),
                price.getBrandId(),
                price.getPriceList(),
                price.getStartDate(),
                price.getEndDate(),
                price.getPrice()
        );
    }

}
