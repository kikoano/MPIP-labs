package com.example.kikoano111.lab2;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

/**
 * Created by kikoano111 on 22/11/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public class MyHolder extends RecyclerView.ViewHolder {

        TextView movieName;
        ImageView imagePoster;
        TextView movieYear;

        public static final String EXTRA_MOVIE_TITLE = "movie title";

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            movieName = (TextView) itemView.findViewById(R.id.movieName);
            imagePoster = (ImageView) itemView.findViewById(R.id.moviePoster);
            movieYear = (TextView) itemView.findViewById(R.id.movieYear);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), MovieDetailsActivity.class);
                    intent.putExtra(EXTRA_MOVIE_TITLE, movieName.getText().toString());
                    v.getContext().startActivity(intent);
                }
            });
        }

    }


    private Context context;
    private LayoutInflater inflater;
    List<Movie> data = Collections.emptyList();
    Movie current;
    int currentPos = 0;

    // create constructor to innitilize context and data sent from MainActivity
    public MyAdapter(Context context, List<Movie> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder = (MyHolder) holder;
        Movie current = data.get(position);
        myHolder.movieName.setText(current.movieName);
        myHolder.movieYear.setText(current.movieYear);
        Log.e("Erroe","Error");
        // load image into imageview using glide
        Glide.with(context).load(current.posterImage)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(myHolder.imagePoster);

    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


}