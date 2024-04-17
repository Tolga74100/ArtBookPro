package com.tolga.artbookpro.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.tolga.artbookpro.model.Art;
import com.tolga.artbookpro.model.ArtAndPlace;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface ArtDao {

    @Transaction
    @Query("SELECT  * FROM Art")
    Flowable<List<ArtAndPlace>> getArtAndPlace ();
    @Query("SELECT  * FROM Art")
    Flowable<List<Art>> getAll();

    @Insert
    Completable artInsert(Art art);

    @Delete
    Completable artDelete(Art art);
}
