package com.example.fomo.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.fomo.data.local.entity.CityEntity;
import java.util.List;

/**
 * Data Access Object (DAO) for managing saved and favorite cities.
 */
@Dao
public interface CityDao {

    @Query("SELECT * FROM saved_cities ORDER BY selectedAt DESC")
    List<CityEntity> getAllCities();

    @Query("SELECT * FROM saved_cities WHERE isFavorite = 1 ORDER BY name ASC")
    List<CityEntity> getFavoriteCities();

    @Query("SELECT * FROM saved_cities ORDER BY selectedAt DESC LIMIT 1")
    CityEntity getSelectedCity();

    @Query("SELECT * FROM saved_cities WHERE id = :id LIMIT 1")
    CityEntity getCityById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertCity(CityEntity city);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCities(List<CityEntity> cities);

    @Update
    void updateCity(CityEntity city);

    @Delete
    void deleteCity(CityEntity city);

    @Query("DELETE FROM saved_cities WHERE id = :id")
    void deleteCityById(int id);

    @Query("UPDATE saved_cities SET selectedAt = :timestamp WHERE id = :id")
    void markCitySelected(int id, long timestamp);

    @Query("SELECT COUNT(*) FROM saved_cities")
    int getCityCount();
}

