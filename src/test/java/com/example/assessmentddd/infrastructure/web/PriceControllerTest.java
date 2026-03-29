package com.example.assessmentddd.infrastructure.web;

import com.example.assessmentddd.application.dto.PriceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class PriceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private com.example.assessmentddd.application.service.PriceService priceService;

    @Test
    void test1_14h10_expectPriceList1_35_50() throws Exception {
        // 10:00 día 14 → priority 0 → 35.50
        mockResponse(LocalDateTime.parse("2020-06-14T10:00:00"), 35455, 1,
                1, 35.50, "2020-06-14T00:00:00", "2020-12-31T23:59:59");

        mockMvc.perform(get("/api/v1/prices")
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.priceList").value(1))
                .andExpect(jsonPath("$.price").value(35.50));
    }

    @Test
    void test2_14h16_expectPriceList2_25_45() throws Exception {
        // 16:00 día 14 → priority 1 → 25.45 (15:00-18:30)
        mockResponse(LocalDateTime.parse("2020-06-14T16:00:00"), 35455, 1,
                2, 25.45, "2020-06-14T15:00:00", "2020-06-14T18:30:00");

        mockMvc.perform(get("/api/v1/prices")
                        .param("applicationDate", "2020-06-14T16:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceList").value(2))
                .andExpect(jsonPath("$.price").value(25.45));
    }

    @Test
    void test3_14h21_expectPriceList1_35_50() throws Exception {
        // 21:00 día 14 → fuera rango especial → priority 0 → 35.50
        mockResponse(LocalDateTime.parse("2020-06-14T21:00:00"), 35455, 1,
                1, 35.50, "2020-06-14T00:00:00", "2020-12-31T23:59:59");

        mockMvc.perform(get("/api/v1/prices")
                        .param("applicationDate", "2020-06-14T21:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceList").value(1));
    }

    @Test
    void test4_15h10_expectPriceList3_30_50() throws Exception {
        // 10:00 día 15 → priority 1 → 30.50 (00:00-11:00)
        mockResponse(LocalDateTime.parse("2020-06-15T10:00:00"), 35455, 1,
                3, 30.50, "2020-06-15T00:00:00", "2020-06-15T11:00:00");

        mockMvc.perform(get("/api/v1/prices")
                        .param("applicationDate", "2020-06-15T10:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceList").value(3));
    }

    @Test
    void test5_16h21_expectPriceList4_38_95() throws Exception {
        // 21:00 día 16 → priority 1 → 38.95 (16 16:00+)
        mockResponse(LocalDateTime.parse("2020-06-16T21:00:00"), 35455, 1,
                4, 38.95, "2020-06-15T16:00:00", "2020-12-31T23:59:59");

        mockMvc.perform(get("/api/v1/prices")
                        .param("applicationDate", "2020-06-16T21:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceList").value(4));
    }

    private void mockResponse(LocalDateTime date, Integer productId, Integer brandId,
                              Integer priceList, double priceValue, String start, String end) {
        PriceResponse response = new PriceResponse(
                productId, brandId, priceList,
                LocalDateTime.parse(start),
                LocalDateTime.parse(end),
                new BigDecimal(priceValue)
        );
        when(priceService.getPrice(date, productId, brandId)).thenReturn(response);
    }

    @Test
    @DisplayName("Should return 400 when applicationDate is empty")
    void shouldReturnBadRequestWhenApplicationDateIsEmpty() throws Exception {
        mockMvc.perform(get("/api/v1/prices")
                        .param("applicationDate", "")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(priceService);
    }

    @Test
    @DisplayName("Should return 400 when productId is empty")
    void shouldReturnBadRequestWhenProductIdIsEmpty() throws Exception {
        mockMvc.perform(get("/api/v1/prices")
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", "")
                        .param("brandId", "1"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(priceService);
    }

    @Test
    @DisplayName("Should return 400 when brandId is empty")
    void shouldReturnBadRequestWhenBrandIdIsEmpty() throws Exception {
        mockMvc.perform(get("/api/v1/prices")
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", "35455")
                        .param("brandId", ""))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(priceService);
    }
}