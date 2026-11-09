package com.example.fomo;

import static org.junit.Assert.assertEquals;

import com.example.fomo.ui.adapter.WeatherIconMapper;

import org.junit.Test;

/**
 * Unit tests for WeatherIconMapper ensuring deterministic drawable resolution.
 */
public class WeatherIconMapperTest {

    @Test
    public void testKnownConditionsMapping() {
        assertEquals(R.drawable.cloudy, WeatherIconMapper.getDrawableResourceId("cloudy"));
        assertEquals(R.drawable.sunny, WeatherIconMapper.getDrawableResourceId("sun"));
        assertEquals(R.drawable.sunny, WeatherIconMapper.getDrawableResourceId("sunny"));
        assertEquals(R.drawable.rainy, WeatherIconMapper.getDrawableResourceId("rainy"));
        assertEquals(R.drawable.rain, WeatherIconMapper.getDrawableResourceId("rain"));
        assertEquals(R.drawable.wind, WeatherIconMapper.getDrawableResourceId("wind"));
        assertEquals(R.drawable.storm, WeatherIconMapper.getDrawableResourceId("storm"));
        assertEquals(R.drawable.snowy, WeatherIconMapper.getDrawableResourceId("snowy"));
    }

    @Test
    public void testCaseInsensitiveAndTrimming() {
        assertEquals(R.drawable.sunny, WeatherIconMapper.getDrawableResourceId(" SUN "));
        assertEquals(R.drawable.cloudy, WeatherIconMapper.getDrawableResourceId("Cloudy"));
        assertEquals(R.drawable.storm, WeatherIconMapper.getDrawableResourceId("STORm"));
    }

    @Test
    public void testNullAndUnknownFallback() {
        assertEquals(R.drawable.cloudy_sunny, WeatherIconMapper.getDrawableResourceId(null));
        assertEquals(R.drawable.cloudy_sunny, WeatherIconMapper.getDrawableResourceId("unknown_tornado"));
        assertEquals(R.drawable.cloudy_sunny, WeatherIconMapper.getDrawableResourceId(""));
    }
}
