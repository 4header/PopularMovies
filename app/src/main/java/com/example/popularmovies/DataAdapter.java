package com.example.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.database.MovieApp;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataHolder>  {
    private int[] stockPhoto;
    public static List<MovieApp> favMovies = new ArrayList<>();
    private List<MovieApp> favTitles = new ArrayList<>();
    private List<String> imgArray, descArray, backdropArray, titleArray, releaseArray, voteArray, idArray;
    // Keep track of starred titles
    public static List<String> favs = new ArrayList<>();
    private String[] stockDesc;
    private Context context;
    private Intent intent;

    public DataAdapter(Context context, List<String> imgArray, List<String> descArray, List<String> backdropArray,
                       List<String> titleArray, List<String> releaseArray, List<String> voteArray,
                       List<String> idArray, int[] stockPhoto,  String[] stockDesc) {
        this.context = context;
        this.imgArray = imgArray;
        this.descArray = descArray;
        this.backdropArray = backdropArray;
        this.stockPhoto = stockPhoto;
        this.stockDesc = stockDesc;
        this.titleArray = titleArray;
        this.releaseArray = releaseArray;
        this.voteArray = voteArray;
        this.idArray = idArray;
    }

    public class DataHolder extends RecyclerView.ViewHolder {

        ImageView image;
        LinearLayout linlay;

        DataHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            linlay = itemView.findViewById(R.id.linlay);
        }
    }

    @Override
    public DataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.custom_view, parent, false);
        return new DataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DataHolder holder, final int position) {

        // Show starred films on creation
        if (!favTitles.isEmpty()) {
            for (MovieApp m: favTitles) {
                if (!favs.contains(m.getTitle())) {
                    favs.add(m.getTitle());
                }
            }
        }
        if (!imgArray.isEmpty()) {
            Picasso.get().load("https://image.tmdb.org/t/p/w185/" + imgArray.get(position)).into(holder.image);
        }else {
            MovieApp movieApp = favMovies.get(position);
            String pic = movieApp.getImage();
            Picasso.get().load(pic).into(holder.image);
        }
        // Memory limits on emulator
//        }else {
//            // So not having to stare at a blank screen
//            holder.image.setImageResource(stockPhoto[position]);
//        }
        holder.linlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!imgArray.isEmpty()) {
                    Parcel vars = new Parcel(
                            imgArray.get(position),
                            descArray.get(position),
                            backdropArray.get(position),
                            titleArray.get(position),
                            releaseArray.get(position),
                            voteArray.get(position),
                            idArray.get(position));
                    intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("parcel", vars);
                    context.startActivity(intent);
                }else {
                    String img, desc, bg, title, rel, vote, id;
                    img = favMovies.get(position).getImage();
                    desc = favMovies.get(position).getDescriptions();
                    bg = favMovies.get(position).getBackdrop();
                    title = favMovies.get(position).getTitle();
                    rel = favMovies.get(position).getRelease();
                    vote = favMovies.get(position).getVote();
                    id = favMovies.get(position).getId();

                    Parcel vars = new Parcel(img, desc, bg, title, rel, vote, id);
                    intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("parcel", vars);
                    context.startActivity(intent);
                }
                // Memory limits on emulator
//                } else {
//                    intent = new Intent(context, Offline.class);
//                    intent.putExtra("StockImg", stockPhoto[position]);
//                    intent.putExtra("Description", stockDesc[position]);
//                    context.startActivity(intent);
//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (!imgArray.isEmpty()) {
            return imgArray.size();
        }else if (favMovies != null) {
            return favMovies.size();
        }else{
            return stockPhoto.length;
        }
    }

    public void setFavMovies(List<MovieApp> movies) {
        favMovies = movies;
        notifyDataSetChanged();
    }

    public void setFavList(List<MovieApp> movies) {
         favTitles = movies;
    }
}
