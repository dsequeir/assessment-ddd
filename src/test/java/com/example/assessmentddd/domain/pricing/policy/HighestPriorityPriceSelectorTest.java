package com.example.assessmentddd.domain.pricing.policy;

import com.example.assessmentddd.domain.pricing.model.Price;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HighestPriorityPriceSelectorTest {

    private final PriceSelector selector = new HighestPriorityPriceSelector();
    private static Price pricePriority1;
    private static Price pricePriority2;
    private static Price pricePriority3;

    @BeforeAll
    public static void setup() {
        pricePriority1 = new Price();
        pricePriority2 = new Price();
        pricePriority3 = new Price();
        pricePriority1.setProductId(1);
        pricePriority1.setPriority(1);
        pricePriority2.setProductId(2);
        pricePriority2.setPriority(2);
        pricePriority3.setProductId(3);
        pricePriority3.setPriority(3);
    }

    @Test
    @DisplayName("Should select price with highest priority")
    void shouldSelectPriceWithHighestPriority() {
        var prices = List.of(pricePriority1, pricePriority3, pricePriority2);

        var selected = selector.getApplicablePrice(prices);

        assertTrue(selected.isPresent());
        assertThat(selected)
                .isPresent()
                .get()
                .extracting(Price::getPriority)
                .isEqualTo(3);

    }

    @Test
    @DisplayName("Should return no price")
    void shouldReturnNoPrice() {
        var prices = List.<Price>of();

        assertFalse(selector.getApplicablePrice(prices).isPresent());
    }

}

