package com.example.fomo.data.local.database;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.example.fomo.data.local.dao.AutomationDao;
import com.example.fomo.data.local.dao.CityDao;
import com.example.fomo.data.local.dao.WeatherDao;
import com.example.fomo.data.local.entity.AutomationEntity;
import com.example.fomo.data.local.entity.CityEntity;
import com.example.fomo.data.local.entity.HourlyEntity;
import com.example.fomo.data.local.entity.WeatherEntity;

/**
 * Main Room Database for Fomo local weather intelligence, multi-city persistence,
 * offline caching, and automation audit logs.
 */
@Database(
        entities = {WeatherEntity.class, HourlyEntity.class, CityEntity.class, AutomationEntity.class},
        version = 2,
        exportSchema = false
)
public abstract class FomoDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "fomo_weather.db";
    private static volatile FomoDatabase sInstance;

    public abstract WeatherDao weatherDao();
    public abstract CityDao cityDao();
    public abstract AutomationDao automationDao();

    /**
     * Migration from version 1 to version 2: adds `saved_cities` and `automation_logs` tables.
     */
    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `saved_cities` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`name` TEXT NOT NULL, " +
                    "`country` TEXT NOT NULL, " +
                    "`adminRegion` TEXT, " +
                    "`latitude` REAL NOT NULL, " +
                    "`longitude` REAL NOT NULL, " +
                    "`isFavorite` INTEGER NOT NULL, " +
                    "`selectedAt` INTEGER NOT NULL)");

            database.execSQL("CREATE TABLE IF NOT EXISTS `automation_logs` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`timestamp` INTEGER NOT NULL, " +
                    "`triggerType` TEXT NOT NULL, " +
                    "`severity` TEXT NOT NULL, " +
                    "`message` TEXT NOT NULL, " +
                    "`metricValue` REAL NOT NULL)");

            // Seed default initial city (New York) to maintain continuity
            database.execSQL("INSERT OR IGNORE INTO `saved_cities` " +
                    "(`id`, `name`, `country`, `adminRegion`, `latitude`, `longitude`, `isFavorite`, `selectedAt`) " +
                    "VALUES (1, 'New York', 'United States', 'New York', 40.7128, -74.0060, 1, 1)");
        }
    };

    /**
     * Obtains the thread-safe singleton instance of FomoDatabase.
     */
    public static FomoDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (FomoDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            FomoDatabase.class,
                            DATABASE_NAME
                    )
                    .addMigrations(MIGRATION_1_2)
                    .build();
                }
            }
        }
        return sInstance;
    }
}
