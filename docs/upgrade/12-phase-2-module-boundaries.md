# 12 — Phase 2 Module Boundaries & Integration Contracts

## 1. Package Ownership & Responsibility Boundaries

The Fomo codebase enforces strict unidirectional dependency boundaries:

```text
[ Presentation Layer: com.example.fomo.ui.* ]
                     ↓ (Depends on)
  [ Domain Layer: com.example.fomo.domain.* ]
                     ↑ (Implemented by)
    [ Data Layer: com.example.fomo.data.* ]
```

---

## 2. Module Specifications

### A. Presentation Layer (`com.example.fomo.ui`)
- **Sub-packages**:
  - `ui.main`: UI controllers (`MainActivity`).
  - `ui.adapter`: RecyclerView adapters (`HourlyAdapter`), ViewHolders, compiled drawable mappers (`WeatherIconMapper`).
  - `ui.viewmodel`: Lifecycle-aware state holders (`WeatherViewModel`), immutable state contracts (`WeatherUiState`).
- **Allowed Dependencies**: AndroidX (`AppCompatActivity`, `ViewModel`, `LiveData`), Jetpack ViewBinding, Domain Layer (`domain.model`, `domain.repository`).
- **Forbidden Dependencies**: Direct HTTP networking clients, SQLite/Room DAOs, raw data sources.

### B. Domain Layer (`com.example.fomo.domain`)
- **Sub-packages**:
  - `domain.model`: Pure business entities (`Hourly`, `CurrentWeather`, `WeatherForecast`).
  - `domain.repository`: Abstract repository contracts (`WeatherRepository`).
- **Allowed Dependencies**: AndroidX annotations (`@NonNull`, `@Nullable`), standard Java libraries (`java.util.*`).
- **Forbidden Dependencies**: Android UI (`View`, `Activity`, `Context`), Android Resources (`R.drawable`), external networking/persistence libraries.

### C. Data Layer (`com.example.fomo.data`)
- **Sub-packages**:
  - `data.repository`: Repository implementations (`InMemoryWeatherRepository`, and in Phase 3, `WeatherRepositoryImpl`).
- **Allowed Dependencies**: Domain layer (`domain.model`, `domain.repository`), Android threading primitives (`Looper`, `Handler`, `Executors`), Network & Database clients (Phase 3).
- **Forbidden Dependencies**: Presentation layer (`MainActivity`, `HourlyAdapter`, `WeatherViewModel`).

---

## 3. Public Integration Contracts

### `WeatherRepository` Interface
```java
public interface WeatherRepository {
    interface Callback<T> {
        void onSuccess(@NonNull T data);
        void onError(@NonNull Throwable error);
    }

    @NonNull
    WeatherForecast getSynchronousForecast();

    void getForecastAsync(@NonNull Callback<WeatherForecast> callback);
}
```

### `WeatherUiState` Contract
```java
public final class WeatherUiState {
    public enum Status { LOADING, SUCCESS, ERROR }
    public boolean isLoading();
    public boolean isSuccess();
    public boolean isError();
    @Nullable public WeatherForecast getData();
    @Nullable public String getErrorMessage();
}
```
