package com.example.assessmentddd.infrastructure.persistence.repository;

import com.example.assessmentddd.domain.model.Price;
import com.example.assessmentddd.infrastructure.persistence.adapter.PriceRepositoryJpaAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

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

        when(repository.findFirstByStartDateLessThanEqualAndEndDateGreaterThanEqualAndProductIdAndBrandIdOrderByPriorityDesc(
                applicationDate,
                applicationDate,
                35455,
                1
        )).thenReturn(Optional.of(price));

        Optional<Price> result = adapter.findByApplicationDateProductAndBrand(applicationDate, 35455, 1);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(price);

        verify(repository).findFirstByStartDateLessThanEqualAndEndDateGreaterThanEqualAndProductIdAndBrandIdOrderByPriorityDesc(
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

        when(repository.findFirstByStartDateLessThanEqualAndEndDateGreaterThanEqualAndProductIdAndBrandIdOrderByPriorityDesc(
                applicationDate,
                applicationDate,
                35455,
                1
        )).thenReturn(Optional.empty());

        Optional<Price> result = adapter.findByApplicationDateProductAndBrand(applicationDate, 35455, 1);

        assertThat(result).isEmpty();

        verify(repository).findFirstByStartDateLessThanEqualAndEndDateGreaterThanEqualAndProductIdAndBrandIdOrderByPriorityDesc(
                applicationDate,
                applicationDate,
                35455,
                1
        );
    }
}