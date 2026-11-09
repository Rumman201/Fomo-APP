package com.example.fomo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.example.fomo.domain.model.ActivityScores;
import com.example.fomo.domain.model.AtmosphericIntelligence;
import com.example.fomo.domain.model.AtmosphericRisk;
import com.example.fomo.domain.model.CurrentWeather;
import com.example.fomo.domain.service.AtmosphericIntelligenceService;
import org.junit.Before;
import org.junit.Test;

public class AtmosphericIntelligenceServiceTest {

    private AtmosphericIntelligenceService service;

    @Before
    public void setUp() {
        service = new AtmosphericIntelligenceService();
    }

    @Test
    public void testThermalComfortSteadmanHeatIndexCalculation() {
        // At 32C and 70% humidity, apparent temperature should exceed actual temperature significantly
        double apparent = service.calculateApparentTemperature(32.0, 70.0, 10.0);
        assertTrue("Heat index should be higher than dry bulb temperature", apparent > 35.0);
    }

    @Test
    public void testThermalComfortWindChillCalculation() {
        // At -5C and 30 km/h wind, wind chill should be significantly colder than actual temperature
        double apparent = service.calculateApparentTemperature(-5.0, 60.0, 30.0);
        assertTrue("Wind chill should be lower than dry bulb temperature", apparent < -10.0);
    }

    @Test
    public void testComfortCategorization() {
        assertEquals("Extreme Heat Hazard", service.categorizeComfort(40.0));
        assertEquals("Warm & Humid", service.categorizeComfort(32.0));
        assertEquals("Optimal Comfort", service.categorizeComfort(22.0));
        assertEquals("Mild & Fresh", service.categorizeComfort(15.0));
        assertEquals("Cool & Crisp", service.categorizeComfort(8.0));
        assertEquals("Chilly", service.categorizeComfort(2.0));
        assertEquals("Freezing Frost", service.categorizeComfort(-8.0));
    }

    @Test
    public void testAttireRecommendations() {
        String freezingAdvice = service.deriveAttireRecommendation(-3.0, -8.0, 10, 15.0, "Clear");
        assertTrue(freezingAdvice.contains("Heavy winter coat"));

        String rainAdvice = service.deriveAttireRecommendation(18.0, 18.0, 60, 12.0, "Showers");
        assertTrue(rainAdvice.contains("umbrella"));

        String hotAdvice = service.deriveAttireRecommendation(28.0, 30.0, 10, 10.0, "Sunny");
        assertTrue(hotAdvice.contains("Light, breathable"));

        String windyAdvice = service.deriveAttireRecommendation(15.0, 12.0, 10, 42.0, "Breezy");
        assertTrue(windyAdvice.contains("Windbreaker"));
    }

    @Test
    public void testActivityScoresDegradationInStorm() {
        ActivityScores scores = service.calculateActivityScores(22.0, 80, 50.0, "Thunderstorm");
        assertTrue("Running score should be severely degraded in a thunderstorm", scores.getRunningScore() < 25);
        assertTrue("Outdoor dining should be near zero in a thunderstorm", scores.getOutdoorDiningScore() < 15);
        assertEquals("Indoor activities favored", scores.getPrimaryRecommendation());
    }

    @Test
    public void testAtmosphericRiskAssessment() {
        AtmosphericRisk criticalRisk = service.assessAtmosphericRisk(22.0, 70.0, 80, "Heavy Thunderstorm");
        assertEquals(AtmosphericRisk.Level.CRITICAL, criticalRisk.getLevel());
        assertTrue(criticalRisk.isElevated());

        AtmosphericRisk warningRisk = service.assessAtmosphericRisk(-8.0, 20.0, 10, "Clear");
        assertEquals(AtmosphericRisk.Level.WARNING, warningRisk.getLevel());

        AtmosphericRisk normalRisk = service.assessAtmosphericRisk(21.0, 12.0, 10, "Partly Cloudy");
        assertEquals(AtmosphericRisk.Level.NORMAL, normalRisk.getLevel());
        assertEquals(false, normalRisk.isElevated());
    }

    @Test
    public void testEndToEndAnalysis() {
        CurrentWeather weather = new CurrentWeather(
                "Scattered Showers",
                16,
                "Today | 3:00 PM",
                "H:18 L:12",
                "65%",
                "22 km/h",
                "75%",
                "rainy"
        );

        AtmosphericIntelligence intel = service.analyze(weather);
        assertNotNull(intel);
        assertNotNull(intel.getComfortCategory());
        assertNotNull(intel.getClothingAdvice());
        assertNotNull(intel.getActivityScores());
        assertNotNull(intel.getRisk());
        assertTrue(intel.getClothingAdvice().contains("umbrella"));
    }
}

