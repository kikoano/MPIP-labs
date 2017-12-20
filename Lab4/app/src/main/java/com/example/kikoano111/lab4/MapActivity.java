package com.example.kikoano111.lab4;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<LocationEntity>>,
        OnMapReadyCallback, LocationListener{

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static final String EXTRA_LOCATION = "extra location";

    private UpdateLocationsReceiver receiver;

    private List<LocationEntity> locationEntities;
    private LocationManager locationManager;
    private Location location;

    private SupportMapFragment mapFragment;

    private class UpdateLocationsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            MapActivity.this
                    .getSupportLoaderManager()
                    .restartLoader(0,null,MapActivity.this)
                    .forceLoad();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        initVariables();
        checkLocationPermission();
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(DownloadLocations.DATABASE_UPDATED);
        registerReceiver(receiver, filter);

        getLocation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);

        locationManager.removeUpdates(this);
    }


    @Override
    public Loader<List<LocationEntity>> onCreateLoader(int id, Bundle args) {
        return new LocationLoader(this.getApplicationContext());
    }

    @Override
    public void onLoadFinished(Loader<List<LocationEntity>> loader, List<LocationEntity> data) {
        this.locationEntities = data;
        setUpMap();
    }

    @Override
    public void onLoaderReset(Loader<List<LocationEntity>> loader) {

    }


    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        downloadLocations();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.clear();
        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(current)
                .title("You are here!"));

        for (LocationEntity le : locationEntities){
            LatLng locale = new LatLng(le.lat, le.lng);
            googleMap.addMarker(new MarkerOptions().position(locale)
                    .title(le.name));
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 16));
    }


    private void initVariables() {
        receiver = new UpdateLocationsReceiver();
        locationEntities = new ArrayList<>();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    private void downloadLocations() {
        startService(new Intent
                (this, DownloadLocations.class)
                .putExtra(EXTRA_LOCATION, location.getLatitude() + "," + location.getLongitude()));
    }

    private void setUpMap() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void getLocation(){
        getCurrentLocation();
        if (location == null){
            getLastKnownLocation();
        }
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 0, this);
        }
    }

    private void getLastKnownLocation(){
        try{
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        catch(SecurityException e){
            e.printStackTrace();
        }
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Grant LocationData Permission")
                        .setMessage("You need to grant location permission in order for the app to work")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
        }
    }
}