package com.example.fomo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.example.fomo.domain.model.AtmosphericRisk;
import com.example.fomo.domain.model.AutomationAlert;
import com.example.fomo.domain.model.CurrentWeather;
import com.example.fomo.domain.model.Hourly;
import com.example.fomo.domain.model.WeatherForecast;
import com.example.fomo.domain.service.WeatherAutomationEngine;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class WeatherAutomationEngineTest {

    private WeatherAutomationEngine engine;

    @Before
    public void setUp() {
        // Cooldown of 1000ms for test environment
        engine = new WeatherAutomationEngine(null, null, 1000L);
    }

    @Test
    public void testSevereStormTrigger() {
        CurrentWeather stormWeather = new CurrentWeather(
                "Severe Thunderstorm", 22, "Today", "H:24 L:18", "90%", "65 km/h", "85%", "storm"
        );
        WeatherForecast forecast = new WeatherForecast(stormWeather, Collections.emptyList());

        List<AutomationAlert> alerts = engine.evaluateForecast(forecast);
        assertFalse(alerts.isEmpty());

        boolean hasStormAlert = false;
        for (AutomationAlert alert : alerts) {
            if (alert.getTriggerType().equals(WeatherAutomationEngine.TRIGGER_SEVERE_STORM)) {
                hasStormAlert = true;
                assertEquals(AtmosphericRisk.Level.CRITICAL, alert.getSeverity());
            }
        }
        assertTrue("Storm alert should be triggered", hasStormAlert);
    }

    @Test
    public void testFreezingRoadTrigger() {
        CurrentWeather freezeWeather = new CurrentWeather(
                "Clear Sky", -3, "Tonight", "H:2 L:-5", "10%", "15 km/h", "40%", "sunny"
        );
        WeatherForecast forecast = new WeatherForecast(freezeWeather, Collections.emptyList());

        List<AutomationAlert> alerts = engine.evaluateForecast(forecast);
        boolean hasFreezeAlert = false;
        for (AutomationAlert alert : alerts) {
            if (alert.getTriggerType().equals(WeatherAutomationEngine.TRIGGER_FREEZING_WARNING)) {
                hasFreezeAlert = true;
                assertEquals(AtmosphericRisk.Level.WARNING, alert.getSeverity());
            }
        }
        assertTrue("Freezing alert should be triggered", hasFreezeAlert);
    }

    @Test
    public void testHighWindTrigger() {
        CurrentWeather windWeather = new CurrentWeather(
                "Windy", 18, "Today", "H:20 L:14", "20%", "48 km/h", "50%", "windy"
        );
        WeatherForecast forecast = new WeatherForecast(windWeather, Collections.emptyList());

        List<AutomationAlert> alerts = engine.evaluateForecast(forecast);
        boolean hasWindAlert = false;
        for (AutomationAlert alert : alerts) {
            if (alert.getTriggerType().equals(WeatherAutomationEngine.TRIGGER_HIGH_WIND)) {
                hasWindAlert = true;
                assertEquals(AtmosphericRisk.Level.WARNING, alert.getSeverity());
            }
        }
        assertTrue("High wind alert should be triggered", hasWindAlert);
    }

    @Test
    public void testRainAlertTrigger() {
        CurrentWeather rainWeather = new CurrentWeather(
                "Scattered Showers", 17, "Today", "H:19 L:13", "75%", "12 km/h", "70%", "rainy"
        );
        WeatherForecast forecast = new WeatherForecast(rainWeather, Collections.emptyList());

        List<AutomationAlert> alerts = engine.evaluateForecast(forecast);
        boolean hasRainAlert = false;
        for (AutomationAlert alert : alerts) {
            if (alert.getTriggerType().equals(WeatherAutomationEngine.TRIGGER_RAIN_ALERT)) {
                hasRainAlert = true;
                assertEquals(AtmosphericRisk.Level.ADVISORY, alert.getSeverity());
            }
        }
        assertTrue("Rain alert should be triggered", hasRainAlert);
    }

    @Test
    public void testCooldownDeduplicationSuppression() {
        CurrentWeather rainWeather = new CurrentWeather(
                "Rainy", 16, "Today", "H:18 L:12", "80%", "10 km/h", "80%", "rainy"
        );
        WeatherForecast forecast = new WeatherForecast(rainWeather, Collections.emptyList());

        // First execution -> alert generated
        List<AutomationAlert> firstPass = engine.evaluateForecast(forecast);
        assertFalse(firstPass.isEmpty());

        // Immediate second execution -> alert suppressed by cooldown window
        List<AutomationAlert> secondPass = engine.evaluateForecast(forecast);
        assertTrue("Subsequent immediate alert should be suppressed by cooldown", secondPass.isEmpty());
    }
}

