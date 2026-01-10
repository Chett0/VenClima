package com.venclima.backend.service;

import com.venclima.model.DataStation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TideServiceTest {

    private RestTemplate restTemplate;
    String urlRealTimeData;

    @BeforeEach
    void setup() {
        this.restTemplate = new RestTemplate();
        this.urlRealTimeData = "https://dati.venezia.it/sites/default/files/dataset/opendata/livello.json";
    }

    @Test
    public void testSuccessfulRetrievalOpenDataRealTimeTides() {
        DataStation[] data = this.restTemplate.getForObject(this.urlRealTimeData, DataStation[].class);

        assertNotNull(data, "The retrieved DataStation array should not be null.");
        assertTrue(data.length > 0, "The data array should contain at least one element.");
    }

}
