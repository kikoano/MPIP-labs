package com.example.kikoano111.lab4;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by kikoano111 on 20/12/2017.
 */

@Database(entities = {LocationEntity.class}, version = 3)
public abstract class LocationsDatabase extends RoomDatabase {
    public abstract LocationDao locationDao();
}
