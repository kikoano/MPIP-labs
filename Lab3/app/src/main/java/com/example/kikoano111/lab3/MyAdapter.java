package com.example.kikoano111.lab3;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.Collections;
import java.util.List;

/**
 * Created by kikoano111 on 6/12/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String INTENT_MOVIE = "MOVIE";


    public class MyHolder extends RecyclerView.ViewHolder {

        TextView movieName;
        ImageView imagePoster;
        TextView movieYear;
        String id;


        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            movieName = (TextView) itemView.findViewById(R.id.movieName);
            imagePoster = (ImageView) itemView.findViewById(R.id.moviePoster);
            movieYear = (TextView) itemView.findViewById(R.id.movieYear);

        }

    }


    private Context context;
    private LayoutInflater inflater;
    List<Movie> data = Collections.emptyList();

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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder = (MyHolder) holder;
        final Movie current = data.get(position);
        myHolder.movieName.setText(current.getTitle());
        myHolder.movieYear.setText(current.getYear());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.ic_launcher);
        requestOptions.error(R.drawable.ic_launcher_background);

        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(current.getPoster())
                .into(myHolder.imagePoster);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MovieDetailsActivity.class);
                intent.putExtra(INTENT_MOVIE, current);
                v.getContext().startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                data.remove(position);

                notifyDataSetChanged();
                MoviesActivity.database.movieDao().deleteById(current.getImdbID());
                return false;
            }
        });

    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


}
