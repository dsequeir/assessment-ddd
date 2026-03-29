package com.example.assessmentddd.infrastructure.persistence.adapter;

import com.example.assessmentddd.domain.model.Price;
import com.example.assessmentddd.domain.port.PricePort;
import com.example.assessmentddd.infrastructure.persistence.repository.SpringDataPriceRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public class PriceRepositoryJpaAdapter implements PricePort {

    private final SpringDataPriceRepository repository;

    public PriceRepositoryJpaAdapter(SpringDataPriceRepository repository) {
        this.repository = repository;
    }


    @Override
    public List<Price> findApplicablePrices(LocalDateTime date, Integer productId, Integer brandId) {
        return repository
                .findByStartDateLessThanEqualAndEndDateGreaterThanEqualAndProductIdAndBrandId(
                        date, date, productId, brandId
                );
    }
}
