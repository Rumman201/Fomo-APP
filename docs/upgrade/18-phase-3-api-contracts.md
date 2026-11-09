# 18 — Phase 3 API Contracts & Integration Specification

## 1. External Weather API Contract: Open-Meteo REST API

- **Base URL**: `https://api.open-meteo.com/`
- **Protocol**: HTTPS (enforced via `network_security_config.xml`)
- **Authentication**: Keyless public meteorological API (zero secrets or credentials stored in repository).
- **Target Endpoint**: `GET /v1/forecast`

### Query Parameter Specification

| Parameter | Type | Required | Default Value | Description |
| :--- | :--- | :--- | :--- | :--- |
| `latitude` | `double` | Yes | `40.7128` | WGS84 latitude coordinate. |
| `longitude` | `double` | Yes | `-74.0060` | WGS84 longitude coordinate. |
| `current` | `String` | Yes | `temperature_2m,relative_humidity_2m,apparent_temperature,precipitation,weather_code,wind_speed_10m` | Comma-separated list of current weather metrics. |
| `hourly` | `String` | Yes | `temperature_2m,weather_code` | Comma-separated list of hourly forecast projections. |
| `timezone` | `String` | No | `auto` | Timezone resolution for timestamp alignment. |

---

## 2. Remote Response Data Transfer Objects (DTOs)

- [`OpenMeteoWeatherResponse`](file:///h:/Projects/Fomo-App/app/src/main/java/com/example/fomo/data/remote/dto/OpenMeteoWeatherResponse.java): Root payload containing `latitude`, `longitude`, `timezone`, `current`, and `hourly`.
- [`CurrentWeatherDto`](file:///h:/Projects/Fomo-App/app/src/main/java/com/example/fomo/data/remote/dto/CurrentWeatherDto.java): Serialized metrics for current temperature, humidity, precipitation, wind speed, and WMO weather code.
- [`HourlyForecastDto`](file:///h:/Projects/Fomo-App/app/src/main/java/com/example/fomo/data/remote/dto/HourlyForecastDto.java): Parallel arrays of ISO timestamps (`time`), temperatures (`temperature_2m`), and WMO weather codes (`weather_code`).

---

## 3. Boundary Mapping & Defensive Deserialization

All DTOs are mapped to Domain entities through [`WeatherDtoMapper`](file:///h:/Projects/Fomo-App/app/src/main/java/com/example/fomo/data/remote/mapper/WeatherDtoMapper.java):
1. **Null Safety**: All DTO accessors guard against null values with sensible meteorological defaults (e.g., 20°C, 50% humidity).
2. **Time Parsing**: ISO timestamps (e.g., `"2026-09-01T14:00"`) are parsed safely without modern Java 8 desugaring issues, converting to standard 12-hour strings (`"2 pm"`).
3. **High/Low Calculation**: The mapper scans the first 24 hours of forecast points to compute the day's high and low bounds.
