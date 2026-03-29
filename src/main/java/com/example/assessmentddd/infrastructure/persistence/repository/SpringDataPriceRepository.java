package com.example.assessmentddd.infrastructure.persistence.repository;

import com.example.assessmentddd.domain.model.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface SpringDataPriceRepository extends JpaRepository<Price, Long> {

    Optional<Price> findFirstByStartDateLessThanEqualAndEndDateGreaterThanEqualAndProductIdAndBrandIdOrderByPriorityDesc(
            LocalDateTime dateFrom,
            LocalDateTime dateTo,
            Integer productId,
            Integer brandId
    );
}