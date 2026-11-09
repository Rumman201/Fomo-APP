package com.example.fomo.data.remote.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Data Transfer Object representing an individual geocoding search result from Open-Meteo.
 */
public class GeocodingResultDto {

    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String name;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("country")
    private String country;

    @SerializedName("country_code")
    private String countryCode;

    @SerializedName("admin1")
    private String admin1;

    @SerializedName("timezone")
    private String timezone;

    public GeocodingResultDto() {
    }

    public GeocodingResultDto(long id, String name, double latitude, double longitude,
                              String country, String admin1, String timezone) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.admin1 = admin1;
        this.timezone = timezone;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getAdmin1() {
        return admin1;
    }

    public String getTimezone() {
        return timezone;
    }
}

