package com.example.kikoano111.lab4;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by kikoano111 on 20/12/2017.
 */


public interface LocationsApi {
    @GET("/maps/api/place/nearbysearch/json")
    Call<SearchResult> getLocations(@QueryMap Map<String, String> attributes);
}

