package com.example.fomo.domain.model;

import androidx.annotation.NonNull;
import java.util.Objects;

/**
 * Immutable domain model representing a single daily forecast projection.
 */
public final class DailyForecast {

    @NonNull
    private final String dayName;
    @NonNull
    private final String condition;
    private final int highTemp;
    private final int lowTemp;
    @NonNull
    private final String iconKey;

    public DailyForecast(@NonNull String dayName,
                         @NonNull String condition,
                         int highTemp,
                         int lowTemp,
                         @NonNull String iconKey) {
        this.dayName = Objects.requireNonNull(dayName, "dayName must not be null");
        this.condition = Objects.requireNonNull(condition, "condition must not be null");
        this.highTemp = highTemp;
        this.lowTemp = lowTemp;
        this.iconKey = Objects.requireNonNull(iconKey, "iconKey must not be null");
    }

    @NonNull
    public String getDayName() {
        return dayName;
    }

    @NonNull
    public String getCondition() {
        return condition;
    }

    public int getHighTemp() {
        return highTemp;
    }

    public int getLowTemp() {
        return lowTemp;
    }

    @NonNull
    public String getIconKey() {
        return iconKey;
    }

    @NonNull
    public String getHighLowFormatted() {
        return highTemp + "° / " + lowTemp + "°";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyForecast that = (DailyForecast) o;
        return highTemp == that.highTemp &&
                lowTemp == that.lowTemp &&
                dayName.equals(that.dayName) &&
                condition.equals(that.condition) &&
                iconKey.equals(that.iconKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dayName, condition, highTemp, lowTemp, iconKey);
    }

    @NonNull
    @Override
    public String toString() {
        return "DailyForecast{" +
                "dayName='" + dayName + '\'' +
                ", condition='" + condition + '\'' +
                ", highTemp=" + highTemp +
                ", lowTemp=" + lowTemp +
                ", iconKey='" + iconKey + '\'' +
                '}';
    }
}
