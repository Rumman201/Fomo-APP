package com.example.fomo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.fomo.domain.model.WmoWeatherCodeMapper;
import org.junit.Test;

public class WmoWeatherCodeMapperTest {

    @Test
    public void testClearSkyMapping() {
        assertEquals("Clear sky", WmoWeatherCodeMapper.getConditionDescription(0));
        assertEquals("sunny", WmoWeatherCodeMapper.getIconPath(0));
    }

    @Test
    public void testCloudyMappings() {
        assertEquals("Mainly clear", WmoWeatherCodeMapper.getConditionDescription(1));
        assertEquals("cloudy_sunny", WmoWeatherCodeMapper.getIconPath(1));

        assertEquals("Partly cloudy", WmoWeatherCodeMapper.getConditionDescription(2));
        assertEquals("cloudy", WmoWeatherCodeMapper.getIconPath(2));

        assertEquals("Overcast", WmoWeatherCodeMapper.getConditionDescription(3));
        assertEquals("cloudy", WmoWeatherCodeMapper.getIconPath(3));
    }

    @Test
    public void testRainMappings() {
        assertEquals("Slight rain", WmoWeatherCodeMapper.getConditionDescription(61));
        assertEquals("rainy", WmoWeatherCodeMapper.getIconPath(61));

        assertEquals("Moderate rain", WmoWeatherCodeMapper.getConditionDescription(63));
        assertEquals("rainy", WmoWeatherCodeMapper.getIconPath(63));

        assertEquals("Heavy rain", WmoWeatherCodeMapper.getConditionDescription(65));
        assertEquals("rainy", WmoWeatherCodeMapper.getIconPath(65));
    }

    @Test
    public void testSnowMappings() {
        assertEquals("Slight snow fall", WmoWeatherCodeMapper.getConditionDescription(71));
        assertEquals("snowy", WmoWeatherCodeMapper.getIconPath(71));

        assertEquals("Moderate snow fall", WmoWeatherCodeMapper.getConditionDescription(73));
        assertEquals("snowy", WmoWeatherCodeMapper.getIconPath(73));
    }

    @Test
    public void testThunderstormMappings() {
        assertEquals("Thunderstorm", WmoWeatherCodeMapper.getConditionDescription(95));
        assertEquals("storm", WmoWeatherCodeMapper.getIconPath(95));

        assertEquals("Thunderstorm with hail", WmoWeatherCodeMapper.getConditionDescription(96));
        assertEquals("storm", WmoWeatherCodeMapper.getIconPath(96));
    }

    @Test
    public void testFallbackUnknownCodes() {
        assertNotNull(WmoWeatherCodeMapper.getConditionDescription(999));
        assertNotNull(WmoWeatherCodeMapper.getIconPath(999));
    }
}
