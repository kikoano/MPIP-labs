package com.example.kikoano111.lab3;

/**
 * Created by kikoano111 on 6/12/2017.
 */

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(entities = {Movie.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {
    public abstract MovieDao movieDao();
}
