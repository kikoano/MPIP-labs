package com.example.kikoano111.lab3;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Url;


public class GetMovies extends IntentService {

    public GetMovies() {
        super("GetMovies");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // http://www.omdbapi.com/?s=spiderman&plot=short&apikey=94732e92

        String BASE_URL = "http://www.omdbapi.com";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        OmdbApi omdbApi = retrofit.create(OmdbApi.class);
        Map<String, String> map = new HashMap<>();
        if(!intent.getStringExtra("query").equals("94732e92")) {
            map.put("s", intent.getStringExtra("query"));
            map.put("plot", "short");
            map.put("apikey", "94732e92");
            Call<JsonData> call = omdbApi.getData(map);
            call.enqueue(new Callback<JsonData>() {
                @Override
                public void onResponse(Call<JsonData> call, Response<JsonData> response) {
                    List<Movie> movies = new ArrayList<>();
                    for (Search movie : response.body().getSearch()) {

                        movies.add(new Movie(movie.getImdbID(), movie.getTitle(), movie.getYear(), movie.getPoster()));

                    }
                /*MoviesActivity.database.movieDao().insertAll(movies);
                MoviesActivity.mAdapter = new MyAdapter(MoviesActivity.this,movies);
                MoviesActivity.recyclerView.setAdapter(MoviesActivity.mAdapter);
                MoviesActivity.recyclerView.setLayoutManager(new LinearLayoutManager(MoviesActivity.class));*/
                    //show searched

                }

                @Override
                public void onFailure(Call<JsonData> call, Throwable t) {
                    Log.e("MoviesActivity", "Failed to load");
                }
            });
        }else
        {
            map.put("t", intent.getStringExtra("title"));
            map.put("plot", "long");
            map.put("apikey", "94732e92");
            Call<JsonDataLong> call = omdbApi.getDataLong(map);
            call.enqueue(new Callback<JsonDataLong>() {
                @Override
                public void onResponse(Call<JsonDataLong> call, Response<JsonDataLong> response) {
                    //needs new view
                   /* MoviesActivity.mAdapter = new MyAdapter(MoviesActivity.this,movies);
                    MoviesActivity.recyclerView.setAdapter(MoviesActivity.mAdapter);
                    MoviesActivity.recyclerView.setLayoutManager(new LinearLayoutManager(MoviesActivity.this));*/
                }
                @Override
                public void onFailure(Call<JsonDataLong> call, Throwable t) {
                    Log.e("MoviesActivity", "Failed to load");
                }
            });
        }
    }

}