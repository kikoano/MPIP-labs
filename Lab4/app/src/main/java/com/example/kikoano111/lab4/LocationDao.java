package com.example.kikoano111.lab4;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by kikoano111 on 20/12/2017.
 */
@Dao
public interface LocationDao {
    @Query("SELECT * FROM locations")
    List<LocationEntity> getAll();

    @Insert
    void insertAll(LocationEntity... locationEntity);

    @Query("DELETE FROM locations")
    void deleteAll();
}
