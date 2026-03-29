package com.example.assessmentddd.application.service;

import com.example.assessmentddd.application.dto.PriceResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PriceServiceIntegrationTest {

    @Autowired
    private PriceService priceService;

    @Sql(scripts = {"/01_test_data.sql"})
    @Test
    void test1_14h10_returnsPriceList1_35_50() {
        // 10:00 día 14 → priceList 1, 35.50 (priority 0)
        PriceResponse result = priceService.getPrice(
                LocalDateTime.parse("2020-06-14T10:00:00"), 35455, 1);

        assertThat(result.productId()).isEqualTo(35455);
        assertThat(result.brandId()).isEqualTo(1);
        assertThat(result.priceList()).isEqualTo(1);
        assertThat(result.price()).isEqualByComparingTo(new BigDecimal("35.50"));
    }

    @Sql(scripts = {"/01_test_data.sql"})
    @Test
    void test2_14h16_returnsPriceList2_25_45() {
        // 16:00 día 14 → priceList 2, 25.45 (priority 1, 15-18:30)
        PriceResponse result = priceService.getPrice(
                LocalDateTime.parse("2020-06-14T16:00:00"), 35455, 1);

        assertThat(result.priceList()).isEqualTo(2);
        assertThat(result.price()).isEqualByComparingTo(new BigDecimal("25.45"));
    }

}
