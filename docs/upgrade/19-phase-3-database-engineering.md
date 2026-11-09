# 19 — Phase 3 Database & Persistence Engineering

## 1. Local Database Architecture

- **Engine**: SQLite managed via Android Jetpack Room 2.6.1 ORM.
- **Database Class**: [`FomoDatabase`](file:///h:/Projects/Fomo-App/app/src/main/java/com/example/fomo/data/local/database/FomoDatabase.java) (`fomo_weather.db`).
- **Schema Strategy**: Single-source-of-truth offline cache.
- **Export Schema**: `exportSchema = false` (baseline version 1).

---

## 2. Table Schema Definitions

### Table 1: `weather_cache`
Stores the current observation snapshot, meteorological status, and daily high/low temperatures.

| Column Name | SQL Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `INTEGER` | `PRIMARY KEY` (Fixed at 1) | Single-row singleton cache identifier. |
| `cachedTimestamp` | `INTEGER` | `NOT NULL` | Epoch timestamp (millis) when data was fetched. |
| `condition` | `TEXT` | `NOT NULL` | Human-readable condition description (e.g. "Clear sky"). |
| `temperature` | `INTEGER` | `NOT NULL` | Current temperature in °C. |
| `dateTime` | `TEXT` | `NOT NULL` | Formatted observation string (e.g. "Today \| 12:00 PM"). |
| `highLow` | `TEXT` | `NOT NULL` | Daily bounds string (e.g. "H:28 L:18"). |
| `rainPercentage` | `TEXT` | `NOT NULL` | Formatted rain probability (e.g. "20%"). |
| `windSpeed` | `TEXT` | `NOT NULL` | Formatted wind speed (e.g. "12 Km/h"). |
| `humidityPercentage` | `TEXT` | `NOT NULL` | Formatted relative humidity (e.g. "60%"). |
| `iconKey` | `TEXT` | `NOT NULL` | Icon key mapping to compiled drawable resources. |

### Table 2: `hourly_forecast`
Stores the 24-hour chronological forecast items.

| Column Name | SQL Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `INTEGER` | `PRIMARY KEY AUTOINCREMENT` | Auto-generated row ID. |
| `hourOrder` | `INTEGER` | `NOT NULL` | Zero-indexed chronological display order. |
| `hour` | `TEXT` | `NOT NULL` | 12-hour formatted time string (e.g. "12 pm"). |
| `temp` | `INTEGER` | `NOT NULL` | Forecasted temperature in °C. |
| `picPath` | `TEXT` | `NOT NULL` | Icon key mapping to compiled drawable resources. |

---

## 3. Atomic Transaction Management

To prevent inconsistent reads or partial cache writes, [`WeatherDao`](file:///h:/Projects/Fomo-App/app/src/main/java/com/example/fomo/data/local/dao/WeatherDao.java) implements an atomic transaction method:

```java
@Transaction
default void replaceCache(WeatherEntity weather, List<HourlyEntity> hourly) {
    clearWeatherCache();
    clearHourlyForecasts();
    insertWeatherCache(weather);
    if (hourly != null && !hourly.isEmpty()) {
        insertHourlyForecasts(hourly);
    }
}
```

This guarantees that weather state and hourly cards are never mismatched.
