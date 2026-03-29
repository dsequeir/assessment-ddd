package com.example.assessmentddd.infrastructure.persistence.adapter;

import com.example.assessmentddd.domain.model.Price;
import com.example.assessmentddd.infrastructure.persistence.repository.SpringDataPriceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Sql("/01_test_data.sql")
class SpringDataPriceRepositoryTest {

    @Autowired
    private SpringDataPriceRepository repository;

    @Test
    @DisplayName("should return highest priority applicable price")
    void shouldReturnHighestPriorityApplicablePrice() {
        List<Price> result =
                repository.findByStartDateLessThanEqualAndEndDateGreaterThanEqualAndProductIdAndBrandId(
                        LocalDateTime.of(2020, 6, 14, 16, 0),
                        LocalDateTime.of(2020, 6, 14, 16, 0),
                        35455,
                        1
                );

        assertThat(result).isNotEmpty();
        assertEquals(4, result.size());
        assertThat(result)
                .extracting(Price::getPriority)
                .containsExactlyInAnyOrder(0, 0, 1, 1);
    }

    @Test
    @DisplayName("should return empty when no applicable price exists")
    void shouldReturnEmptyWhenNoApplicablePriceExists() {
        List<Price> result =
                repository.findByStartDateLessThanEqualAndEndDateGreaterThanEqualAndProductIdAndBrandId(
                        LocalDateTime.of(2020, 6, 14, 10, 0),
                        LocalDateTime.of(2020, 6, 14, 10, 0),
                        99999,
                        1
                );

        assertThat(result).isEmpty();
    }
}