package com.example.assessmentddd.application.dto;

public record PriceRequest(
        String brandId,
        String productId,
        String applicationDate) {
}