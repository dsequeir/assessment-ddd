package com.example.assessmentddd.infrastructure.web;

import com.example.assessmentddd.application.usecase.GetPriceUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PriceController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class PriceControllerExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GetPriceUseCase getPriceUseCase;

    @Test
    @DisplayName("Should return 400 when request parameter is missing")
    void shouldReturnBadRequestWhenRequestParameterIsMissing() throws Exception {
        mockMvc.perform(get("/api/v1/prices")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.name()));
    }

    @Test
    @DisplayName("Should return 400 when request parameter format is invalid")
    void shouldReturnBadRequestWhenRequestParameterFormatIsInvalid() throws Exception {
        mockMvc.perform(get("/api/v1/prices")
                        .param("applicationDate", "invalid-date")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when request parameter validation fails")
    void shouldReturnBadRequestWhenRequestParameterValidationFails() throws Exception {
        mockMvc.perform(get("/api/v1/prices")
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", "0")
                        .param("brandId", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 500 when unexpected error happens")
    void shouldReturnInternalServerErrorWhenUnexpectedErrorHappens() throws Exception {
        when(getPriceUseCase.getPrice(
                eq(LocalDateTime.of(2020, 6, 14, 10, 0, 0)),
                eq(35455),
                eq(1)))
                .thenThrow(new RuntimeException("Unexpected failure"));

        mockMvc.perform(get("/api/v1/prices")
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.message").value("Unexpected error"));
    }
}