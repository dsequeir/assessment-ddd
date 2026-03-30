package com.example.assessmentddd.infrastructure.persistence.repository;

import com.example.assessmentddd.infrastructure.persistence.entity.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SpringDataPriceRepository extends JpaRepository<PriceEntity, Long> {

    List<PriceEntity> findByStartDateLessThanEqualAndEndDateGreaterThanEqualAndProductIdAndBrandId(
            LocalDateTime dateFrom,
            LocalDateTime dateTo,
            Integer productId,
            Integer brandId
    );
}