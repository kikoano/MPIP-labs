package com.example.kikoano111.lab3;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by kikoano111 on 6/12/2017.
 */
@Dao
public interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Movie> movie);

    @Update
    void update(Movie movie);

    @Delete
    void delete(Movie movie);

    @Query("DELETE FROM Movie WHERE imdbID = :id")
    void deleteById(String id);

    @Query("SELECT * FROM Movie ORDER BY title")
    List<Movie> fetchAllData();
}
