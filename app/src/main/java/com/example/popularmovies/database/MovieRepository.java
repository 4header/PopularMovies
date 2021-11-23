package com.example.popularmovies.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

class MovieRepository {

    private MovieDao mDao;
    private LiveData<List<MovieApp>> movies, posters;

    MovieRepository(Application application) {
        MovieDatabase db = MovieDatabase.getDatabase(application);
        mDao = db.movieDao();
        movies = mDao.loadAllFavs();
        posters = mDao.loadPosters();
    }

    LiveData<List<MovieApp>> getDbFavs() {
        return movies;
    }

    LiveData<List<MovieApp>> getDbPosters() {
        return posters;
    }
}
