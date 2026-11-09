package com.example.fomo.data.remote.api;

import androidx.annotation.NonNull;
import java.util.concurrent.TimeUnit;
import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Enterprise factory and provider for resilient, authenticated HTTP networking clients
 * using Retrofit, OkHttp, and OkHttp CertificatePinner.
 */
public final class ApiClient {

    private static final long TIMEOUT_SECONDS = 15;

    // Standard public certificate pins for Open-Meteo infrastructure (ISRG Root X1 & backup roots)
    public static final String PIN_ISRG_ROOT_X1 = "sha256/C5+lpZ7tcVwmwQIMcRtPbsQtWLABXhQzejna0wHFr8M=";
    public static final String PIN_LE_R3_BACKUP = "sha256/jQJTbIh0grw0/1TkHSumWb+Fs0Ggogr621gT3PvPKG0=";
    public static final String PIN_ISRG_ROOT_X2 = "sha256/diuDEWzNEeCdUbal9uhqBsWM9ToPtdvgWJZ1BSRhUFA=";

    private static volatile WeatherApiService sWeatherInstance;
    private static volatile GeocodingApiService sGeocodingInstance;

    private ApiClient() {
        // Utility / Factory class
    }

    /**
     * Builds standard CertificatePinner for Open-Meteo endpoints.
     */
    @NonNull
    public static CertificatePinner buildCertificatePinner() {
        return new CertificatePinner.Builder()
                .add("api.open-meteo.com", PIN_ISRG_ROOT_X1)
                .add("api.open-meteo.com", PIN_LE_R3_BACKUP)
                .add("api.open-meteo.com", PIN_ISRG_ROOT_X2)
                .add("geocoding-api.open-meteo.com", PIN_ISRG_ROOT_X1)
                .add("geocoding-api.open-meteo.com", PIN_LE_R3_BACKUP)
                .add("geocoding-api.open-meteo.com", PIN_ISRG_ROOT_X2)
                .build();
    }

    /**
     * Returns the singleton instance of WeatherApiService configured with production defaults.
     */
    public static WeatherApiService getInstance() {
        if (sWeatherInstance == null) {
            synchronized (ApiClient.class) {
                if (sWeatherInstance == null) {
                    sWeatherInstance = createService(WeatherApiService.BASE_URL);
                }
            }
        }
        return sWeatherInstance;
    }

    /**
     * Returns the singleton instance of GeocodingApiService configured with production defaults.
     */
    public static GeocodingApiService getGeocodingService() {
        if (sGeocodingInstance == null) {
            synchronized (ApiClient.class) {
                if (sGeocodingInstance == null) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(GeocodingApiService.BASE_URL)
                            .client(buildOkHttpClient())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    sGeocodingInstance = retrofit.create(GeocodingApiService.class);
                }
            }
        }
        return sGeocodingInstance;
    }

    /**
     * Builds an OkHttpClient configured with timeout bounds, certificate pinning, and logging.
     */
    @NonNull
    public static OkHttpClient buildOkHttpClient() {
        return buildOkHttpClient(true);
    }

    /**
     * Builds an OkHttpClient with configurable certificate pinning.
     */
    @NonNull
    public static OkHttpClient buildOkHttpClient(boolean enablePinning) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .retryOnConnectionFailure(true);

        if (enablePinning) {
            builder.certificatePinner(buildCertificatePinner());
        }

        return builder.build();
    }

    /**
     * Factory method to create a WeatherApiService instance with a custom base URL.
     * Useful for pointing to MockWebServer during automated integration tests.
     */
    public static WeatherApiService createService(@NonNull String baseUrl) {
        return createService(baseUrl, buildOkHttpClient());
    }

    /**
     * Factory method to create a WeatherApiService instance with a custom base URL and OkHttpClient.
     */
    public static WeatherApiService createService(@NonNull String baseUrl, @NonNull OkHttpClient client) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(WeatherApiService.class);
    }
}
