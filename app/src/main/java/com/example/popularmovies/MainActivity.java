package com.example.popularmovies;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.popularmovies.database.MovieApp;
import com.example.popularmovies.database.MovieViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recycler;
    DataAdapter dAdapter;
    Spinner spinner;
    MenuItem refresher;
    Snackbar snackbar;
    private List<String> imgArray, descArray, backdropArray, titleArray, releaseArray, voteArray, idArray;
    private static final String TAG = "MainActivity";
    private final String NOW_PLAYING = "https://api.themoviedb.org/3/movie/now_playing";
    private final String TOP_RATED = "https://api.themoviedb.org/3/movie/top_rated";
    private final String POPULAR = "https://api.themoviedb.org/3/movie/popular";
    private String hostName = null;
    private boolean visibility;
    public static boolean disable;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem menuItem = menu.findItem(R.id.spinner);
        refresher = menu.findItem(R.id.menu_refresh);
        snackbar = Snackbar.make(recycler, "No connection found", Snackbar.LENGTH_INDEFINITE);

        spinner = (Spinner) menuItem.getActionView();
        spinner.setPopupBackgroundResource(R.color.colorPrimaryDark);

        final ArrayAdapter<CharSequence> spinAdapter = ArrayAdapter.createFromResource(this,
                R.array.spinner, android.R.layout.simple_spinner_item);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinAdapter);

        if (imgArray.isEmpty()) {
            spinner.setVisibility(View.INVISIBLE);
            refresher.setVisible(false);
        }


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spinPos = (String) spinner.getItemAtPosition(position);
                switch (spinPos) {
                    case "Now Playing":
                        recycler.setBackgroundResource(R.color.colorPrimaryDark);
                        clearMovieGrid();
                        setHostName(NOW_PLAYING);
                        newThread(NOW_PLAYING);
                        connectionCheck();
                        disable = false;
                        break;
                    case "Most Popular":
                        recycler.setBackgroundResource(R.color.colorPrimaryDark);
                        clearMovieGrid();
                        setHostName(POPULAR);
                        newThread(POPULAR);
                        connectionCheck();
                        disable = false;
                        break;
                    case "Highest Rated":
                        recycler.setBackgroundResource(R.color.colorPrimaryDark);
                        clearMovieGrid();
                        setHostName(TOP_RATED);
                        newThread(TOP_RATED);
                        connectionCheck();
                        disable = false;
                        break;
                    case "Favourites":
                        recycler.setBackgroundResource(R.color.white);
                        clearMovieGrid();
                        setViewModel();
                        disable = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_refresh) {
            SwipeRefreshLayout refreshLayout = findViewById(R.id.refresh_layout);
            refreshLayout.setRefreshing(true);

            if (disable) {
                refreshLayout.setRefreshing(false);
                return true;
            }

            clearMovieGrid();
            newThread(getHostName());
            connectionCheck();

            Toast.makeText(getApplicationContext(), "Refreshed", Toast.LENGTH_SHORT).show();
            refreshLayout.setRefreshing(false);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setHostName(NOW_PLAYING);
        newThread(NOW_PLAYING);

        setFavsList();

        int[] stockPhoto = {R.drawable.kitten_cat_rush_lucky_cat_45170,
                R.drawable.short_coated_black_and_brown_puppy_in_white_and_red_polka_39317,
                R.drawable.close_up_portrait_of_lion_247502,
                R.drawable.adult_and_cub_tiger_on_snowfield_near_bare_trees_39629,
                R.drawable.bird_red_animal_colorful_40984,
                R.drawable.agriculture_animals_baby_blur_288621,
                R.drawable.flock_of_flamingo_1327405,
                R.drawable.zebras_on_zebra_247376,
                R.drawable.animal_blur_chameleon_close_up_567540,
                R.drawable.view_of_elephant_in_water_247431};

        recycler = findViewById(R.id.recyclerview_id);
        recycler.setBackgroundResource(R.color.colorPrimaryDark);

        String[] stockDesc = getResources().getStringArray(R.array.descriptions);

        recycler.setLayoutManager(new GridLayoutManager(this, 2));
        dAdapter = new DataAdapter(this, imgArray, descArray, backdropArray, titleArray,
                releaseArray, voteArray, idArray, stockPhoto, stockDesc);
        recycler.setAdapter(dAdapter);

        swipe();
    }

    private void swipe() {
        final SwipeRefreshLayout refreshLayout = findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (disable) {
                    refreshLayout.setRefreshing(false);
                    return;
                }
                clearMovieGrid();
                newThread(getHostName());
                connectionCheck();

                Toast.makeText(getApplicationContext(), "Refreshed", Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    public void newThread(String host) {
        final URL url = NetRequest.urlCrafter(host);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Internet check & parsing, else null
                    NetRequest.json(NetRequest.request(url));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Update/clear lists for viewholder passing
        imgArray = NetRequest.imgCache;
        descArray = NetRequest.descCache;
        backdropArray = NetRequest.backdropCache;
        titleArray = NetRequest.titleCache;
        releaseArray = NetRequest.releaseCache;
        voteArray = NetRequest.voteCache;
        idArray = NetRequest.idCache;
    }

    private void setHostName(String host) {
        hostName = host;
    }

    public String getHostName() {
        return hostName;
    }

    public void clearMovieGrid() {
        imgArray.clear();
        descArray.clear();
        backdropArray.clear();
        titleArray.clear();
        releaseArray.clear();
        voteArray.clear();
        idArray.clear();
        fixPos();
        // Scrap old reference to Lists
        dAdapter.notifyDataSetChanged();
    }

    // Trouble with spinner visibility when alternating online and offline
    public void connectionCheck() {
        if (!imgArray.isEmpty()) {
            refresher.setVisible(true);
            snackbar.dismiss();
        }else {
            refresher.setVisible(false);
            snackbar.show();
        }
    }

    // Resets adapter position
    private void fixPos() {
        recycler.scrollToPosition(0);
    }

    // Populate grid with favs
    private void setViewModel() {
        final MovieViewModel viewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        viewModel.getDbPosters().observe(this, new Observer<List<MovieApp>>() {
            @Override
            public void onChanged(List<MovieApp> favs) {
                dAdapter.setFavMovies(favs);
            }
        });
    }

    // Set stars on saved flicks
    private void setFavsList() {
        final MovieViewModel viewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        viewModel.getDbFavs().observe(this, new Observer<List<MovieApp>>() {
            @Override
            public void onChanged(List<MovieApp> favs) {
                dAdapter.setFavList(favs);
            }
        });
    }

    // Logging
    private void setVisibility(boolean setVis) {
        visibility = setVis;
    }

    private boolean getVisibility() {
        return visibility;
    }
}

