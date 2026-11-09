package com.example.fomo.ui.viewmodel;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.fomo.data.local.database.FomoDatabase;
import com.example.fomo.data.remote.api.ApiClient;
import com.example.fomo.data.repository.InMemoryWeatherRepository;
import com.example.fomo.data.repository.OfflineFirstWeatherRepository;
import com.example.fomo.data.repository.RoomLocationRepository;
import com.example.fomo.domain.model.AiWeatherAdvisory;
import com.example.fomo.domain.model.AutomationAlert;
import com.example.fomo.domain.model.City;
import com.example.fomo.domain.model.WeatherForecast;
import com.example.fomo.domain.repository.LocationRepository;
import com.example.fomo.domain.repository.WeatherRepository;
import com.example.fomo.domain.service.FeatureFlagService;
import com.example.fomo.domain.service.WeatherAiAdvisor;
import com.example.fomo.domain.service.WeatherAutomationEngine;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Presentation ViewModel managing weather data lifecycle, multi-city navigation,
 * AI atmospheric briefings, and intelligent automation alerts.
 */
public class WeatherViewModel extends ViewModel {

    private final WeatherRepository repository;
    private final LocationRepository locationRepository;
    private final WeatherAiAdvisor aiAdvisor;
    private final WeatherAutomationEngine automationEngine;
    private final FeatureFlagService featureFlagService;

    private final MutableLiveData<WeatherUiState> uiState = new MutableLiveData<>();
    private City currentCity;

    public WeatherViewModel() {
        this(new InMemoryWeatherRepository());
    }

    public WeatherViewModel(@NonNull WeatherRepository repository) {
        this(
                repository,
                null,
                new WeatherAiAdvisor(),
                new WeatherAutomationEngine(),
                new FeatureFlagService()
        );
    }

    public WeatherViewModel(@NonNull WeatherRepository repository,
                            @Nullable LocationRepository locationRepository,
                            @NonNull WeatherAiAdvisor aiAdvisor,
                            @NonNull WeatherAutomationEngine automationEngine,
                            @NonNull FeatureFlagService featureFlagService) {
        this.repository = Objects.requireNonNull(repository, "repository must not be null");
        this.locationRepository = locationRepository;
        this.aiAdvisor = Objects.requireNonNull(aiAdvisor, "aiAdvisor must not be null");
        this.automationEngine = Objects.requireNonNull(automationEngine, "automationEngine must not be null");
        this.featureFlagService = Objects.requireNonNull(featureFlagService, "featureFlagService must not be null");

        if (locationRepository != null) {
            this.currentCity = locationRepository.getSelectedCitySync();
        } else {
            this.currentCity = RoomLocationRepository.DEFAULT_CITY;
        }

        loadWeather();
    }

    @NonNull
    public LiveData<WeatherUiState> getUiState() {
        return uiState;
    }

    @NonNull
    public City getCurrentCity() {
        return currentCity != null ? currentCity : RoomLocationRepository.DEFAULT_CITY;
    }

    /**
     * Triggers loading of weather forecast data for the currently active city.
     */
    public void loadWeather() {
        uiState.setValue(WeatherUiState.loading(currentCity));

        WeatherRepository.Callback<WeatherForecast> callback = new WeatherRepository.Callback<WeatherForecast>() {
            @Override
            public void onSuccess(@NonNull WeatherForecast data) {
                AiWeatherAdvisory advisory = null;
                if (featureFlagService.isAiAdvisoryEnabled() && data.getCurrentWeather() != null) {
                    advisory = aiAdvisor.generateBriefing(data.getCurrentWeather());
                }

                List<AutomationAlert> alerts = Collections.emptyList();
                if (featureFlagService.isAutomationAlertsEnabled()) {
                    alerts = automationEngine.evaluateForecast(data);
                }

                uiState.setValue(WeatherUiState.success(data, currentCity, advisory, alerts));
            }

            @Override
            public void onError(@NonNull Throwable error) {
                String message = error.getMessage() != null ? error.getMessage() : "Unknown error occurred";
                uiState.setValue(WeatherUiState.error(message, currentCity));
            }
        };

        if (currentCity != null) {
            repository.getForecastForLocationAsync(currentCity.getLatitude(), currentCity.getLongitude(), callback);
        } else {
            repository.getForecastAsync(callback);
        }
    }

