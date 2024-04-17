package com.tolga.artbookpro.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.Contract;

import java.io.Serializable;

@Entity(tableName = "Art")
public class Art implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int artid;
    @ColumnInfo(name = "artName")
    public String artName;
    @ColumnInfo(name = "artistName")
    public String artistName;
    @ColumnInfo(name = "year")
    public  String year;
    @ColumnInfo(name = "LocationName")
    public String locationName;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    public byte[] image;

    public Art (String artName, String artistName, String year, String locationName, byte[] image){
        this.artName = artName;
        this.artistName = artistName;
        this.year = year;
        this.image = image;
        this.locationName= locationName;
    }
}
