package com.example.fomo.domain.model;

import androidx.annotation.NonNull;
import java.util.Objects;

/**
 * Immutable domain model representing current meteorological observations and metrics.
 */
public final class CurrentWeather {
    @NonNull
    private final String condition;
    private final int temperature;
    @NonNull
    private final String dateTime;
    @NonNull
    private final String highLow;
    @NonNull
    private final String rainPercentage;
    @NonNull
    private final String windSpeed;
    @NonNull
    private final String humidityPercentage;
    @NonNull
    private final String iconKey;

    public CurrentWeather(
            @NonNull String condition,
            int temperature,
            @NonNull String dateTime,
            @NonNull String highLow,
            @NonNull String rainPercentage,
            @NonNull String windSpeed,
            @NonNull String humidityPercentage,
            @NonNull String iconKey
    ) {
        this.condition = Objects.requireNonNull(condition, "condition must not be null");
        this.temperature = temperature;
        this.dateTime = Objects.requireNonNull(dateTime, "dateTime must not be null");
        this.highLow = Objects.requireNonNull(highLow, "highLow must not be null");
        this.rainPercentage = Objects.requireNonNull(rainPercentage, "rainPercentage must not be null");
        this.windSpeed = Objects.requireNonNull(windSpeed, "windSpeed must not be null");
        this.humidityPercentage = Objects.requireNonNull(humidityPercentage, "humidityPercentage must not be null");
        this.iconKey = Objects.requireNonNull(iconKey, "iconKey must not be null");
    }

    @NonNull
    public String getCondition() {
        return condition;
    }

    public int getTemperature() {
        return temperature;
    }

    @NonNull
    public String getDateTime() {
        return dateTime;
    }

    @NonNull
    public String getHighLow() {
        return highLow;
    }

    @NonNull
    public String getRainPercentage() {
        return rainPercentage;
    }

    @NonNull
    public String getWindSpeed() {
        return windSpeed;
    }

    @NonNull
    public String getHumidityPercentage() {
        return humidityPercentage;
    }

    @NonNull
    public String getIconKey() {
        return iconKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrentWeather that = (CurrentWeather) o;
        return temperature == that.temperature &&
                condition.equals(that.condition) &&
                dateTime.equals(that.dateTime) &&
                highLow.equals(that.highLow) &&
                rainPercentage.equals(that.rainPercentage) &&
                windSpeed.equals(that.windSpeed) &&
                humidityPercentage.equals(that.humidityPercentage) &&
                iconKey.equals(that.iconKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(condition, temperature, dateTime, highLow,
                rainPercentage, windSpeed, humidityPercentage, iconKey);
    }

    @NonNull
    @Override
    public String toString() {
        return "CurrentWeather{" +
                "condition='" + condition + '\'' +
                ", temperature=" + temperature +
                ", dateTime='" + dateTime + '\'' +
                ", highLow='" + highLow + '\'' +
                ", rainPercentage='" + rainPercentage + '\'' +
                ", windSpeed='" + windSpeed + '\'' +
                ", humidityPercentage='" + humidityPercentage + '\'' +
                ", iconKey='" + iconKey + '\'' +
                '}';
    }
}
