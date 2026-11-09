package com.example.fomo.domain.model;

import androidx.annotation.NonNull;
import java.util.Objects;

/**
 * Immutable domain model representing a triggered meteorological alert.
 */
public final class AutomationAlert {

    private final String triggerType;
    private final AtmosphericRisk.Level severity;
    private final String title;
    private final String message;
    private final long timestamp;
    private final double metricValue;

    public AutomationAlert(@NonNull String triggerType,
                           @NonNull AtmosphericRisk.Level severity,
                           @NonNull String title,
                           @NonNull String message,
                           long timestamp,
                           double metricValue) {
        this.triggerType = Objects.requireNonNull(triggerType, "triggerType must not be null");
        this.severity = Objects.requireNonNull(severity, "severity must not be null");
        this.title = Objects.requireNonNull(title, "title must not be null");
        this.message = Objects.requireNonNull(message, "message must not be null");
        this.timestamp = timestamp;
        this.metricValue = metricValue;
    }

    @NonNull
    public String getTriggerType() {
        return triggerType;
    }

    @NonNull
    public AtmosphericRisk.Level getSeverity() {
        return severity;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public double getMetricValue() {
        return metricValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AutomationAlert)) return false;
        AutomationAlert that = (AutomationAlert) o;
        return timestamp == that.timestamp &&
                Double.compare(that.metricValue, metricValue) == 0 &&
                triggerType.equals(that.triggerType) &&
                severity == that.severity &&
                title.equals(that.title) &&
                message.equals(that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(triggerType, severity, title, message, timestamp, metricValue);
    }
}

