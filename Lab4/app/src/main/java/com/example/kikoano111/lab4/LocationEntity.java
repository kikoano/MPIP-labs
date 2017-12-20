package com.example.kikoano111.lab4;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by kikoano111 on 20/12/2017.
 */

@Entity(tableName = "locations")
public class LocationEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    public double lat;

    public double lng;

    public String name;
}