    /**
     * Switches the active city and triggers a forecast refresh for the new location.
     */
    public void selectCity(@NonNull City city) {
        this.currentCity = Objects.requireNonNull(city, "city must not be null");
        if (locationRepository != null) {
            locationRepository.setSelectedCityAsync(city, new LocationRepository.Callback<Void>() {
                @Override
                public void onSuccess(@NonNull Void result) {
                }

                @Override
                public void onError(@NonNull Throwable error) {
                }
            });
        }
        loadWeather();
    }

    /**
     * Searches cities via Geocoding API.
     */
    public void searchCities(@NonNull String query, @NonNull LocationRepository.Callback<List<City>> callback) {
        if (locationRepository != null) {
            locationRepository.searchCitiesAsync(query, callback);
        } else {
            callback.onSuccess(Collections.emptyList());
        }
    }

    /**
     * Retrieves saved cities list.
     */
    public void getSavedCities(@NonNull LocationRepository.Callback<List<City>> callback) {
        if (locationRepository != null) {
            locationRepository.getSavedCitiesAsync(callback);
        } else {
            callback.onSuccess(Collections.singletonList(RoomLocationRepository.DEFAULT_CITY));
        }
    }

    /**
     * Toggles favorite status for a city.
     */
    public void toggleFavorite(@NonNull City city, @NonNull LocationRepository.Callback<City> callback) {
        if (locationRepository != null) {
            City updated = city.withFavorite(!city.isFavorite());
            locationRepository.saveCityAsync(updated, callback);
        } else {
            callback.onSuccess(city);
        }
    }

    /**
     * Synchronous load helper, particularly useful for unit testing.
     */
    public void loadWeatherSync() {
        uiState.setValue(WeatherUiState.loading(currentCity));
        try {
            WeatherForecast forecast = repository.getSynchronousForecast();
            AiWeatherAdvisory advisory = aiAdvisor.generateBriefing(forecast.getCurrentWeather());
            List<AutomationAlert> alerts = automationEngine.evaluateForecast(forecast);
            uiState.setValue(WeatherUiState.success(forecast, currentCity, advisory, alerts));
        } catch (Exception e) {
            uiState.setValue(WeatherUiState.error(
                    e.getMessage() != null ? e.getMessage() : "Failed to load forecast",
                    currentCity
            ));
        }
    }

    /**
     * ViewModel Factory for instantiating WeatherViewModel with production repositories.
     */
    public static class Factory implements ViewModelProvider.Factory {
        private final Context applicationContext;

        public Factory(@NonNull Context context) {
            this.applicationContext = Objects.requireNonNull(context, "context must not be null").getApplicationContext();
        }

        @NonNull
        @Override
        @SuppressWarnings("unchecked")
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(WeatherViewModel.class)) {
                FomoDatabase database = FomoDatabase.getInstance(applicationContext);
                WeatherRepository weatherRepo = new OfflineFirstWeatherRepository(
                        ApiClient.getInstance(),
                        database.weatherDao()
                );
                LocationRepository locationRepo = new RoomLocationRepository(
                        database.cityDao(),
                        ApiClient.getGeocodingService()
                );
                WeatherAiAdvisor aiAdvisor = new WeatherAiAdvisor();
                WeatherAutomationEngine automationEngine = new WeatherAutomationEngine(
                        database.automationDao(),
                        null
                );
                FeatureFlagService featureFlags = new FeatureFlagService(applicationContext);

                return (T) new WeatherViewModel(
                        weatherRepo,
                        locationRepo,
                        aiAdvisor,
                        automationEngine,
                        featureFlags
                );
            }
            throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
        }
    }
}
