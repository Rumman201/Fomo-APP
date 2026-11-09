package com.example.fomo.data.remote.mapper;

import com.example.fomo.data.remote.dto.CurrentWeatherDto;
import com.example.fomo.data.remote.dto.HourlyForecastDto;
import com.example.fomo.data.remote.dto.OpenMeteoWeatherResponse;
import com.example.fomo.domain.model.CurrentWeather;
import com.example.fomo.domain.model.Hourly;
import com.example.fomo.domain.model.WeatherForecast;
import com.example.fomo.domain.model.WmoWeatherCodeMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Maps remote Open-Meteo DTO structures into clean, immutable Domain models.
 */
public final class WeatherDtoMapper {

    private WeatherDtoMapper() {
        // Utility mapper class
    }

    /**
     * Converts an OpenMeteoWeatherResponse into a domain WeatherForecast.
     *
     * @param response Remote API response DTO
     * @return Immutable WeatherForecast domain entity
     * @throws IllegalArgumentException if response or critical inner DTOs are null
     */
    public static WeatherForecast toDomain(OpenMeteoWeatherResponse response) {
        if (response == null) {
            throw new IllegalArgumentException("Response DTO cannot be null");
        }
        CurrentWeatherDto currentDto = response.getCurrent();
        if (currentDto == null) {
            throw new IllegalArgumentException("Current weather DTO cannot be null");
        }

        int weatherCode = (currentDto.getWeatherCode() != null) ? currentDto.getWeatherCode() : 0;
        int currentTemp = (currentDto.getTemperature2m() != null)
                ? (int) Math.round(currentDto.getTemperature2m()) : 20;
        int humidity = (currentDto.getRelativeHumidity2m() != null)
                ? currentDto.getRelativeHumidity2m() : 50;
        int windSpeed = (currentDto.getWindSpeed10m() != null)
                ? (int) Math.round(currentDto.getWindSpeed10m()) : 10;
        int rainPercentage = (currentDto.getPrecipitation() != null)
                ? (int) Math.min(100, Math.round(currentDto.getPrecipitation() * 10)) : 0;

        String status = WmoWeatherCodeMapper.getConditionDescription(weatherCode);
        String picPath = WmoWeatherCodeMapper.getIconPath(weatherCode);

        // Map hourly forecast items
        List<Hourly> hourlyList = new ArrayList<>();
        HourlyForecastDto hourlyDto = response.getHourly();
        int calculatedHigh = currentTemp;
        int calculatedLow = currentTemp;

        if (hourlyDto != null && hourlyDto.getTime() != null && hourlyDto.getTemperature2m() != null) {
            List<String> times = hourlyDto.getTime();
            List<Double> temps = hourlyDto.getTemperature2m();
            List<Integer> codes = hourlyDto.getWeatherCode();

            int count = Math.min(times.size(), temps.size());
            int limit = Math.min(count, 24);

            double maxTemp = Double.NEGATIVE_INFINITY;
            double minTemp = Double.POSITIVE_INFINITY;

            for (int i = 0; i < limit; i++) {
                String rawTime = times.get(i);
                Double rawTemp = temps.get(i);
                int code = (codes != null && i < codes.size() && codes.get(i) != null)
                        ? codes.get(i) : weatherCode;

                double tempVal = (rawTemp != null) ? rawTemp : currentTemp;
                if (tempVal > maxTemp) {
                    maxTemp = tempVal;
                }
                if (tempVal < minTemp) {
                    minTemp = tempVal;
                }

                String formattedHour = formatIsoHour(rawTime);
                int roundedTemp = (int) Math.round(tempVal);
                String hourPicPath = WmoWeatherCodeMapper.getIconPath(code);

                hourlyList.add(new Hourly(formattedHour, roundedTemp, hourPicPath));
            }

            if (maxTemp != Double.NEGATIVE_INFINITY) {
                calculatedHigh = (int) Math.round(maxTemp);
            }
            if (minTemp != Double.POSITIVE_INFINITY) {
                calculatedLow = (int) Math.round(minTemp);
            }
        }

        if (calculatedHigh < currentTemp) {
            calculatedHigh = currentTemp;
        }
        if (calculatedLow > currentTemp) {
            calculatedLow = currentTemp;
        }

        String dateTimeStr = formatCurrentDateTime(currentDto.getTime());
        String highLowStr = "H:" + calculatedHigh + "  L:" + calculatedLow;
        String rainStr = rainPercentage + "%";
        String windStr = windSpeed + " Km/h";
        String humidityStr = humidity + "%";

        CurrentWeather currentWeather = new CurrentWeather(
                status,
                currentTemp,
                dateTimeStr,
                highLowStr,
                rainStr,
                windStr,
                humidityStr,
                picPath
        );

        return new WeatherForecast(currentWeather, Collections.unmodifiableList(hourlyList));
    }

    /**
     * Parses an ISO timestamp (e.g. "2026-09-01T14:00") into a user-friendly 12-hour format ("2 pm").
     *
     * @param isoTime ISO formatted time string
     * @return Formatted 12-hour string
     */
    public static String formatIsoHour(String isoTime) {
        if (isoTime == null || !isoTime.contains("T")) {
            return "12 pm";
        }
        try {
            int tIndex = isoTime.indexOf('T');
            String timePart = isoTime.substring(tIndex + 1);
            int colonIndex = timePart.indexOf(':');
            if (colonIndex != -1) {
                String hourStr = timePart.substring(0, colonIndex);
                int hour = Integer.parseInt(hourStr.trim());
                if (hour == 0) {
                    return "12 am";
                } else if (hour < 12) {
                    return hour + " am";
                } else if (hour == 12) {
                    return "12 pm";
                } else {
                    return (hour - 12) + " pm";
                }
            }
        } catch (Exception ignored) {
            // Fallback
        }
        return "12 pm";
    }

    /**
     * Formats the current ISO timestamp into a display string (e.g. "Today | 2:00 PM").
     */
    public static String formatCurrentDateTime(String isoTime) {
        if (isoTime == null || !isoTime.contains("T")) {
            return "Today | 12:00 PM";
        }
        try {
            int tIndex = isoTime.indexOf('T');
            String timePart = isoTime.substring(tIndex + 1);
            String[] parts = timePart.split(":");
            if (parts.length >= 2) {
                int hour = Integer.parseInt(parts[0].trim());
                String minute = parts[1].trim();
                String ampm = (hour >= 12) ? "PM" : "AM";
                int displayHour = (hour == 0) ? 12 : (hour > 12 ? hour - 12 : hour);
                return "Today | " + displayHour + ":" + minute + " " + ampm;
            }
        } catch (Exception ignored) {
            // Fallback
        }
        return "Today | 12:00 PM";
    }
}
