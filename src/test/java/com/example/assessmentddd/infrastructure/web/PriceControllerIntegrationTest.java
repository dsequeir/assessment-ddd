package com.example.assessmentddd.infrastructure.web;

import com.example.assessmentddd.application.dto.PriceRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PriceControllerIntegrationTest {

    public static final String PATH = "/api/v1/prices";
    public static final String BRAND_ID = "brandId";
    public static final String PRODUCT_ID = "productId";
    public static final String APPLICATION_DATE = "applicationDate";

    private final PriceRequest test_data1 = new PriceRequest("1", "35455", "2020-06-14T10:00:00");
    private final PriceRequest test_data2 = new PriceRequest("1", "35455", "2020-06-14T16:00:00");
    private final PriceRequest test_data3 = new PriceRequest("1", "35455", "2020-06-14T21:00:00");
    private final PriceRequest test_data4 = new PriceRequest("1", "35455", "2020-06-15T10:00:00");
    private final PriceRequest test_data5 = new PriceRequest("1", "35455", "2020-06-16T21:00:00");

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnPriceForTestData1() throws Exception {
        mockMvc.perform(get(PATH)
                        .param(BRAND_ID, test_data1.brandId())
                        .param(PRODUCT_ID, test_data1.productId())
                        .param(APPLICATION_DATE, test_data1.applicationDate()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.price").value(35.50));
    }

    @Test
    void shouldReturnPriceForTestData2() throws Exception {
        mockMvc.perform(get(PATH)
                        .param(BRAND_ID, test_data2.brandId())
                        .param(PRODUCT_ID, test_data2.productId())
                        .param(APPLICATION_DATE, test_data2.applicationDate()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.price").value(25.45));
    }

    @Test
    void shouldReturnPriceForTestData3() throws Exception {
        mockMvc.perform(get(PATH)
                        .param(BRAND_ID, test_data3.brandId())
                        .param(PRODUCT_ID, test_data3.productId())
                        .param(APPLICATION_DATE, test_data3.applicationDate()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.price").value(35.50));
    }

    @Test
    void shouldReturnPriceForTestData4() throws Exception {
        mockMvc.perform(get(PATH)
                        .param(BRAND_ID, test_data4.brandId())
                        .param(PRODUCT_ID, test_data4.productId())
                        .param(APPLICATION_DATE, test_data4.applicationDate()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.price").value(30.50));
    }

    @Test
    void shouldReturnPriceForTestData5() throws Exception {
        mockMvc.perform(get(PATH)
                        .param(BRAND_ID, test_data5.brandId())
                        .param(PRODUCT_ID, test_data5.productId())
                        .param(APPLICATION_DATE, test_data5.applicationDate()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.price").value(38.95));
    }
}
