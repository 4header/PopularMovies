package com.example.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class Offline extends AppCompatActivity {
    MainActivity refresh;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.offline, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.offline_menu_refresh) {
            SwipeRefreshLayout refreshLayout = findViewById(R.id.refresh_layout);
            refreshLayout.setRefreshing(true);

            refresh.clearMovieGrid();
            refresh.newThread(refresh.getHostName());
            refresh.connectionCheck();

            Toast.makeText(getApplicationContext(), "Refreshed", Toast.LENGTH_SHORT).show();
            refreshLayout.setRefreshing(false);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);

        ImageView off_image;
        TextView off_text;

        Intent intent = getIntent();
        if (intent != null) {

            if (intent.hasExtra("StockImg")) {
                off_image = findViewById(R.id.off_image);
                off_image.setImageResource(intent.getIntExtra("StockImg", 0));
            }
            if (intent.hasExtra("Description")) {
                off_text = findViewById(R.id.off_text);
                off_text.setText(intent.getStringExtra("Description"));
            }
        }
    }
}
