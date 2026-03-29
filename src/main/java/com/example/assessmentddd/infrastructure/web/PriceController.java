package com.example.assessmentddd.infrastructure.web;

import com.example.assessmentddd.application.dto.PriceResponse;
import com.example.assessmentddd.application.service.PriceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
/**
 * REST controller exposing price GET endpoint.
 *
 * <p>This controller receives HTTP requests and delegates the business logic
 * to the application layer, returning the applicable price based on
 * product, brand and date.</p>
 */
@RestController
@RequestMapping("/api/v1/prices")
public class PriceController {

    private static final Logger log = LoggerFactory.getLogger(PriceController.class);

    private final PriceService priceService;

    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    /**
     * Retrieves the applicable price for a given product, brand and date.
     *
     * @param applicationDate the date when the price should be applied
     * @param productId the product identifier
     * @param brandId the brand identifier
     * @return the applicable price information
     */
    @Operation(
            summary = "Get applicable price",
            description = "Returns the applicable price for a product and brand at a given application date"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Applicable price found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PriceResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request parameters",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                        {
                                          "status": 400,
                                          "error": "BAD_REQUEST",
                                          "message": "applicationDate must be a valid ISO-8601 date-time",
                                          "path": "/api/v1/prices",
                                          "timestamp": "2026-03-26T10:15:30"
                                        }
                                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No applicable price found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                        {
                                          "status": 404,
                                          "error": "PRICE_NOT_FOUND",
                                          "message": "No applicable price found for the given criteria",
                                          "path": "/api/v1/prices",
                                          "timestamp": "2026-03-26T10:15:30"
                                        }
                                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @GetMapping
    @Validated
    public ResponseEntity<PriceResponse> getPrice(
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate,
            @RequestParam @NotNull @Positive Integer productId,
            @RequestParam @NotNull @Positive  Integer brandId) {
        log.info("GET /api/v1/prices - productId={}, brandId={}, date={}",
                productId, brandId, applicationDate);
        return ResponseEntity.ok(priceService.getPrice(applicationDate, productId, brandId));
    }
}
