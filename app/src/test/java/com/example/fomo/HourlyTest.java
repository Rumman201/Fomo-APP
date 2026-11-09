package com.example.fomo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import com.example.fomo.domain.model.Hourly;

import org.junit.Test;

/**
 * Unit tests for the immutable Hourly domain model.
 */
public class HourlyTest {

    @Test
    public void testHourlyModelConstructionAndGetters() {
        Hourly hourly = new Hourly("9 PM", 28, "cloudy");

        assertEquals("9 PM", hourly.getHour());
        assertEquals(28, hourly.getTemp());
        assertEquals("cloudy", hourly.getPicPath());
    }

    @Test
    public void testHourlyEqualityAndHashCode() {
        Hourly item1 = new Hourly("10 AM", 20, "sun");
        Hourly item2 = new Hourly("10 AM", 20, "sun");
        Hourly different = new Hourly("11 AM", 20, "sun");

        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());
        assertNotEquals(item1, different);
    }

    @Test
    public void testHourlyTemperatureIntegrity() {
        Hourly subZero = new Hourly("3 AM", -5, "snowy");
        assertEquals(-5, subZero.getTemp());
        assertNotNull(subZero.getHour());
        assertNotNull(subZero.getPicPath());
        assertNotNull(subZero.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testNullHourThrowsException() {
        new Hourly(null, 25, "cloudy");
    }

    @Test(expected = NullPointerException.class)
    public void testNullPicPathThrowsException() {
        new Hourly("12 PM", 25, null);
    }
}
