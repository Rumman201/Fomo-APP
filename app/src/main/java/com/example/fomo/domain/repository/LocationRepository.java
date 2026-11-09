package com.example.fomo.domain.repository;

import androidx.annotation.NonNull;
import com.example.fomo.domain.model.City;
import java.util.List;

/**
 * Domain repository contract managing multi-city persistence, favorite locations,
 * and remote geocoding searches.
 */
public interface LocationRepository {

    interface Callback<T> {
        void onSuccess(@NonNull T result);
        void onError(@NonNull Throwable error);
    }

    /**
     * Retrieves all saved cities ordered by recent selection.
     */
    void getSavedCitiesAsync(@NonNull Callback<List<City>> callback);

    /**
     * Retrieves favorite cities.
     */
    void getFavoriteCitiesAsync(@NonNull Callback<List<City>> callback);

    /**
     * Queries remote geocoding service for matching cities.
     */
    void searchCitiesAsync(@NonNull String query, @NonNull Callback<List<City>> callback);

    /**
     * Persists or updates a city in the local database.
     */
    void saveCityAsync(@NonNull City city, @NonNull Callback<City> callback);

    /**
     * Deletes a city from saved locations.
     */
    void deleteCityAsync(@NonNull City city, @NonNull Callback<Void> callback);

    /**
     * Marks a city as the currently active selected location.
     */
    void setSelectedCityAsync(@NonNull City city, @NonNull Callback<Void> callback);

    /**
     * Synchronously retrieves the currently selected city (for initial viewmodel setup).
     */
    @NonNull
    City getSelectedCitySync();
}

