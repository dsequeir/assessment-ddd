package com.example.assessmentddd.application.usecase;

import com.example.assessmentddd.application.dto.PriceResponse;
import com.example.assessmentddd.application.exception.PriceNotFoundException;
import com.example.assessmentddd.shared.AssessmentConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class GetPriceUseCaseIntegrationTest {

    @Autowired
    private GetPriceUseCase getPriceUseCase;

    @Sql(scripts = {"/01_test_data.sql"})
    @DisplayName("Should return 35.50 price for 2020-06-14 10:00")
    @Test
    void shouldReturnPrice35_50() {
        PriceResponse result = getPriceUseCase.getPrice(
                LocalDateTime.parse("2020-06-14T10:00:00"), 35455, 1);

        assertThat(result.productId()).isEqualTo(35455);
        assertThat(result.brandId()).isEqualTo(1);
        assertThat(result.priceList()).isEqualTo(1);
        assertThat(result.price()).isEqualByComparingTo(new BigDecimal("35.50"));
    }

    @Sql(scripts = {"/01_test_data.sql"})
    @DisplayName("Should return 25.45 price for 2020-06-14 16:00")
    @Test
    void shouldReturnPrice25_45() {
        PriceResponse result = getPriceUseCase.getPrice(
                LocalDateTime.parse("2020-06-14T16:00:00"), 35455, 1);

        assertThat(result.priceList()).isEqualTo(2);
        assertThat(result.price()).isEqualByComparingTo(new BigDecimal("25.45"));
    }

    @Sql(scripts = {"/01_test_data.sql"})
    @DisplayName("Should not return any price for 2020-06-10 16:00")
    @Test
    void shouldReturnNoPrice() {
        PriceNotFoundException exception = assertThrows(PriceNotFoundException.class, () ->
                getPriceUseCase.getPrice(
                        LocalDateTime.parse("2020-06-10T16:00:00"), 35455, 1));

        assertThat(exception.getMessage()).isEqualTo(AssessmentConstants.MSG_PRICE_NOT_FOUND);
    }

}
