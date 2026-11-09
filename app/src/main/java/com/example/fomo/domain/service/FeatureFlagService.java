package com.example.fomo.domain.service;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Service managing runtime feature toggles, progressive enablement, and emergency kill switches.
 */
public class FeatureFlagService {

    private static final String PREFS_NAME = "fomo_feature_flags";

    public static final String FLAG_AI_ADVISORY = "feature_ai_advisory";
    public static final String FLAG_AUTOMATION_ALERTS = "feature_automation_alerts";
    public static final String FLAG_MULTI_CITY = "feature_multi_city";
    public static final String FLAG_IOT_STATION_TELEMETRY = "feature_iot_station_telemetry";

    private final SharedPreferences preferences;

    public FeatureFlagService() {
        this.preferences = null;
    }

    public FeatureFlagService(@Nullable Context context) {
        if (context != null) {
            this.preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        } else {
            this.preferences = null;
        }
    }

    public boolean isAiAdvisoryEnabled() {
        return getFlag(FLAG_AI_ADVISORY, true);
    }

    public boolean isAutomationAlertsEnabled() {
        return getFlag(FLAG_AUTOMATION_ALERTS, true);
    }

    public boolean isMultiCityEnabled() {
        return getFlag(FLAG_MULTI_CITY, true);
    }

    public boolean isIotStationTelemetryEnabled() {
        return getFlag(FLAG_IOT_STATION_TELEMETRY, true);
    }

    public void setFlag(@NonNull String flagKey, boolean enabled) {
        if (preferences != null) {
            preferences.edit().putBoolean(flagKey, enabled).apply();
        }
    }

    private boolean getFlag(String flagKey, boolean defaultValue) {
        if (preferences != null) {
            return preferences.getBoolean(flagKey, defaultValue);
        }
        return defaultValue;
    }
}

