package com.example.fomo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import com.example.fomo.domain.model.DailyForecast;
import org.junit.Test;

public class DailyForecastTest {

    @Test
    public void testCreationAndGetters() {
        DailyForecast forecast = new DailyForecast("Tomorrow", "Clear sky", 28, 18, "sunny");

        assertEquals("Tomorrow", forecast.getDayName());
        assertEquals("Clear sky", forecast.getCondition());
        assertEquals(28, forecast.getHighTemp());
        assertEquals(18, forecast.getLowTemp());
        assertEquals("sunny", forecast.getIconKey());
        assertEquals("28° / 18°", forecast.getHighLowFormatted());
    }

    @Test
    public void testEqualityAndHashCode() {
        DailyForecast f1 = new DailyForecast("Tomorrow", "Clear sky", 28, 18, "sunny");
        DailyForecast f2 = new DailyForecast("Tomorrow", "Clear sky", 28, 18, "sunny");
        DailyForecast f3 = new DailyForecast("Monday", "Rain", 20, 14, "rainy");

        assertEquals(f1, f2);
        assertEquals(f1.hashCode(), f2.hashCode());
        assertNotEquals(f1, f3);
    }

    @Test(expected = NullPointerException.class)
    public void testNullDayNameThrows() {
        new DailyForecast(null, "Clear sky", 28, 18, "sunny");
    }

    @Test(expected = NullPointerException.class)
    public void testNullConditionThrows() {
        new DailyForecast("Tomorrow", null, 28, 18, "sunny");
    }

    @Test
    public void testToStringContainsData() {
        DailyForecast forecast = new DailyForecast("Wednesday", "Storm", 25, 17, "storm");
        assertNotNull(forecast.toString());
        assertEquals(true, forecast.toString().contains("Wednesday"));
    }
}
