package com.example.fomo.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.fomo.domain.model.AiWeatherAdvisory;
import com.example.fomo.domain.model.AutomationAlert;
import com.example.fomo.domain.model.City;
import com.example.fomo.domain.model.WeatherForecast;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Immutable representation of the weather dashboard UI state.
 * Encapsulates meteorological data, current location, AI briefing, and active automation alerts.
 */
public final class WeatherUiState {

    public enum Status {
        LOADING,
        SUCCESS,
        ERROR
    }

    @NonNull
    private final Status status;
    @Nullable
    private final WeatherForecast data;
    @Nullable
    private final City selectedCity;
    @Nullable
    private final AiWeatherAdvisory aiAdvisory;
    @NonNull
    private final List<AutomationAlert> activeAlerts;
    @Nullable
    private final String errorMessage;

    private WeatherUiState(@NonNull Status status,
                           @Nullable WeatherForecast data,
                           @Nullable City selectedCity,
                           @Nullable AiWeatherAdvisory aiAdvisory,
                           @Nullable List<AutomationAlert> activeAlerts,
                           @Nullable String errorMessage) {
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.data = data;
        this.selectedCity = selectedCity;
        this.aiAdvisory = aiAdvisory;
        this.activeAlerts = activeAlerts != null ? Collections.unmodifiableList(activeAlerts) : Collections.emptyList();
        this.errorMessage = errorMessage;
    }

    @NonNull
    public static WeatherUiState loading() {
        return new WeatherUiState(Status.LOADING, null, null, null, null, null);
    }

    @NonNull
    public static WeatherUiState loading(@Nullable City currentCity) {
        return new WeatherUiState(Status.LOADING, null, currentCity, null, null, null);
    }

    @NonNull
    public static WeatherUiState success(@NonNull WeatherForecast data) {
        return new WeatherUiState(Status.SUCCESS, Objects.requireNonNull(data), null, null, null, null);
    }

    @NonNull
    public static WeatherUiState success(@NonNull WeatherForecast data,
                                         @Nullable City selectedCity,
                                         @Nullable AiWeatherAdvisory aiAdvisory,
                                         @Nullable List<AutomationAlert> activeAlerts) {
        return new WeatherUiState(Status.SUCCESS, Objects.requireNonNull(data), selectedCity, aiAdvisory, activeAlerts, null);
    }

    @NonNull
    public static WeatherUiState error(@NonNull String message) {
        return new WeatherUiState(Status.ERROR, null, null, null, null, Objects.requireNonNull(message));
    }

    @NonNull
    public static WeatherUiState error(@NonNull String message, @Nullable City selectedCity) {
        return new WeatherUiState(Status.ERROR, null, selectedCity, null, null, Objects.requireNonNull(message));
    }

    @NonNull
    public Status getStatus() {
        return status;
    }

    public boolean isLoading() {
        return status == Status.LOADING;
    }

    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }

    public boolean isError() {
        return status == Status.ERROR;
    }

    @Nullable
    public WeatherForecast getData() {
        return data;
    }

    @Nullable
    public City getSelectedCity() {
        return selectedCity;
    }

    @Nullable
    public AiWeatherAdvisory getAiAdvisory() {
        return aiAdvisory;
    }

    @NonNull
    public List<AutomationAlert> getActiveAlerts() {
        return activeAlerts;
    }

    @Nullable
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeatherUiState that = (WeatherUiState) o;
        return status == that.status &&
                Objects.equals(data, that.data) &&
                Objects.equals(selectedCity, that.selectedCity) &&
                Objects.equals(aiAdvisory, that.aiAdvisory) &&
                Objects.equals(activeAlerts, that.activeAlerts) &&
                Objects.equals(errorMessage, that.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, data, selectedCity, aiAdvisory, activeAlerts, errorMessage);
    }

    @NonNull
    @Override
    public String toString() {
        return "WeatherUiState{" +
                "status=" + status +
                ", data=" + data +
                ", selectedCity=" + selectedCity +
                ", activeAlertsCount=" + activeAlerts.size() +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
