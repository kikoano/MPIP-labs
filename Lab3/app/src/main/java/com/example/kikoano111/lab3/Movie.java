package com.example.kikoano111.lab3;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

/**
 * Created by kikoano111 on 6/12/2017.
 */
@Entity
public class Movie {

    @PrimaryKey
    @NonNull
    public String id;

    public String title;
    public String year;
    public String poster;

    public Movie(String id, String title, String year, String poster) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.poster = poster;
    }
}
