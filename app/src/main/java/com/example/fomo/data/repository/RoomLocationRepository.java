package com.example.fomo.data.repository;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import com.example.fomo.core.concurrency.AppExecutors;
import com.example.fomo.data.local.dao.CityDao;
import com.example.fomo.data.local.entity.CityEntity;
import com.example.fomo.data.local.mapper.CityMapper;
import com.example.fomo.data.remote.api.GeocodingApiService;
import com.example.fomo.data.remote.dto.GeocodingResponse;
import com.example.fomo.domain.model.City;
import com.example.fomo.domain.repository.LocationRepository;
import com.example.fomo.security.sanitizer.InputSanitizer;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import retrofit2.Response;

/**
 * Production implementation of LocationRepository backed by Room local persistence
 * and Open-Meteo Geocoding REST API.
 */
public class RoomLocationRepository implements LocationRepository {

    public static final City DEFAULT_CITY = new City(
            1, "New York", "United States", "New York",
            40.7128, -74.0060, true, 1
    );

    private final CityDao cityDao;
    private final GeocodingApiService geocodingApiService;
    private final Executor workerExecutor;
    private final Executor mainExecutor;

    public RoomLocationRepository(@NonNull CityDao cityDao,
                                  @NonNull GeocodingApiService geocodingApiService) {
        this(
                cityDao,
                geocodingApiService,
                AppExecutors.getInstance().diskIO(),
                AppExecutors.getInstance().mainThread()
        );
    }

    public RoomLocationRepository(@NonNull CityDao cityDao,
                                  @NonNull GeocodingApiService geocodingApiService,
                                  @NonNull Executor workerExecutor,
                                  @NonNull Executor mainExecutor) {
        this.cityDao = Objects.requireNonNull(cityDao, "cityDao must not be null");
        this.geocodingApiService = Objects.requireNonNull(geocodingApiService, "geocodingApiService must not be null");
        this.workerExecutor = Objects.requireNonNull(workerExecutor, "workerExecutor must not be null");
        this.mainExecutor = Objects.requireNonNull(mainExecutor, "mainExecutor must not be null");
    }

    @Override
    public void getSavedCitiesAsync(@NonNull Callback<List<City>> callback) {
        Objects.requireNonNull(callback, "callback must not be null");
        workerExecutor.execute(() -> {
            try {
                List<CityEntity> entities = cityDao.getAllCities();
                if (entities.isEmpty()) {
                    // Seed default city
                    cityDao.insertCity(CityMapper.toEntity(DEFAULT_CITY));
                    entities = cityDao.getAllCities();
                }
                List<City> domainList = CityMapper.toDomainList(entities);
                mainExecutor.execute(() -> callback.onSuccess(domainList));
            } catch (Exception e) {
                mainExecutor.execute(() -> callback.onError(e));
            }
        });
    }

    @Override
    public void getFavoriteCitiesAsync(@NonNull Callback<List<City>> callback) {
        Objects.requireNonNull(callback, "callback must not be null");
        workerExecutor.execute(() -> {
            try {
                List<CityEntity> entities = cityDao.getFavoriteCities();
                List<City> domainList = CityMapper.toDomainList(entities);
                mainExecutor.execute(() -> callback.onSuccess(domainList));
            } catch (Exception e) {
                mainExecutor.execute(() -> callback.onError(e));
            }
        });
    }

    @Override
    public void searchCitiesAsync(@NonNull String query, @NonNull Callback<List<City>> callback) {
        Objects.requireNonNull(callback, "callback must not be null");
        String sanitizedQuery = InputSanitizer.sanitizeSearchQuery(query);
        if (sanitizedQuery.isEmpty()) {
            callback.onSuccess(Collections.emptyList());
            return;
        }

        workerExecutor.execute(() -> {
            try {
                Response<GeocodingResponse> response = geocodingApiService.searchCities(
                        sanitizedQuery,
                        GeocodingApiService.DEFAULT_SEARCH_COUNT,
                        GeocodingApiService.DEFAULT_LANGUAGE,
                        GeocodingApiService.DEFAULT_FORMAT
                ).execute();

                if (response.isSuccessful() && response.body() != null) {
                    List<City> results = CityMapper.fromGeocodingDtoList(response.body().getResults());
                    mainExecutor.execute(() -> callback.onSuccess(results));
                } else {
                    mainExecutor.execute(() -> callback.onError(
                            new IOException("Geocoding search failed with HTTP " + response.code())
                    ));
                }
            } catch (Exception e) {
                mainExecutor.execute(() -> callback.onError(e));
            }
        });
    }

    @Override
    public void saveCityAsync(@NonNull City city, @NonNull Callback<City> callback) {
        Objects.requireNonNull(city, "city must not be null");
        Objects.requireNonNull(callback, "callback must not be null");

        if (!InputSanitizer.isValidCoordinate(city.getLatitude(), city.getLongitude())) {
            callback.onError(new IllegalArgumentException("Invalid geographic coordinates for city: " + city.getName()));
            return;
        }

        workerExecutor.execute(() -> {
            try {
                CityEntity entity = CityMapper.toEntity(city);
                long newId = cityDao.insertCity(entity);
                City savedCity = new City(
                        (int) newId,
                        city.getName(),
                        city.getCountry(),
                        city.getAdminRegion(),
                        city.getLatitude(),
                        city.getLongitude(),
                        city.isFavorite(),
                        city.getSelectedAt()
                );
                mainExecutor.execute(() -> callback.onSuccess(savedCity));
            } catch (Exception e) {
                mainExecutor.execute(() -> callback.onError(e));
            }
        });
    }

    @Override
    public void deleteCityAsync(@NonNull City city, @NonNull Callback<Void> callback) {
        Objects.requireNonNull(city, "city must not be null");
        Objects.requireNonNull(callback, "callback must not be null");

        workerExecutor.execute(() -> {
            try {
                cityDao.deleteCityById(city.getId());
                mainExecutor.execute(() -> callback.onSuccess(null));
            } catch (Exception e) {
                mainExecutor.execute(() -> callback.onError(e));
            }
        });
    }

    @Override
    public void setSelectedCityAsync(@NonNull City city, @NonNull Callback<Void> callback) {
        Objects.requireNonNull(city, "city must not be null");
        Objects.requireNonNull(callback, "callback must not be null");

        workerExecutor.execute(() -> {
            try {
                long now = System.currentTimeMillis();
                CityEntity existing = cityDao.getCityById(city.getId());
                if (existing == null) {
                    CityEntity toInsert = CityMapper.toEntity(city.withSelectedAt(now));
                    cityDao.insertCity(toInsert);
                } else {
                    cityDao.markCitySelected(city.getId(), now);
                }
                mainExecutor.execute(() -> callback.onSuccess(null));
            } catch (Exception e) {
                mainExecutor.execute(() -> callback.onError(e));
            }
        });
    }

    @NonNull
    @Override
    public City getSelectedCitySync() {
        try {
            CityEntity entity = cityDao.getSelectedCity();
            if (entity != null) {
                City domain = CityMapper.toDomain(entity);
                if (domain != null) {
                    return domain;
                }
            }
        } catch (Exception ignored) {
        }
        return DEFAULT_CITY;
    }

    private static class HandlerExecutor implements Executor {
        private final Handler handler;

        HandlerExecutor(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void execute(Runnable command) {
            handler.post(command);
        }
    }
}
