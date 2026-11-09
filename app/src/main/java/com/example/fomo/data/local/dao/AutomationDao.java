package com.example.fomo.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.fomo.data.local.entity.AutomationEntity;
import java.util.List;

/**
 * Data Access Object (DAO) for managing intelligent weather automation audit logs.
 */
@Dao
public interface AutomationDao {

    @Query("SELECT * FROM automation_logs ORDER BY timestamp DESC LIMIT :limit")
    List<AutomationEntity> getRecentLogs(int limit);

    @Query("SELECT * FROM automation_logs WHERE triggerType = :triggerType ORDER BY timestamp DESC LIMIT 1")
    AutomationEntity getLatestLogForTrigger(String triggerType);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertLog(AutomationEntity log);

    @Query("DELETE FROM automation_logs WHERE timestamp < :cutoffTimestamp")
    int deleteOldLogs(long cutoffTimestamp);

    @Query("DELETE FROM automation_logs")
    void clearAllLogs();
}

