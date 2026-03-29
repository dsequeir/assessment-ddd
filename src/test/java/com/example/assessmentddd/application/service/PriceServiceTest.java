package com.example.assessmentddd.application.service;

import com.example.assessmentddd.application.dto.PriceResponse;
import com.example.assessmentddd.application.exception.PriceNotFoundException;
import com.example.assessmentddd.domain.model.Price;
import com.example.assessmentddd.domain.port.PricePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriceServiceTest {

    @Mock
    private PricePort pricePort;

    @InjectMocks
    private PriceService priceService;

    private Price testPrice;

    @BeforeEach
    void setUp() {
        testPrice = new Price();
        testPrice.setBrandId(1);
        testPrice.setProductId(35455);
        testPrice.setPriceList(1);
        testPrice.setPriority(0);
        testPrice.setPrice(new BigDecimal("35.50"));
        testPrice.setStartDate(LocalDateTime.parse("2020-06-14T00:00:00"));
        testPrice.setEndDate(LocalDateTime.parse("2020-12-31T23:59:59"));
    }

    @Test
    void getPrice_found_returnsResponse() {

        LocalDateTime date = LocalDateTime.parse("2020-06-14T10:00:00");
        when(pricePort.findApplicablePrices(date, 35455, 1))
                .thenReturn(List.of(testPrice));

        PriceResponse response = priceService.getPrice(date, 35455, 1);

        assertNotNull(response);
        assertEquals(35455, response.productId());
        assertEquals(new BigDecimal("35.50"), response.price());
        verify(pricePort).findApplicablePrices(date, 35455, 1);
    }

    @Test
    void getPrice_notFound_throwsPriceNotFoundException() {
        LocalDateTime date = LocalDateTime.parse("2020-01-01T10:00:00");
        when(pricePort.findApplicablePrices(date, 99999, 999))
                .thenReturn(Collections.emptyList());

        PriceNotFoundException exception = assertThrows(PriceNotFoundException.class,
                () -> priceService.getPrice(date, 99999, 999));

        assertTrue(exception.getMessage().contains("No price"));
        verify(pricePort).findApplicablePrices(date, 99999, 999);
    }

    @Test
    void getPrice_callsPortOnce() {

        LocalDateTime date = LocalDateTime.parse("2020-06-14T10:00:00");
        when(pricePort.findApplicablePrices(any(), anyInt(), anyInt()))
                .thenReturn(List.of(testPrice));

        priceService.getPrice(date, 35455, 1);

        verify(pricePort, times(1)).findApplicablePrices(date, 35455, 1);
        verifyNoMoreInteractions(pricePort);
    }
}