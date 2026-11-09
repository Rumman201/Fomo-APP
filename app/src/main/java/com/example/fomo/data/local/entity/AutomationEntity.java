package com.example.fomo.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Room entity storing audit logs and delivery history for intelligent meteorological automations.
 * Uses composite indices on `(triggerType, timestamp)` and `timestamp` to ensure O(1) cooldown lookups.
 */
@Entity(
        tableName = "automation_logs",
        indices = {
                @Index(value = {"triggerType", "timestamp"}),
                @Index(value = {"timestamp"})
        }
)
public class AutomationEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public long timestamp;

    @NonNull
    public String triggerType;

    @NonNull
    public String severity;

    @NonNull
    public String message;

    public double metricValue;

    public AutomationEntity() {
        this.triggerType = "";
        this.severity = "";
        this.message = "";
    }

    @androidx.room.Ignore
    public AutomationEntity(long id, long timestamp, @NonNull String triggerType,
                            @NonNull String severity, @NonNull String message, double metricValue) {
        this.id = id;
        this.timestamp = timestamp;
        this.triggerType = triggerType;
        this.severity = severity;
        this.message = message;
        this.metricValue = metricValue;
    }
}
