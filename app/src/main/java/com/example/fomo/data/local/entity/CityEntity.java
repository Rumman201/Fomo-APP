package com.example.fomo.data.local.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Room entity representing a saved or favorite location in the multi-city catalog.
 * Uses b-tree indices on `isFavorite` and `selectedAt` to accelerate frequent lookups.
 */
@Entity(
        tableName = "saved_cities",
        indices = {
                @Index(value = {"isFavorite"}),
                @Index(value = {"selectedAt"})
        }
)
public class CityEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String name;

    @NonNull
    public String country;

    @Nullable
    public String adminRegion;

    public double latitude;
    public double longitude;
    public boolean isFavorite;
    public long selectedAt;

    public CityEntity() {
        this.name = "";
        this.country = "";
    }

    @androidx.room.Ignore
    public CityEntity(int id, @NonNull String name, @NonNull String country,
                      @Nullable String adminRegion, double latitude, double longitude,
                      boolean isFavorite, long selectedAt) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.adminRegion = adminRegion;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isFavorite = isFavorite;
        this.selectedAt = selectedAt;
    }
}
