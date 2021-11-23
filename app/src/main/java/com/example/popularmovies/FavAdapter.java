package com.example.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.database.MovieApp;

import java.util.List;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.DataHolder> {

    private List<MovieApp> favMovies;
    Context context;

    public class DataHolder extends RecyclerView.ViewHolder {

        TextView textView;

        DataHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.fav_title);
        }
    }

    public FavAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public FavAdapter.DataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.favourites_view, parent, false);
        return new DataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavAdapter.DataHolder holder, int position) {
        MovieApp movieApp = favMovies.get(position);
        String title = movieApp.getFavs();
        holder.textView.setText(title);
    }

    @Override
    public int getItemCount() {
        if (favMovies == null) {
            return 0;
        }
        return favMovies.size();
    }

    public void setFavMovies(List<MovieApp> movies) {
        favMovies = movies;
        notifyDataSetChanged();
    }

    public List<MovieApp> getFavMovies() {
        return favMovies;
    }
}
