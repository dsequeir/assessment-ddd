package com.example.assessmentddd.infrastructure.persistence.repository;

import com.example.assessmentddd.domain.model.Price;
import com.example.assessmentddd.infrastructure.persistence.PriceMapper;
import com.example.assessmentddd.infrastructure.persistence.adapter.PriceRepositoryJpaAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PriceRepositoryJpaAdapterTest {

    private SpringDataPriceRepository repository;
    private PriceRepositoryJpaAdapter adapter;

    @BeforeEach
    void setUp() {
        repository = mock(SpringDataPriceRepository.class);
        adapter = new PriceRepositoryJpaAdapter(repository);
    }

    @Test
    @DisplayName("Should return the applicable price returned by the repository")
    void shouldReturnApplicablePrice() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0);

        Price price = new Price();
        price.setBrandId(1);
        price.setProductId(35455);
        price.setPriceList(2);
        price.setPriority(1);

        when(repository.findByStartDateLessThanEqualAndEndDateGreaterThanEqualAndProductIdAndBrandId(
                applicationDate,
                applicationDate,
                35455,
                1
        )).thenReturn(List.of(PriceMapper.toEntity(price)));

        List<Price> result = adapter.findApplicablePrices(applicationDate, 35455, 1);

        assertThat(result).isNotEmpty();
        assertThat(result.getFirst().getPrice()).isEqualTo(price.getPrice());
        assertThat(result.getFirst().getProductId()).isEqualTo(price.getProductId());
        assertThat(result.getFirst().getBrandId()).isEqualTo(price.getBrandId());

        verify(repository).findByStartDateLessThanEqualAndEndDateGreaterThanEqualAndProductIdAndBrandId(
                applicationDate,
                applicationDate,
                35455,
                1
        );
    }

    @Test
    @DisplayName("Should return empty when repository finds no applicable price")
    void shouldReturnEmptyWhenRepositoryReturnsEmpty() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);

        when(repository.findByStartDateLessThanEqualAndEndDateGreaterThanEqualAndProductIdAndBrandId(
                applicationDate,
                applicationDate,
                35455,
                1
        )).thenReturn(Collections.emptyList());

        List<Price> result = adapter.findApplicablePrices(applicationDate, 35455, 1);

        assertThat(result).isEmpty();

        verify(repository).findByStartDateLessThanEqualAndEndDateGreaterThanEqualAndProductIdAndBrandId(
                applicationDate,
                applicationDate,
                35455,
                1
        );
    }
}