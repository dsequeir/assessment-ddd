package com.example.assessmentddd.infrastructure.persistence.adapter;

import com.example.assessmentddd.domain.model.Price;
import com.example.assessmentddd.infrastructure.persistence.repository.SpringDataPriceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql("/01_test_data.sql")
class SpringDataPriceRepositoryTest {

    @Autowired
    private SpringDataPriceRepository repository;

    @Test
    @DisplayName("should return highest priority applicable price")
    void shouldReturnHighestPriorityApplicablePrice() {
        Optional<Price> result =
                repository.findFirstByStartDateLessThanEqualAndEndDateGreaterThanEqualAndProductIdAndBrandIdOrderByPriorityDesc(
                        LocalDateTime.of(2020, 6, 14, 16, 0),
                        LocalDateTime.of(2020, 6, 14, 16, 0),
                        35455,
                        1
                );

        assertThat(result).isPresent();
        assertThat(result.get().getPriceList()).isEqualTo(2);
        assertThat(result.get().getPriority()).isEqualTo(1);
        assertThat(result.get().getPrice()).isEqualByComparingTo("25.45");
    }

    @Test
    @DisplayName("should return empty when no applicable price exists")
    void shouldReturnEmptyWhenNoApplicablePriceExists() {
        Optional<Price> result =
                repository.findFirstByStartDateLessThanEqualAndEndDateGreaterThanEqualAndProductIdAndBrandIdOrderByPriorityDesc(
                        LocalDateTime.of(2020, 6, 14, 10, 0),
                        LocalDateTime.of(2020, 6, 14, 10, 0),
                        99999,
                        1
                );

        assertThat(result).isEmpty();
    }
}