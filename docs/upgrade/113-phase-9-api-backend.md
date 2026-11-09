# 113 — Phase 9 Backend API & Integration Contract Verification

## REST API Integration Verification

Fomo communicates with Open-Meteo REST APIs via Retrofit 2 and OkHttp:

---

## 1. Open-Meteo Forecast Contract
- **Endpoint**: `https://api.open-meteo.com/v1/forecast`
- **Parameters**: `latitude`, `longitude`, `current=temperature_2m,relative_humidity_2m,apparent_temperature,precipitation,weather_code,wind_speed_10m`, `hourly=temperature_2m,weather_code`, `daily=weather_code,temperature_2m_max,temperature_2m_min`.
- **Verified by**: `OpenMeteoJsonContractTest.java` and `WeatherDtoMapperTest.java`.
- **Deserialization**: Stream-based Gson parsing handles missing fields and null safety without throwing unhandled runtime exceptions.

---

## 2. Geocoding Search Contract
- **Endpoint**: `https://geocoding-api.open-meteo.com/v1/search`
- **Parameters**: `name`, `count=10`, `language=en`, `format=json`.
- **Verified by**: `GeocodingDtoTest.java` and `CityMapperTest.java`.
- **Input Sanitization**: Search query passed through `InputSanitizer.sanitizeSearchQuery()` before dispatch, bounding length to 100 characters and stripping SQL/XSS tokens.

