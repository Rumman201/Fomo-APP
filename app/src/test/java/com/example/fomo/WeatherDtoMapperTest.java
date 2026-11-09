package com.example.fomo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.example.fomo.data.remote.dto.CurrentWeatherDto;
import com.example.fomo.data.remote.dto.HourlyForecastDto;
import com.example.fomo.data.remote.dto.OpenMeteoWeatherResponse;
import com.example.fomo.data.remote.mapper.WeatherDtoMapper;
import com.example.fomo.domain.model.WeatherForecast;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class WeatherDtoMapperTest {

    @Test
    public void testFormatIsoHour() {
        assertEquals("12 am", WeatherDtoMapper.formatIsoHour("2026-09-01T00:00"));
        assertEquals("1 am", WeatherDtoMapper.formatIsoHour("2026-09-01T01:00"));
        assertEquals("9 am", WeatherDtoMapper.formatIsoHour("2026-09-01T09:00"));
        assertEquals("12 pm", WeatherDtoMapper.formatIsoHour("2026-09-01T12:00"));
        assertEquals("1 pm", WeatherDtoMapper.formatIsoHour("2026-09-01T13:00"));
        assertEquals("6 pm", WeatherDtoMapper.formatIsoHour("2026-09-01T18:00"));
        assertEquals("11 pm", WeatherDtoMapper.formatIsoHour("2026-09-01T23:00"));
    }

    @Test
    public void testFormatCurrentDateTime() {
        String formatted = WeatherDtoMapper.formatCurrentDateTime("2026-09-01T14:30");
        assertTrue(formatted.contains("2:30 PM"));
    }

    @Test
    public void testToDomainMappingComplete() {
        CurrentWeatherDto current = new CurrentWeatherDto(
                "2026-09-01T12:00",
                25.4,
                62,
                26.0,
                0.0,
                1, // Mainly clear
                14.2
        );

        List<String> times = Arrays.asList("2026-09-01T12:00", "2026-09-01T13:00", "2026-09-01T14:00");
        List<Double> temps = Arrays.asList(25.4, 27.8, 23.1);
        List<Integer> codes = Arrays.asList(1, 0, 61);

        HourlyForecastDto hourly = new HourlyForecastDto(times, temps, codes);

        OpenMeteoWeatherResponse response = new OpenMeteoWeatherResponse(
                40.7128, -74.0060, "America/New_York", current, hourly
        );

        WeatherForecast forecast = WeatherDtoMapper.toDomain(response);
        assertNotNull(forecast);
        assertEquals("Mainly clear", forecast.getCurrentWeather().getCondition());
        assertEquals(25, forecast.getCurrentWeather().getTemperature());
        assertEquals("62%", forecast.getCurrentWeather().getHumidityPercentage());
        assertEquals("14 Km/h", forecast.getCurrentWeather().getWindSpeed());
        assertEquals("cloudy_sunny", forecast.getCurrentWeather().getIconKey());

        assertEquals(3, forecast.getHourlyForecasts().size());
        assertEquals("12 pm", forecast.getHourlyForecasts().get(0).getHour());
        assertEquals(25, forecast.getHourlyForecasts().get(0).getTemp());
        assertEquals("cloudy_sunny", forecast.getHourlyForecasts().get(0).getPicPath());

        assertEquals("1 pm", forecast.getHourlyForecasts().get(1).getHour());
        assertEquals(28, forecast.getHourlyForecasts().get(1).getTemp());
        assertEquals("sunny", forecast.getHourlyForecasts().get(1).getPicPath());

        assertEquals("2 pm", forecast.getHourlyForecasts().get(2).getHour());
        assertEquals(23, forecast.getHourlyForecasts().get(2).getTemp());
        assertEquals("rainy", forecast.getHourlyForecasts().get(2).getPicPath());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullResponseThrows() {
        WeatherDtoMapper.toDomain(null);
    }
}
