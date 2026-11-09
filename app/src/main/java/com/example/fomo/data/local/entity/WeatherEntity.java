package com.example.fomo.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Room database entity storing the primary weather forecast and current condition metrics.
 * Uses a single-row cache strategy (id = 1).
 */
@Entity(tableName = "weather_cache")
public class WeatherEntity {

    public static final int SINGLETON_CACHE_ID = 1;

    @PrimaryKey
    public int id = SINGLETON_CACHE_ID;

    public long cachedTimestamp;
    public String condition;
    public int temperature;
    public String dateTime;
    public String highLow;
    public String rainPercentage;
    public String windSpeed;
    public String humidityPercentage;
    public String iconKey;

    public WeatherEntity() {
    }

    @androidx.room.Ignore
    public WeatherEntity(int id, long cachedTimestamp, @NonNull String condition, int temperature,
                         @NonNull String dateTime, @NonNull String highLow,
                         @NonNull String rainPercentage, @NonNull String windSpeed,
                         @NonNull String humidityPercentage, @NonNull String iconKey) {
        this.id = id;
        this.cachedTimestamp = cachedTimestamp;
        this.condition = condition;
        this.temperature = temperature;
        this.dateTime = dateTime;
        this.highLow = highLow;
        this.rainPercentage = rainPercentage;
        this.windSpeed = windSpeed;
        this.humidityPercentage = humidityPercentage;
        this.iconKey = iconKey;
    }
}
