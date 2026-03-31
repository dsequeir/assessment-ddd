package com.example.assessmentddd.application.usecase;

import com.example.assessmentddd.application.PriceResponseMapper;
import com.example.assessmentddd.application.dto.PriceResponse;
import com.example.assessmentddd.application.exception.PriceNotFoundException;
import com.example.assessmentddd.domain.pricing.policy.PriceSelector;
import com.example.assessmentddd.domain.pricing.port.PricePort;
import com.example.assessmentddd.shared.AssessmentConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Application Use Case responsible for retrieving the applicable price
 * based on business rules.
 *
 * <p>The Use Case consist of retrieving the applicable price
 * based on the pricing domain business rules .</p>
 */
@Service
public class GetPriceUseCase {

    private static final Logger log = LoggerFactory.getLogger(GetPriceUseCase.class);

    private final PricePort pricePort;

    private final PriceSelector priceSelector;

    private final PriceResponseMapper responseMapper;

    public GetPriceUseCase(PricePort pricePort, PriceSelector priceSelector, PriceResponseMapper responseMapper) {
        this.pricePort = pricePort;
        this.priceSelector = priceSelector;
        this.responseMapper = responseMapper;
    }

    @Cacheable(
            value = "prices-cache",
            key = "T(String).valueOf(#applicationDate) + ':' + #productId + ':' + #brandId"
    )
    public PriceResponse getPrice(LocalDateTime applicationDate, Integer productId, Integer brandId) {
        log.debug("Cache miss - resolving price for date={}, productId={}, brandId={}",
                applicationDate, productId, brandId);

        return Optional.ofNullable(pricePort.findApplicablePrices(applicationDate, productId, brandId))
                .filter(prices -> !prices.isEmpty())
                .flatMap(priceSelector::getApplicablePrice)
                .map(responseMapper::toResponse)
                .orElseThrow(() -> new PriceNotFoundException(AssessmentConstants.MSG_PRICE_NOT_FOUND));
    }
}
