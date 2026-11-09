package com.example.fomo.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import com.example.fomo.data.local.entity.HourlyEntity;
import com.example.fomo.data.local.entity.WeatherEntity;
import java.util.List;

/**
 * Data Access Object (DAO) for weather cache tables with atomic transactional updates.
 */
@Dao
public interface WeatherDao {

    @Query("SELECT * FROM weather_cache WHERE id = 1 LIMIT 1")
    WeatherEntity getWeatherCache();

    @Query("SELECT * FROM hourly_forecast ORDER BY hourOrder ASC")
    List<HourlyEntity> getHourlyForecasts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWeatherCache(WeatherEntity entity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHourlyForecasts(List<HourlyEntity> entities);

    @Query("DELETE FROM hourly_forecast")
    void clearHourlyForecasts();

    @Query("DELETE FROM weather_cache")
    void clearWeatherCache();

    /**
     * Atomically replaces the cached weather forecast and hourly forecast collection.
     */
    @Transaction
    default void replaceCache(WeatherEntity weather, List<HourlyEntity> hourly) {
        clearWeatherCache();
        clearHourlyForecasts();
        insertWeatherCache(weather);
        if (hourly != null && !hourly.isEmpty()) {
            insertHourlyForecasts(hourly);
        }
    }
}
