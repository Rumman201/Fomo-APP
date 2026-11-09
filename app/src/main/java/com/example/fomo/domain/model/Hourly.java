package com.example.fomo.domain.model;

import androidx.annotation.NonNull;
import java.util.Objects;

/**
 * Immutable domain model representing a single hourly weather forecast interval.
 */
public final class Hourly {
    @NonNull
    private final String hour;
    private final int temp;
    @NonNull
    private final String picPath;

    public Hourly(@NonNull String hour, int temp, @NonNull String picPath) {
        this.hour = Objects.requireNonNull(hour, "hour must not be null");
        this.temp = temp;
        this.picPath = Objects.requireNonNull(picPath, "picPath must not be null");
    }

    @NonNull
    public String getHour() {
        return hour;
    }

    public int getTemp() {
        return temp;
    }

    @NonNull
    public String getPicPath() {
        return picPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hourly hourly = (Hourly) o;
        return temp == hourly.temp &&
                hour.equals(hourly.hour) &&
                picPath.equals(hourly.picPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hour, temp, picPath);
    }

    @NonNull
    @Override
    public String toString() {
        return "Hourly{" +
                "hour='" + hour + '\'' +
                ", temp=" + temp +
                ", picPath='" + picPath + '\'' +
                '}';
    }
}
