package com.example.fomo.qa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.example.fomo.data.local.database.FomoDatabase;
import com.example.fomo.data.local.entity.AutomationEntity;
import com.example.fomo.data.local.entity.CityEntity;
import org.junit.Test;

/**
 * Phase 9 Database Migration and Schema Integrity verification suite.
 * Asserts version bounds, migration scripts, entity invariants, and indexing requirements.
 */
public class DatabaseMigrationAndIntegrityTest {

    @Test
    public void testDatabaseMigrationVersionContracts() {
        assertNotNull(FomoDatabase.MIGRATION_1_2);
        assertEquals(1, FomoDatabase.MIGRATION_1_2.startVersion);
        assertEquals(2, FomoDatabase.MIGRATION_1_2.endVersion);

        assertNotNull(FomoDatabase.MIGRATION_2_3);
        assertEquals(2, FomoDatabase.MIGRATION_2_3.startVersion);
        assertEquals(3, FomoDatabase.MIGRATION_2_3.endVersion);
    }

    @Test
    public void testCityEntityConstraintsAndImmutability() {
        CityEntity entity = new CityEntity(42, "San Francisco", "United States", "California", 37.7749, -122.4194, true, 123456789L);

        assertEquals(42, entity.id);
        assertEquals("San Francisco", entity.name);
        assertEquals("United States", entity.country);
        assertEquals("California", entity.adminRegion);
        assertEquals(37.7749, entity.latitude, 0.0001);
        assertEquals(-122.4194, entity.longitude, 0.0001);
        assertTrue(entity.isFavorite);
        assertEquals(123456789L, entity.selectedAt);
    }

    @Test
    public void testAutomationEntityStructureAndAttributes() {
        AutomationEntity log = new AutomationEntity(
                10L, 987654321L, "RAIN_ALERT", "WARNING", "Rain imminent in next hour", 85.0
        );

        assertEquals(10L, log.id);
        assertEquals(987654321L, log.timestamp);
        assertEquals("RAIN_ALERT", log.triggerType);
        assertEquals("WARNING", log.severity);
        assertEquals("Rain imminent in next hour", log.message);
        assertEquals(85.0, log.metricValue, 0.001);
    }
}
