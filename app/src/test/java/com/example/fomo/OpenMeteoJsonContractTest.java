package com.example.fomo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.fomo.data.remote.dto.OpenMeteoWeatherResponse;
import com.example.fomo.data.remote.mapper.WeatherDtoMapper;
import com.example.fomo.domain.model.WeatherForecast;
import com.google.gson.Gson;
import org.junit.Test;

public class OpenMeteoJsonContractTest {

    private static final String SAMPLE_OPEN_METEO_JSON = "{\n" +
            "  \"latitude\": 40.7128,\n" +
            "  \"longitude\": -74.006,\n" +
            "  \"generationtime_ms\": 0.082,\n" +
            "  \"utc_offset_seconds\": -14400,\n" +
            "  \"timezone\": \"America/New_York\",\n" +
            "  \"timezone_abbreviation\": \"EDT\",\n" +
            "  \"elevation\": 10.0,\n" +
            "  \"current\": {\n" +
            "    \"time\": \"2026-09-01T14:00\",\n" +
            "    \"interval\": 900,\n" +
            "    \"temperature_2m\": 26.5,\n" +
            "    \"relative_humidity_2m\": 55,\n" +
            "    \"apparent_temperature\": 27.0,\n" +
            "    \"precipitation\": 0.0,\n" +
            "    \"weather_code\": 0,\n" +
            "    \"wind_speed_10m\": 11.2\n" +
            "  },\n" +
            "  \"hourly\": {\n" +
            "    \"time\": [\"2026-09-01T14:00\", \"2026-09-01T15:00\"],\n" +
            "    \"temperature_2m\": [26.5, 27.1],\n" +
            "    \"weather_code\": [0, 1]\n" +
            "  }\n" +
            "}";

    @Test
    public void testGsonDeserializationOfOpenMeteoPayload() {
        Gson gson = new Gson();
        OpenMeteoWeatherResponse response = gson.fromJson(SAMPLE_OPEN_METEO_JSON, OpenMeteoWeatherResponse.class);

        assertNotNull(response);
        assertEquals(Double.valueOf(40.7128), response.getLatitude());
        assertEquals(Double.valueOf(-74.006), response.getLongitude());
        assertEquals("America/New_York", response.getTimezone());

        assertNotNull(response.getCurrent());
        assertEquals("2026-09-01T14:00", response.getCurrent().getTime());
        assertEquals(Double.valueOf(26.5), response.getCurrent().getTemperature2m());
        assertEquals(Integer.valueOf(55), response.getCurrent().getRelativeHumidity2m());
        assertEquals(Integer.valueOf(0), response.getCurrent().getWeatherCode());

        assertNotNull(response.getHourly());
        assertEquals(2, response.getHourly().getTime().size());
        assertEquals(2, response.getHourly().getTemperature2m().size());
        assertEquals(2, response.getHourly().getWeatherCode().size());
    }

    @Test
    public void testContractToDomainIntegration() {
        Gson gson = new Gson();
        OpenMeteoWeatherResponse response = gson.fromJson(SAMPLE_OPEN_METEO_JSON, OpenMeteoWeatherResponse.class);
        WeatherForecast forecast = WeatherDtoMapper.toDomain(response);

        assertNotNull(forecast);
        assertEquals("Clear sky", forecast.getCurrentWeather().getCondition());
        assertEquals(27, forecast.getCurrentWeather().getTemperature());
        assertEquals("sunny", forecast.getCurrentWeather().getIconKey());
        assertEquals(2, forecast.getHourlyForecasts().size());
    }
}
