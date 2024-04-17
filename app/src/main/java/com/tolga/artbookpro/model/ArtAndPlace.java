package com.tolga.artbookpro.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.io.Serializable;
import java.util.List;

public class ArtAndPlace implements Serializable {
    @Embedded public Art art;
    @Relation(
            parentColumn = "artid",
            entityColumn = "placeid"
    )
    public Place place;
}
