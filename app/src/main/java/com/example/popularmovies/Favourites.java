package com.example.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.example.popularmovies.database.MovieApp;
import com.example.popularmovies.database.MovieDatabase;
import com.example.popularmovies.database.MovieViewModel;

import java.util.Collections;
import java.util.List;

public class Favourites extends AppCompatActivity {

    RecyclerView mRecycler;
    FavAdapter mFAdapter;
    MovieDatabase db;
    Button confirm, cancel;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favourites, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.clear_all:
                MovieDatabase.databaseWriteExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        db.movieDao().deleteAll();
                        DataAdapter.favs.clear();
                        DataAdapter.favMovies.clear();
                    }
                });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        mRecycler = findViewById(R.id.fav_recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mFAdapter = new FavAdapter(this);
        mRecycler.setAdapter(mFAdapter);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        db = MovieDatabase.getDatabase(getApplicationContext());
        setViewModel();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                MovieDatabase.databaseWriteExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        // Query
                        List<MovieApp> movies = mFAdapter.getFavMovies();

                        if (DataAdapter.favs.size() > 0) {
                            // Sync alphabetically with query
                            Collections.sort(DataAdapter.favs);
                            DataAdapter.favs.remove(position);
                        }
                        db.movieDao().delete(movies.get(position));
                    }
                });
            }
        }).attachToRecyclerView(mRecycler);
    }

    private void setViewModel() {
        MovieViewModel viewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        viewModel.getDbFavs().observe(this, new Observer<List<MovieApp>>() {
            @Override
            public void onChanged(List<MovieApp> favs) {
                mFAdapter.setFavMovies(favs);
            }
        });
    }

    // unused. Conflicts with frame root tag
//    public void confirm_clear(View view) {
//        db.movieDao().deleteAll();
//        invisible();
//    }
//
//    public void cancel_clear(View view) {
//        invisible();
//    }
//
//    private void invisible() {
//        if (confirm != null && cancel != null) {
//            confirm.setVisibility(View.INVISIBLE);
//            cancel.setVisibility(View.INVISIBLE);
//        }
//    }
}
