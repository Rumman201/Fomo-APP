package com.example.fomo.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Room database entity storing an hourly forecast item for offline viewing.
 */
@Entity(tableName = "hourly_forecast")
public class HourlyEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public int hourOrder;
    public String hour;
    public int temp;
    public String picPath;

    public HourlyEntity() {
    }

    @androidx.room.Ignore
    public HourlyEntity(int hourOrder, String hour, int temp, String picPath) {
        this.hourOrder = hourOrder;
        this.hour = hour;
        this.temp = temp;
        this.picPath = picPath;
    }
}
