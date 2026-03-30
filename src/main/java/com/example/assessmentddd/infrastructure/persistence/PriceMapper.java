package com.example.assessmentddd.infrastructure.persistence;

import com.example.assessmentddd.domain.model.PriceDto;
import com.example.assessmentddd.infrastructure.persistence.entity.PriceEntity;

public class PriceMapper {
    private PriceMapper() {
    }

    public static PriceDto toDomain(PriceEntity entity) {
        return new PriceDto(
                entity.getId(),
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

    public static PriceEntity toEntity(PriceDto price) {
        return new PriceEntity(
                price.getId(),
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
