package com.example.fomo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.example.fomo.data.remote.dto.GeocodingResponse;
import com.example.fomo.data.remote.dto.GeocodingResultDto;
import com.google.gson.Gson;
import java.util.List;
import org.junit.Test;

public class GeocodingDtoTest {

    @Test
    public void testOpenMeteoGeocodingJsonDeserialization() {
        String json = "{\n" +
                "  \"results\": [\n" +
                "    {\n" +
                "      \"id\": 5128581,\n" +
                "      \"name\": \"New York\",\n" +
                "      \"latitude\": 40.71427,\n" +
                "      \"longitude\": -74.00597,\n" +
                "      \"elevation\": 10.0,\n" +
                "      \"feature_code\": \"PPLA2\",\n" +
                "      \"country_code\": \"US\",\n" +
                "      \"country\": \"United States\",\n" +
                "      \"admin1\": \"New York\",\n" +
                "      \"timezone\": \"America/New_York\",\n" +
                "      \"population\": 8804190\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 2988507,\n" +
                "      \"name\": \"Paris\",\n" +
                "      \"latitude\": 48.85341,\n" +
                "      \"longitude\": 2.3488,\n" +
                "      \"country\": \"France\",\n" +
                "      \"admin1\": \"Ile-de-France\",\n" +
                "      \"timezone\": \"Europe/Paris\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"generationtime_ms\": 0.62\n" +
                "}";

        Gson gson = new Gson();
        GeocodingResponse response = gson.fromJson(json, GeocodingResponse.class);

        assertNotNull(response);
        assertEquals(0.62, response.getGenerationTimeMs(), 0.001);
        List<GeocodingResultDto> results = response.getResults();
        assertEquals(2, results.size());

        GeocodingResultDto ny = results.get(0);
        assertEquals(5128581L, ny.getId());
        assertEquals("New York", ny.getName());
        assertEquals(40.71427, ny.getLatitude(), 0.0001);
        assertEquals(-74.00597, ny.getLongitude(), 0.0001);
        assertEquals("United States", ny.getCountry());
        assertEquals("New York", ny.getAdmin1());
        assertEquals("America/New_York", ny.getTimezone());

        GeocodingResultDto paris = results.get(1);
        assertEquals("Paris", paris.getName());
        assertEquals("France", paris.getCountry());
        assertEquals("Europe/Paris", paris.getTimezone());
    }

    @Test
    public void testEmptyResultsHandling() {
        String emptyJson = "{\"generationtime_ms\": 0.12}";
        Gson gson = new Gson();
        GeocodingResponse response = gson.fromJson(emptyJson, GeocodingResponse.class);

        assertNotNull(response);
        assertNotNull(response.getResults());
        assertTrue(response.getResults().isEmpty());
    }
}

