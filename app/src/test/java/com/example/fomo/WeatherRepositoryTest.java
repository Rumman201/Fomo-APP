package com.example.fomo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import androidx.annotation.NonNull;

import com.example.fomo.data.repository.InMemoryWeatherRepository;
import com.example.fomo.domain.model.WeatherForecast;
import com.example.fomo.domain.repository.WeatherRepository;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Unit tests for WeatherRepository contract compliance.
 */
public class WeatherRepositoryTest {

    private WeatherRepository repository;

    @Before
    public void setUp() {
        // Direct synchronous execution for unit test isolation
        repository = new InMemoryWeatherRepository(Runnable::run, Runnable::run);
    }

    @Test
    public void testSynchronousForecastContract() {
        WeatherForecast forecast = repository.getSynchronousForecast();

        assertNotNull(forecast);
        assertNotNull(forecast.getCurrentWeather());
        assertNotNull(forecast.getHourlyForecasts());
        assertFalse(forecast.getHourlyForecasts().isEmpty());
        assertEquals(5, forecast.getHourlyForecasts().size());
        assertEquals("Mostly Cloudy", forecast.getCurrentWeather().getCondition());
    }

    @Test
    public void testForecastAsyncContract() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean successCalled = new AtomicBoolean(false);

        repository.getForecastAsync(new WeatherRepository.Callback<WeatherForecast>() {
            @Override
            public void onSuccess(@NonNull WeatherForecast data) {
                assertNotNull(data);
                assertEquals(5, data.getHourlyForecasts().size());
                successCalled.set(true);
                latch.countDown();
            }

            @Override
            public void onError(@NonNull Throwable error) {
                latch.countDown();
            }
        });

        assertTrue(latch.await(2, TimeUnit.SECONDS));
        assertTrue(successCalled.get());
    }
}
