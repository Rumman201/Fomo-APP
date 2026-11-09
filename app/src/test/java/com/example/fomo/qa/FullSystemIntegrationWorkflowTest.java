package com.example.fomo.qa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.example.fomo.core.observability.AppMetricsCollector;
import com.example.fomo.core.observability.HealthCheckService;
import com.example.fomo.core.resilience.ResilienceCircuitBreaker;
import com.example.fomo.domain.model.AiWeatherAdvisory;
import com.example.fomo.domain.model.AtmosphericIntelligence;
import com.example.fomo.domain.model.AutomationAlert;
import com.example.fomo.domain.model.City;
import com.example.fomo.domain.model.CurrentWeather;
import com.example.fomo.domain.model.Hourly;
import com.example.fomo.domain.model.WeatherForecast;
import com.example.fomo.domain.service.AtmosphericIntelligenceService;
import com.example.fomo.domain.service.WeatherAiAdvisor;
import com.example.fomo.domain.service.WeatherAutomationEngine;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Phase 9 End-to-End System Integration Workflow test suite.
 * Chains together domain models, atmospheric intelligence, AI natural language briefing synthesis,
 * automation triggers, and SLI/SLO operational health metrics in a complete operational cycle.
 */
public class FullSystemIntegrationWorkflowTest {

    private AtmosphericIntelligenceService intelligenceService;
    private WeatherAiAdvisor aiAdvisor;
    private WeatherAutomationEngine automationEngine;
    private ResilienceCircuitBreaker circuitBreaker;
    private HealthCheckService healthCheckService;
    private AppMetricsCollector metricsCollector;

    @Before
    public void setUp() {
        intelligenceService = new AtmosphericIntelligenceService();
        aiAdvisor = new WeatherAiAdvisor(intelligenceService);
        automationEngine = new WeatherAutomationEngine();
        circuitBreaker = new ResilienceCircuitBreaker(3, 1000L);
        healthCheckService = new HealthCheckService(circuitBreaker);
        metricsCollector = AppMetricsCollector.getInstance();
        metricsCollector.reset();
    }

    @Test
    public void testFullEndToEndWeatherIntelligencePipeline() {
        // Step 1: User navigates to Tokyo
        City city = new City(101, "Tokyo", "Japan", "Kanto", 35.6762, 139.6503, true, 1L);
        assertEquals("Tokyo", city.getName());
        assertEquals("Japan", city.getCountry());

        // Step 2: Telemetry arrives
        CurrentWeather current = new CurrentWeather(
                "Thunderstorm", 19, "Today | 03:00 PM",
                "H:22  L:16", "95%", "45 Km/h", "85%", "rainy"
        );
        WeatherForecast forecast = new WeatherForecast(
                current,
                Collections.singletonList(new Hourly("03:00 PM", 19, "rainy"))
        );

        // Step 3: Run Atmospheric Intelligence Analysis
        AtmosphericIntelligence intel = intelligenceService.analyze(current);
        assertNotNull(intel);
        assertTrue(intel.getApparentTemperature() > 0.0);
        assertNotNull(intel.getComfortCategory());
        assertNotNull(intel.getClothingAdvice());
        assertNotNull(intel.getActivityScores());

        // Step 4: Run AI Natural Language Briefing
        AiWeatherAdvisory advisory = aiAdvisor.generateBriefing(current);
        assertNotNull(advisory);
        assertNotNull(advisory.getHeadline());
        assertNotNull(advisory.getSummary());
        assertTrue(advisory.getSummary().contains("19°C"));

        // Step 5: Evaluate Automation Triggers
        List<AutomationAlert> alerts = automationEngine.evaluateForecast(forecast);
        assertNotNull(alerts);
        assertTrue("Severe thunderstorm must trigger at least one alert", alerts.size() >= 1);

        // Step 6: Record SLI metrics
        metricsCollector.recordCacheAccess(true);
        metricsCollector.recordNetworkOutcome(true);

        assertEquals(1, metricsCollector.getTotalCacheRequests());
        assertEquals(1, metricsCollector.getCacheHits());
        assertEquals(1.0, metricsCollector.getCacheHitRatio(), 0.001);

        // Step 7: Evaluate Health Status
        HealthCheckService.HealthReport report = healthCheckService.evaluateHealth(true, true);
        assertNotNull(report);
        assertTrue(report.isHealthy());
        assertEquals(HealthCheckService.HealthStatus.HEALTHY, report.getStatus());
    }
}

