package com.example.fomo.domain.model;

/**
 * Maps World Meteorological Organization (WMO) Weather Interpretation Codes (WW)
 * to human-readable condition descriptions and standardized picture path keys.
 * Pure domain utility with zero platform dependencies.
 */
public final class WmoWeatherCodeMapper {

    private WmoWeatherCodeMapper() {
        // Utility class
    }

    /**
     * Resolves a WMO weather interpretation code to a human-readable condition string.
     *
     * @param code WMO weather code (0 - 99)
     * @return Descriptive weather condition string
     */
    public static String getConditionDescription(int code) {
        switch (code) {
            case 0:
                return "Clear sky";
            case 1:
                return "Mainly clear";
            case 2:
                return "Partly cloudy";
            case 3:
                return "Overcast";
            case 45:
            case 48:
                return "Foggy";
            case 51:
                return "Light drizzle";
            case 53:
                return "Moderate drizzle";
            case 55:
                return "Dense drizzle";
            case 56:
            case 57:
                return "Freezing drizzle";
            case 61:
                return "Slight rain";
            case 63:
                return "Moderate rain";
            case 65:
                return "Heavy rain";
            case 66:
            case 67:
                return "Freezing rain";
            case 71:
                return "Slight snow fall";
            case 73:
                return "Moderate snow fall";
            case 75:
                return "Heavy snow fall";
            case 77:
                return "Snow grains";
            case 80:
                return "Slight rain showers";
            case 81:
                return "Moderate rain showers";
            case 82:
                return "Violent rain showers";
            case 85:
                return "Slight snow showers";
            case 86:
                return "Heavy snow showers";
            case 95:
                return "Thunderstorm";
            case 96:
            case 99:
                return "Thunderstorm with hail";
            default:
                if (code >= 51 && code <= 67) {
                    return "Rain";
                } else if (code >= 71 && code <= 86) {
                    return "Snow";
                } else if (code >= 95 && code <= 99) {
                    return "Thunderstorm";
                }
                return "Clear";
        }
    }

    /**
     * Resolves a WMO weather code to the asset picture path key used by the presentation layer.
     *
     * @param code WMO weather code (0 - 99)
     * @return Icon key matching WeatherIconMapper (e.g. "sunny", "cloudy", "rainy", "snowy", "storm")
     */
    public static String getIconPath(int code) {
        switch (code) {
            case 0:
                return "sunny";
            case 1:
                return "cloudy_sunny";
            case 2:
            case 3:
            case 45:
            case 48:
                return "cloudy";
            case 51:
            case 53:
            case 55:
            case 56:
            case 57:
            case 61:
            case 63:
            case 65:
            case 66:
            case 67:
            case 80:
            case 81:
            case 82:
                return "rainy";
            case 71:
            case 73:
            case 75:
            case 77:
            case 85:
            case 86:
                return "snowy";
            case 95:
            case 96:
            case 99:
                return "storm";
            default:
                if (code >= 51 && code <= 67) {
                    return "rainy";
                } else if (code >= 71 && code <= 86) {
                    return "snowy";
                } else if (code >= 95 && code <= 99) {
                    return "storm";
                }
                return "cloudy_sunny";
        }
    }
}
