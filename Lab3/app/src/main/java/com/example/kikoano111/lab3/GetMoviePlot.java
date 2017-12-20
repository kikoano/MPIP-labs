package com.example.kikoano111.lab3;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kikoano111 on 20/12/2017.
 */

public class GetMoviePlot extends IntentService {

    public GetMoviePlot(){
        super("GetMoviePlot");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        String BASE_URL = "http://www.omdbapi.com";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        OmdbApi omdbApi = retrofit.create(OmdbApi.class);
        Map<String, String> map = new HashMap<>();
        map.put("i", intent.getStringExtra("id"));
        map.put("plot", "long");
        map.put("apikey", "94732e92");
        Call<JsonDataLong> call = omdbApi.getDataLong(map);
        call.enqueue(new Callback<JsonDataLong>() {
            @Override
            public void onResponse(Call<JsonDataLong> call, Response<JsonDataLong> response) {

                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(MovieDetailsActivity.ResponseReceiverPlot.ACTION_RESP);
                broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                broadcastIntent.putExtra("plot", response.body().getPlot());
                sendBroadcast(broadcastIntent);

            }

            @Override
            public void onFailure(Call<JsonDataLong> call, Throwable t) {
                Log.e("MoviesActivity", "Failed to load");
            }

        });
    }
}
