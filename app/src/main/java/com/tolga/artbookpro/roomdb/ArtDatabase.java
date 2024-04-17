package com.tolga.artbookpro.roomdb;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.tolga.artbookpro.model.Art;
import com.tolga.artbookpro.model.Place;

@Database(entities = {Art.class,Place.class}, version = 1)
public abstract class ArtDatabase extends RoomDatabase {
    public abstract ArtDao artDao();
    public abstract PlaceDao placeDao();
}



