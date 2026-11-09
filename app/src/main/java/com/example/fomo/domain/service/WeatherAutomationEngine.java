package com.example.fomo.domain.service;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.fomo.data.local.dao.AutomationDao;
import com.example.fomo.data.local.entity.AutomationEntity;
import com.example.fomo.domain.model.AtmosphericRisk;
import com.example.fomo.domain.model.AutomationAlert;
import com.example.fomo.domain.model.CurrentWeather;
import com.example.fomo.domain.model.WeatherForecast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * Event-driven meteorological automation engine evaluating triggers,
 * enforcing cooldown suppression, preventing notification spam, and recording audit logs.
 */
public class WeatherAutomationEngine {

    public static final String TRIGGER_RAIN_ALERT = "RAIN_ALERT";
    public static final String TRIGGER_FREEZING_WARNING = "FREEZING_WARNING";
    public static final String TRIGGER_HIGH_WIND = "HIGH_WIND";
    public static final String TRIGGER_SEVERE_STORM = "SEVERE_STORM";

    public static final long DEFAULT_COOLDOWN_MILLIS = 60 * 60 * 1000L; // 60 minutes

    private final AutomationDao automationDao;
    private final Executor executor;
    private final long cooldownMillis;
    private final Map<String, Long> lastTriggeredMap = new HashMap<>();

    public WeatherAutomationEngine() {
        this(null, null, DEFAULT_COOLDOWN_MILLIS);
    }

    public WeatherAutomationEngine(@Nullable AutomationDao automationDao,
                                   @Nullable Executor executor) {
        this(automationDao, executor, DEFAULT_COOLDOWN_MILLIS);
    }

    public WeatherAutomationEngine(@Nullable AutomationDao automationDao,
                                   @Nullable Executor executor,
                                   long cooldownMillis) {
        this.automationDao = automationDao;
        this.executor = executor;
        this.cooldownMillis = cooldownMillis;
    }

    /**
     * Evaluates weather forecast against configured triggers and returns active unsuppressed alerts.
     */
    @NonNull
    public List<AutomationAlert> evaluateForecast(@NonNull WeatherForecast forecast) {
        if (forecast == null) return Collections.emptyList();
        CurrentWeather current = forecast.getCurrentWeather();
        if (current == null) return Collections.emptyList();

        List<AutomationAlert> activeAlerts = new ArrayList<>();
        long now = System.currentTimeMillis();

        double temp = current.getTemperature();
        int rainPercent = parseNumber(current.getRainPercentage());
        double windKmh = parseNumber(current.getWindSpeed());
        String condition = current.getCondition() != null ? current.getCondition().toLowerCase(Locale.ROOT) : "";

        // 1. Severe Storm trigger
        if (condition.contains("storm") || condition.contains("thunder")) {
            checkAndAddAlert(
                    TRIGGER_SEVERE_STORM,
                    AtmosphericRisk.Level.CRITICAL,
                    "Severe Storm Alert",
                    "Thunderstorm activity detected. Stay indoors and secure loose items.",
                    windKmh,
                    now,
                    activeAlerts
            );
        }

        // 2. Freezing Temperature trigger
        if (temp <= 0.0) {
            checkAndAddAlert(
                    TRIGGER_FREEZING_WARNING,
                    AtmosphericRisk.Level.WARNING,
                    "Freezing Road Warning",
                    "Sub-zero temperature (" + (int) temp + "°C). Watch for black ice on roadways.",
                    temp,
                    now,
                    activeAlerts
            );
        }

        // 3. High Wind trigger
        if (windKmh >= 40.0) {
            checkAndAddAlert(
                    TRIGGER_HIGH_WIND,
                    AtmosphericRisk.Level.WARNING,
                    "High Wind Warning",
                    "Strong wind gusts of " + (int) windKmh + " km/h detected. Use caution while traveling.",
                    windKmh,
                    now,
                    activeAlerts
            );
        }

        // 4. Imminent Rain trigger
        if (rainPercent >= 50) {
            checkAndAddAlert(
                    TRIGGER_RAIN_ALERT,
                    AtmosphericRisk.Level.ADVISORY,
                    "Rain Alert",
                    "High probability of rain (" + rainPercent + "%). Don't forget an umbrella.",
                    rainPercent,
                    now,
                    activeAlerts
            );
        }

        return activeAlerts;
    }

    private void checkAndAddAlert(String triggerType, AtmosphericRisk.Level severity,
                                  String title, String message, double metricValue,
                                  long now, List<AutomationAlert> alerts) {
        Long lastTriggered = lastTriggeredMap.get(triggerType);
        if (lastTriggered != null && (now - lastTriggered) < cooldownMillis) {
            // Suppressed by cooldown deduplication
            return;
        }

        lastTriggeredMap.put(triggerType, now);
        AutomationAlert alert = new AutomationAlert(triggerType, severity, title, message, now, metricValue);
        alerts.add(alert);

        // Record audit log asynchronously
        persistAuditLog(alert);
    }

    private void persistAuditLog(AutomationAlert alert) {
        if (automationDao == null) return;
        Runnable logTask = () -> {
            try {
                AutomationEntity entity = new AutomationEntity(
                        0,
                        alert.getTimestamp(),
                        alert.getTriggerType(),
                        alert.getSeverity().name(),
                        alert.getMessage(),
                        alert.getMetricValue()
                );
                automationDao.insertLog(entity);
            } catch (Exception ignored) {
            }
        };

        if (executor != null) {
            executor.execute(logTask);
        } else {
            new Thread(logTask).start();
        }
    }

    private int parseNumber(String text) {
        if (text == null) return 0;
        try {
            String clean = text.replaceAll("[^0-9]", "").trim();
            if (clean.isEmpty()) return 0;
            return Integer.parseInt(clean);
        } catch (Exception e) {
            return 0;
        }
    }
}

