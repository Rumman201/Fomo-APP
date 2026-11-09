package com.example.fomo.data.remote.dto;

import com.google.gson.annotations.SerializedName;
import java.util.Collections;
import java.util.List;

/**
 * Top-level response container for Open-Meteo Geocoding REST API searches.
 */
public class GeocodingResponse {

    @SerializedName("results")
    private List<GeocodingResultDto> results;

    @SerializedName("generationtime_ms")
    private double generationTimeMs;

    public GeocodingResponse() {
    }

    public GeocodingResponse(List<GeocodingResultDto> results) {
        this.results = results;
    }

    public List<GeocodingResultDto> getResults() {
        return results != null ? results : Collections.emptyList();
    }

    public double getGenerationTimeMs() {
        return generationTimeMs;
    }
}

