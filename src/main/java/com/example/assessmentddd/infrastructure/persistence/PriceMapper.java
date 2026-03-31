package com.example.assessmentddd.infrastructure.persistence;

import com.example.assessmentddd.domain.pricing.model.Price;
import com.example.assessmentddd.infrastructure.persistence.entity.PriceEntity;

public class PriceMapper {
    private PriceMapper() {
    }

    public static Price toDomain(PriceEntity entity) {
        return new Price(
                entity.getBrandId(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getPriceList(),
                entity.getProductId(),
                entity.getPriority(),
                entity.getPrice(),
                entity.getCurrency()
        );
    }

    public static PriceEntity toEntity(Price price) {
        return new PriceEntity(
                price.getBrandId(),
                price.getStartDate(),
                price.getEndDate(),
                price.getPriceList(),
                price.getProductId(),
                price.getPriority(),
                price.getPrice(),
                price.getCurrency()
        );
    }
}
