package com.example.fomo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import com.example.fomo.domain.model.CurrentWeather;

import org.junit.Test;

/**
 * Unit tests for CurrentWeather domain entity contracts.
 */
public class CurrentWeatherTest {

    @Test
    public void testCurrentWeatherConstructionAndGetters() {
        CurrentWeather weather = new CurrentWeather(
                "Mostly Cloudy",
                25,
                "Mon Dec 16 | 2:03 AM",
                "H:27 L:18",
                "22%",
                "12 km/h",
                "18%",
                "cloudy_sunny"
        );

        assertEquals("Mostly Cloudy", weather.getCondition());
        assertEquals(25, weather.getTemperature());
        assertEquals("Mon Dec 16 | 2:03 AM", weather.getDateTime());
        assertEquals("H:27 L:18", weather.getHighLow());
        assertEquals("22%", weather.getRainPercentage());
        assertEquals("12 km/h", weather.getWindSpeed());
        assertEquals("18%", weather.getHumidityPercentage());
        assertEquals("cloudy_sunny", weather.getIconKey());
    }

    @Test
    public void testEqualityAndHashCode() {
        CurrentWeather w1 = new CurrentWeather("Rainy", 18, "Tue", "H:20 L:15", "80%", "20 km/h", "75%", "rainy");
        CurrentWeather w2 = new CurrentWeather("Rainy", 18, "Tue", "H:20 L:15", "80%", "20 km/h", "75%", "rainy");
        CurrentWeather w3 = new CurrentWeather("Sunny", 28, "Tue", "H:30 L:22", "10%", "5 km/h", "30%", "sunny");

        assertEquals(w1, w2);
        assertEquals(w1.hashCode(), w2.hashCode());
        assertNotEquals(w1, w3);
        assertNotNull(w1.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testNullConditionThrowsException() {
        new CurrentWeather(null, 20, "Now", "H:20 L:10", "10%", "5 km/h", "40%", "sunny");
    }
}
