package com.example.fomo.domain.model;

import androidx.annotation.NonNull;
import java.util.Objects;

/**
 * Immutable domain model representing meteorological risk assessment.
 */
public final class AtmosphericRisk {

    public enum Level {
        NORMAL,
        ADVISORY,
        WARNING,
        CRITICAL
    }

    private final Level level;
    private final String title;
    private final String description;

    public AtmosphericRisk(@NonNull Level level, @NonNull String title, @NonNull String description) {
        this.level = Objects.requireNonNull(level, "level must not be null");
        this.title = Objects.requireNonNull(title, "title must not be null");
        this.description = Objects.requireNonNull(description, "description must not be null");
    }

    @NonNull
    public Level getLevel() {
        return level;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public boolean isElevated() {
        return level != Level.NORMAL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AtmosphericRisk)) return false;
        AtmosphericRisk that = (AtmosphericRisk) o;
        return level == that.level &&
                title.equals(that.title) &&
                description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, title, description);
    }
}

