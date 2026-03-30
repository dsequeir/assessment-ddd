package com.example.assessmentddd.infrastructure.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.TimeUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.cache.type=hazelcast"
})
public class BaselinePerformanceTest {

    @Autowired
    private MockMvc mockMvc;

    private static final Logger log = LoggerFactory.getLogger(BaselinePerformanceTest.class);
    private static final int WARMUP_ITERATIONS = 20;
    private static final int MEASURE_ITERATIONS = 100;

    @Test
    @DisplayName("Should measure Performance")
    void shouldMeasurePerformance() throws Exception {

        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            executeRequest();
        }

        long start = System.nanoTime();

        for (int i = 0; i < MEASURE_ITERATIONS; i++) {
            executeRequest();
        }

        long end = System.nanoTime();

        long totalMs = TimeUnit.NANOSECONDS.toMillis(end - start);
        double avgMs = (double) totalMs / MEASURE_ITERATIONS;

        log.debug("==== PERFORMANCE (INDEX & CACHE) ====");
        log.debug("Total time: {} ms", totalMs);
        log.debug("Average time: {} ms", avgMs);
    }

    private void executeRequest() throws Exception {
        mockMvc.perform(get("/api/v1/prices")
                        .param("applicationDate", "2020-06-14T16:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isOk());
    }
}
