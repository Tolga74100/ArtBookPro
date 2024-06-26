package com.tolga.artbookpro.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.tolga.artbookpro.model.Place;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface PlaceDao {
    @Query("SELECT * FROM Place")
    Flowable<List<Place>> getAll();

    @Insert
    Completable mapInsert(Place place);
    @Delete
    Completable mapDelete(Place place);


}
