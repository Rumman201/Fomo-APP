package com.example.fomo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.example.fomo.data.remote.dto.IotStationTelemetryDto;
import com.example.fomo.domain.model.StationReading;
import com.example.fomo.domain.service.IotWeatherStationService;
import org.junit.Before;
import org.junit.Test;

public class IotWeatherStationServiceTest {

    private IotWeatherStationService service;

    @Before
    public void setUp() {
        service = new IotWeatherStationService();
    }

    @Test
    public void testValidStationTelemetryIngestion() {
        IotStationTelemetryDto dto = new IotStationTelemetryDto(
                "PWS-8842",
                System.currentTimeMillis(),
                22.4,
                58.0,
                1014.2,
                14.2,
                270,
                3.28
        );

        IotWeatherStationService.IngestionResult result = service.ingest(dto);
        assertTrue(result.isSuccess());
        assertEquals(IotWeatherStationService.Status.SUCCESS, result.getStatus());

        StationReading reading = result.getReading();
        assertNotNull(reading);
        assertEquals("PWS-8842", reading.getStationId());
        assertEquals(22.4, reading.getTemperatureC(), 0.001);
        assertEquals(58.0, reading.getHumidityPercent(), 0.001);
        assertEquals(1014.2, reading.getPressureHpa(), 0.001);
        assertEquals(14.2, reading.getWindSpeedKmh(), 0.001);
        assertEquals(270, reading.getWindDirectionDegrees());
        assertEquals(3.28, reading.getBatteryVoltage(), 0.001);
    }

    @Test
    public void testOutOfBoundsTemperatureRejection() {
        IotStationTelemetryDto coldDto = new IotStationTelemetryDto(
                "PWS-01", System.currentTimeMillis(), -105.0, 50.0, 1013.0, 10.0, 0, 3.3
        );
        IotWeatherStationService.IngestionResult coldResult = service.ingest(coldDto);
        assertFalse(coldResult.isSuccess());
        assertEquals(IotWeatherStationService.Status.OUT_OF_BOUNDS_TEMPERATURE, coldResult.getStatus());
        assertNull(coldResult.getReading());

        IotStationTelemetryDto hotDto = new IotStationTelemetryDto(
                "PWS-01", System.currentTimeMillis(), 75.0, 50.0, 1013.0, 10.0, 0, 3.3
        );
        IotWeatherStationService.IngestionResult hotResult = service.ingest(hotDto);
        assertFalse(hotResult.isSuccess());
        assertEquals(IotWeatherStationService.Status.OUT_OF_BOUNDS_TEMPERATURE, hotResult.getStatus());
    }

    @Test
    public void testOutOfBoundsHumidityAndPressureRejection() {
        IotStationTelemetryDto humidityDto = new IotStationTelemetryDto(
                "PWS-02", System.currentTimeMillis(), 20.0, 105.0, 1013.0, 10.0, 0, 3.3
        );
        IotWeatherStationService.IngestionResult humidityResult = service.ingest(humidityDto);
        assertFalse(humidityResult.isSuccess());
        assertEquals(IotWeatherStationService.Status.OUT_OF_BOUNDS_HUMIDITY, humidityResult.getStatus());

        IotStationTelemetryDto pressureDto = new IotStationTelemetryDto(
                "PWS-03", System.currentTimeMillis(), 20.0, 50.0, 750.0, 10.0, 0, 3.3
        );
        IotWeatherStationService.IngestionResult pressureResult = service.ingest(pressureDto);
        assertFalse(pressureResult.isSuccess());
        assertEquals(IotWeatherStationService.Status.OUT_OF_BOUNDS_PRESSURE, pressureResult.getStatus());
    }

    @Test
    public void testCorruptPayloadRejection() {
        IotWeatherStationService.IngestionResult nullResult = service.ingest(null);
        assertFalse(nullResult.isSuccess());
        assertEquals(IotWeatherStationService.Status.CORRUPT_PAYLOAD, nullResult.getStatus());

        IotStationTelemetryDto emptyStationDto = new IotStationTelemetryDto(
                "", System.currentTimeMillis(), 20.0, 50.0, 1013.0, 10.0, 0, 3.3
        );
        IotWeatherStationService.IngestionResult emptyResult = service.ingest(emptyStationDto);
        assertFalse(emptyResult.isSuccess());
        assertEquals(IotWeatherStationService.Status.CORRUPT_PAYLOAD, emptyResult.getStatus());
    }

    @Test
    public void testSimulatedStationGeneration() {
        IotStationTelemetryDto sim = service.generateSimulatedTelemetry("SIM-STATION-01");
        assertNotNull(sim);
        assertEquals("SIM-STATION-01", sim.getStationId());

        IotWeatherStationService.IngestionResult ingested = service.ingest(sim);
        assertTrue(ingested.isSuccess());
    }
}

