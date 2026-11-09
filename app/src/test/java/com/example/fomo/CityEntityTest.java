package com.example.fomo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.example.fomo.data.local.entity.CityEntity;
import com.example.fomo.domain.model.City;
import org.junit.Test;

public class CityEntityTest {

    @Test
    public void testCityDomainPropertiesAndDisplayName() {
        City cityWithAdmin = new City(1, "Austin", "United States", "Texas", 30.2672, -97.7431, true, 1000L);
        assertEquals(1, cityWithAdmin.getId());
        assertEquals("Austin", cityWithAdmin.getName());
        assertEquals("United States", cityWithAdmin.getCountry());
        assertEquals("Texas", cityWithAdmin.getAdminRegion());
        assertEquals(30.2672, cityWithAdmin.getLatitude(), 0.0001);
        assertEquals(-97.7431, cityWithAdmin.getLongitude(), 0.0001);
        assertTrue(cityWithAdmin.isFavorite());
        assertEquals(1000L, cityWithAdmin.getSelectedAt());
        assertEquals("Austin, Texas, United States", cityWithAdmin.getDisplayName());

        City cityWithoutAdmin = new City(2, "Tokyo", "Japan", null, 35.6762, 139.6503, false, 2000L);
        assertEquals("Tokyo, Japan", cityWithoutAdmin.getDisplayName());

        City cityWithDuplicateAdmin = new City(3, "New York", "United States", "New York", 40.7128, -74.0060, true, 3000L);
        assertEquals("New York, United States", cityWithDuplicateAdmin.getDisplayName());
    }

    @Test
    public void testCityWithFavoriteAndWithSelectedAt() {
        City original = new City(1, "Paris", "France", "Ile-de-France", 48.8566, 2.3522, false, 500L);
        City favorited = original.withFavorite(true);
        assertTrue(favorited.isFavorite());
        assertEquals(original.getId(), favorited.getId());

        City updatedSelection = favorited.withSelectedAt(9999L);
        assertEquals(9999L, updatedSelection.getSelectedAt());
    }

    @Test
    public void testCityEqualityAndHashCode() {
        City c1 = new City(1, "London", "United Kingdom", "England", 51.5074, -0.1278, true, 10L);
        City c2 = new City(1, "London", "United Kingdom", "England", 51.5074, -0.1278, true, 10L);
        City c3 = new City(2, "Berlin", "Germany", null, 52.5200, 13.4050, false, 20L);

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
        assertNotEquals(c1, c3);
    }

    @Test
    public void testCityEntityMappingProperties() {
        CityEntity entity = new CityEntity(10, "Rome", "Italy", "Lazio", 41.9028, 12.4964, true, 500L);
        assertEquals(10, entity.id);
        assertEquals("Rome", entity.name);
        assertEquals("Italy", entity.country);
        assertEquals("Lazio", entity.adminRegion);
        assertEquals(41.9028, entity.latitude, 0.0001);
        assertEquals(12.4964, entity.longitude, 0.0001);
        assertTrue(entity.isFavorite);
        assertEquals(500L, entity.selectedAt);
    }
}

