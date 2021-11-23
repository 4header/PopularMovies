package com.example.popularmovies.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private MovieRepository mRepository;

    private LiveData<List<MovieApp>> movies, posters;

    public MovieViewModel (Application application) {
        super(application);
        Log.v("VIEW", "Actively retrieving the tasks from the DataBase");
        mRepository = new MovieRepository(application);
        movies = mRepository.getDbFavs();
        posters = mRepository.getDbPosters();
    }

    public LiveData<List<MovieApp>> getDbFavs() { return movies; }

    public LiveData<List<MovieApp>> getDbPosters() { return posters; }

}
