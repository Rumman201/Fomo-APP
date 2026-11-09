package com.example.fomo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.example.fomo.data.local.entity.CityEntity;
import com.example.fomo.data.local.mapper.CityMapper;
import com.example.fomo.data.remote.dto.GeocodingResultDto;
import com.example.fomo.domain.model.City;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

public class CityMapperTest {

    @Test
    public void testEntityToDomainAndReverse() {
        CityEntity entity = new CityEntity(5, "Madrid", "Spain", "Community of Madrid", 40.4168, -3.7038, true, 8000L);
        City domain = CityMapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(5, domain.getId());
        assertEquals("Madrid", domain.getName());
        assertEquals("Spain", domain.getCountry());
        assertEquals("Community of Madrid", domain.getAdminRegion());
        assertEquals(40.4168, domain.getLatitude(), 0.0001);
        assertEquals(-3.7038, domain.getLongitude(), 0.0001);
        assertTrue(domain.isFavorite());
        assertEquals(8000L, domain.getSelectedAt());

        CityEntity mappedBack = CityMapper.toEntity(domain);
        assertNotNull(mappedBack);
        assertEquals(entity.id, mappedBack.id);
        assertEquals(entity.name, mappedBack.name);
        assertEquals(entity.country, mappedBack.country);
        assertEquals(entity.adminRegion, mappedBack.adminRegion);
        assertEquals(entity.latitude, mappedBack.latitude, 0.0001);
        assertEquals(entity.longitude, mappedBack.longitude, 0.0001);
        assertEquals(entity.isFavorite, mappedBack.isFavorite);
        assertEquals(entity.selectedAt, mappedBack.selectedAt);
    }

    @Test
    public void testGeocodingDtoToDomain() {
        GeocodingResultDto dto = new GeocodingResultDto(
                12345L, "Cairo", 30.0444, 31.2357, "Egypt", "Cairo Governorate", "Africa/Cairo"
        );
        City domain = CityMapper.fromGeocodingDto(dto);

        assertNotNull(domain);
        assertEquals(0, domain.getId()); // ID 0 before DB insertion
        assertEquals("Cairo", domain.getName());
        assertEquals("Egypt", domain.getCountry());
        assertEquals("Cairo Governorate", domain.getAdminRegion());
        assertEquals(30.0444, domain.getLatitude(), 0.0001);
        assertEquals(31.2357, domain.getLongitude(), 0.0001);
        assertFalse(domain.isFavorite());
    }

    @Test
    public void testNullAndEmptyListHandling() {
        assertNull(CityMapper.toDomain(null));
        assertNull(CityMapper.toEntity(null));
        assertNull(CityMapper.fromGeocodingDto(null));

        assertTrue(CityMapper.toDomainList(null).isEmpty());
        assertTrue(CityMapper.toDomainList(Collections.emptyList()).isEmpty());
        assertTrue(CityMapper.fromGeocodingDtoList(null).isEmpty());
        assertTrue(CityMapper.fromGeocodingDtoList(Collections.emptyList()).isEmpty());

        List<CityEntity> entities = Collections.singletonList(
                new CityEntity(1, "Sydney", "Australia", "New South Wales", -33.8688, 151.2093, false, 0L)
        );
        assertEquals(1, CityMapper.toDomainList(entities).size());
    }
}

