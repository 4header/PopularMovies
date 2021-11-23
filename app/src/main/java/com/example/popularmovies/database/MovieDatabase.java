package com.example.popularmovies.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {MovieApp.class}, version = 2, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    public abstract MovieDao movieDao();

    private static MovieDatabase instance;
    private static final int THREADS = 10;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(THREADS);

    public static MovieDatabase getDatabase(Context context) {
        if (instance == null) {
            synchronized (MovieDatabase.class) {
                Log.d("TAG", "Creating new db instance");
                instance = Room.databaseBuilder(context.getApplicationContext(),
                        MovieDatabase.class, "movie_db")
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return instance;
    }
}
