package com.example.kikoano111.lab3;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by kikoano111 on 6/12/2017.
 */
public interface OmdbApi {
    @GET("/")
    Call<JsonData> getData(@QueryMap Map<String, String> map);
    @GET("/")
    Call<JsonDataLong> getDataLong(@QueryMap Map<String, String> map);
}
