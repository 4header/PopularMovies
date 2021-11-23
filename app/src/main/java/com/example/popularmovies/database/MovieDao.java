package com.example.popularmovies.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(MovieApp movie);

    @Query("DELETE FROM stored")
    void deleteAll();

    @Delete
    void delete(MovieApp movie);

    @Query("SELECT * FROM stored ORDER BY favs ASC")
    LiveData<List<MovieApp>> loadAllFavs();

    @Query("SELECT * FROM stored ORDER BY poster_favs ASC")
    LiveData<List<MovieApp>> loadPosters();
}
