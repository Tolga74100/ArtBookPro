package com.tolga.artbookpro.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Place")
public class Place implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int placeid;
    @ColumnInfo(name ="mapName")
    public String mapName;
    @ColumnInfo(name = "latitude")
    public Double latitude;
    @ColumnInfo(name = "longitude")
    public Double longitude;

    public Place(String mapName, Double latitude, Double longitude){
        this.mapName = mapName;
        this.latitude = latitude;
        this.longitude = longitude;

    }
}
