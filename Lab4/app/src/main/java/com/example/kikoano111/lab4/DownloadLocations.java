package com.example.kikoano111.lab4;

import android.app.IntentService;
import android.arch.persistence.room.Room;
import android.content.Intent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kikoano111 on 20/12/2017.
 */

public class DownloadLocations extends IntentService {
    public static final String DATABASE_UPDATED = "updated database";

    private LocationsApi service;
    private Map<String, String> attributes;

    public DownloadLocations() {
        super("DownloadLocations");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initVariables();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String location = intent.getStringExtra(MapActivity.EXTRA_LOCATION);
        setUpQuery(location);

        Call<SearchResult> call = service.getLocations(attributes);
        try {
            SearchResult result = call.execute().body();
            List<LocationData> locationData = result.getResults();

            saveResultsInDb(locationData);
            sendBroadcast(new Intent(DATABASE_UPDATED));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initVariables() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(LocationsApi.class);
    }

    private void setUpQuery(String location) {
        this.attributes = new HashMap<>();
        attributes.put("location", location);
        attributes.put("key", getResources().getString(R.string.google_maps_api_key));
        attributes.put("radius", "800");
        attributes.put("types", "restaurant");
    }

    private void saveResultsInDb(List<LocationData> locationData) {
        LocationsDatabase db = Room
                .databaseBuilder(this, LocationsDatabase.class, "locations")
                .fallbackToDestructiveMigration()
                .build();

        List<LocationEntity> locationEntities = new ArrayList<>();

        for (LocationData ld : locationData){
            LocationEntity le = new LocationEntity();
            le.lat = ld.geometry.location.lat;
            le.lng = ld.geometry.location.lng;
            le.name = ld.name;

            locationEntities.add(le);
        }

        db.locationDao().deleteAll();
        db.locationDao().insertAll(locationEntities.toArray(new LocationEntity[]{}));
    }
}
