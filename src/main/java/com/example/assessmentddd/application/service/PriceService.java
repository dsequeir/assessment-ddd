package com.example.assessmentddd.application.service;

import com.example.assessmentddd.application.dto.PriceResponse;
import com.example.assessmentddd.application.exception.PriceNotFoundException;
import com.example.assessmentddd.domain.model.PriceDto;
import com.example.assessmentddd.domain.port.PricePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;

/**
 * Application service responsible for retrieving the applicable price
 * based on business rules.
 *
 * <p>The service selects the price with the highest priority when multiple
 * prices are applicable for the same date range.</p>
 */
@Service
public class PriceService {

    private static final Logger log = LoggerFactory.getLogger(PriceService.class);

    private final PricePort pricePort;

    public PriceService(PricePort pricePort) {
        this.pricePort = pricePort;
    }

    @Cacheable(
            value = "prices-cache",
            key = "T(String).valueOf(#applicationDate) + ':' + #productId + ':' + #brandId"
    )
    public PriceResponse getPrice(LocalDateTime applicationDate, Integer productId, Integer brandId) {
        log.debug("Cache miss - resolving price for date={}, productId={}, brandId={}",
                applicationDate, productId, brandId);

        PriceDto selectedPrice = pricePort.findApplicablePrices(applicationDate, productId, brandId)
                .stream()
                .max(Comparator.comparingInt(PriceDto::getPriority))
                .orElseThrow(() -> {
                    log.warn("Price not found for productId={}, brandId={}, date={}",
                            productId, brandId, applicationDate);
                    return new PriceNotFoundException("No price found");
                });

        return new PriceResponse(
                selectedPrice.getProductId(),
                selectedPrice.getBrandId(),
                selectedPrice.getPriceList(),
                selectedPrice.getStartDate(),
                selectedPrice.getEndDate(),
                selectedPrice.getPrice()
        );
    }
}
