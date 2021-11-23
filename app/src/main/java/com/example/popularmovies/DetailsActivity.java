package com.example.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.popularmovies.database.MovieApp;
import com.example.popularmovies.database.MovieDatabase;
import com.example.popularmovies.database.MovieViewModel;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {
    @BindView(R.id.det_image) ImageView det_image;
    @BindView(R.id.det_description) TextView det_desc;
    @BindView(R.id.det_backdrop) ImageView det_bg;
    @BindView(R.id.det_title) TextView det_title;
    @BindView(R.id.det_release_date) TextView det_rel;
    @BindView(R.id.det_vote_average) TextView det_vote;
    MovieApp movieApp;
    MovieDatabase db;
    Button play, share;
    MenuItem menuItem;
    private URL url;
    private int safeStar = -1;
    public static String id, holder;
    private String dbImg, dbDesc, dbBg, dbTitle, dbRel, dbVote, bgId;
    private final String KEY = "key";
    private final String CONTENT = "content";
    private final String SAVED = "saved";
    private final String BOOL = "bool";
    private String key, content, link;
    private boolean starred;
    private static final String TAG = "DetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            safeStar = savedInstanceState.getInt(SAVED);
            starred = savedInstanceState.getBoolean(BOOL);
        }

        Intent intent = getIntent();
        if (intent != null) {
            Parcel vars = intent.getParcelableExtra("parcel");
            if (MainActivity.disable) {
                dbImg = vars.getImg();
                dbBg = vars.getBg();
            }else {
                dbImg = ("https://image.tmdb.org/t/p/w185/" + vars.getImg());
                dbBg = ("https://image.tmdb.org/t/p/w185/" + vars.getBg());
            }
            Picasso.get().load(dbImg).into(det_image);

            dbDesc = vars.getDesc();
            det_desc.setText(dbDesc);

            Picasso.get().load(dbBg).into(det_bg);

            dbTitle = vars.getTitle();
            det_title.setText(dbTitle);

            dbRel = vars.getRelease();
            det_rel.setText(dbRel);

            dbVote = vars.getVote();
            det_vote.setText(dbVote);

            bgId = vars.getId();
        }

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        db = MovieDatabase.getDatabase(getApplicationContext());

        movieApp = new MovieApp(dbImg, dbDesc, dbBg, dbTitle, dbRel, dbVote, bgId, getReviews(), link);

        id = bgId;
        holder = dbTitle;

       init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (menuItem != null && DataAdapter.favs.contains(dbTitle)) {
            menuItem.setIcon(R.drawable.ic_star_black_24dp);
        }else if (menuItem != null) {
            menuItem.setIcon(R.drawable.ic_star_border_black_24dp);
            holder = dbTitle;
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED, safeStar);
        outState.putBoolean(BOOL, starred);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details, menu);

        menuItem = menu.findItem(R.id.star);
        if (DataAdapter.favs.contains(dbTitle)) {
            menuItem.setIcon(R.drawable.ic_star_black_24dp);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull final MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.preview:
                play = findViewById(R.id.play);
                play.setVisibility(View.VISIBLE);
                play.setText(R.string.play);
                share = findViewById(R.id.share);
                share.setText(R.string.share);
                share.setVisibility(View.VISIBLE);
                break;
            case R.id.review:
                det_desc.setText(getReviews());
                break;
            case R.id.overflow_desc:
                det_desc.setText(dbDesc);
                break;
            case R.id.star:
                if (movieApp.getTitle().equals(holder) && !DataAdapter.favs.contains(holder)) {
                    item.setIcon(R.drawable.ic_star_black_24dp);
                    holder = "changed";
                    movieApp.setFavs(dbTitle);
                    movieApp.setImage(dbImg);
                    DataAdapter.favs.add(dbTitle);
                    MovieDatabase.databaseWriteExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            db.movieDao().insert(movieApp);
                        }
                    });
                }else {
                    item.setIcon(R.drawable.ic_star_border_black_24dp);
                    holder = dbTitle;

                    DataAdapter.favs.remove(dbTitle);
                    MovieDatabase.databaseWriteExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            db.movieDao().delete(movieApp);
                        }
                    });
                }
                break;
            case R.id.favourites:
                invisible();
                Intent intent = new Intent(this, Favourites.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void thread(final String uri, final String value) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Fetch Youtube link
                    if (value.equals(KEY)) {
                        url = NetRequest.YTUrl(uri);
                        key = NetRequest.getYTLink(NetRequest.request(url), value);
                        // Fetch movie reviews
                    }else if (value.equals(CONTENT)) {
                        url = NetRequest.urlCrafter(apiContentResolver());
                        content = NetRequest.getYTLink(NetRequest.request(url), value);
                    }
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
    }

    public static String apiIdResolver() {
        return "https://api.themoviedb.org/3/movie/" + id + "/videos";
    }

    public static String apiContentResolver() {
        return "https://api.themoviedb.org/3/movie/" + id + "/reviews";
    }

    public void play(View view) {
        if (link == null) {
            thread(apiIdResolver(), KEY);
            link = "https://www.youtube.com/watch?v=" + key;
        }
        Intent yt = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + key));
        invisible();
        startActivity(yt);
    }

    public void share(View view) {
        if (link == null) {
            thread(apiIdResolver(), KEY);
            link = "https://www.youtube.com/watch?v=" + key;
        }
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
        share.putExtra(Intent.EXTRA_TEXT, link);
        invisible();
        startActivity(Intent.createChooser(share, "Share URL"));
    }

    public void cancel(View view) {
        invisible();
    }

    private String getReviews() {
        thread(apiContentResolver(), CONTENT);
        if (content != null) {
            return content;
        }else {
            return "No reviews at this time.";
        }
    }

    // Hide buttons
    private void invisible() {
        if (play != null && share != null) {
            play.setVisibility(View.INVISIBLE);
            share.setVisibility(View.INVISIBLE);
        }
    }

    private void init() {
        final MovieViewModel viewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        viewModel.getDbFavs().observe(this, new Observer<List<MovieApp>>() {
            @Override
            public void onChanged(List<MovieApp> favs) {
                viewModel.getDbFavs().removeObserver(this);
            }
        });
    }
}
