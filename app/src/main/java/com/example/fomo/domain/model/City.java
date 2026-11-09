package com.example.fomo.domain.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Objects;

/**
 * Immutable domain model representing a geographic location in the multi-city catalog.
 */
public final class City {

    private final int id;
    private final String name;
    private final String country;
    private final String adminRegion;
    private final double latitude;
    private final double longitude;
    private final boolean isFavorite;
    private final long selectedAt;

    public City(int id, @NonNull String name, @NonNull String country,
                @Nullable String adminRegion, double latitude, double longitude,
                boolean isFavorite, long selectedAt) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.country = Objects.requireNonNull(country, "country must not be null");
        this.adminRegion = adminRegion;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isFavorite = isFavorite;
        this.selectedAt = selectedAt;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getCountry() {
        return country;
    }

    @Nullable
    public String getAdminRegion() {
        return adminRegion;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public long getSelectedAt() {
        return selectedAt;
    }

    /**
     * Formats a user-friendly display label (e.g. "Paris, France" or "Dallas, Texas, United States").
     */
    @NonNull
    public String getDisplayName() {
        if (adminRegion != null && !adminRegion.trim().isEmpty() && !adminRegion.equalsIgnoreCase(name)) {
            return name + ", " + adminRegion + ", " + country;
        }
        return name + ", " + country;
    }

    @NonNull
    public City withFavorite(boolean favorite) {
        return new City(id, name, country, adminRegion, latitude, longitude, favorite, selectedAt);
    }

    @NonNull
    public City withSelectedAt(long timestamp) {
        return new City(id, name, country, adminRegion, latitude, longitude, isFavorite, timestamp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City)) return false;
        City city = (City) o;
        return id == city.id &&
                Double.compare(city.latitude, latitude) == 0 &&
                Double.compare(city.longitude, longitude) == 0 &&
                isFavorite == city.isFavorite &&
                selectedAt == city.selectedAt &&
                name.equals(city.name) &&
                country.equals(city.country) &&
                Objects.equals(adminRegion, city.adminRegion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, country, adminRegion, latitude, longitude, isFavorite, selectedAt);
    }

    @NonNull
    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", isFavorite=" + isFavorite +
                '}';
    }
}

