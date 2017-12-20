package com.example.kikoano111.lab3;

import android.app.Activity;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;

import java.util.List;

public class MoviesActivity extends Activity {

    public static MyDatabase database;
    private static RecyclerView recyclerView;
    private static RecyclerView.Adapter mAdapter;
    private ResponseReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        database = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, "MyDatabase").allowMainThreadQueries().build();
        recyclerView = findViewById(R.id.my_recycle);
        loadSQLData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchbar, menu);
        SearchView search = (SearchView) menu.findItem(R.id.searchMovie).getActionView();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //http://www.omdbapi.com/?t=title&plot=short&apikey=94732e92
                //service
                Intent intent = new Intent(getApplicationContext(), GetMovies.class);
                intent.putExtra("query", query);
                startService(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                if(newText.isEmpty())
                    loadSQLData();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ResponseReceiver();
        registerReceiver(receiver, filter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }

    public class ResponseReceiver extends BroadcastReceiver {
        public static final String ACTION_RESP = "RECEVE_MOVIES";

        @Override
        public void onReceive(Context context, Intent intent) {
            List<Movie> movies = intent.getParcelableArrayListExtra("movies");
            mAdapter = new MyAdapter(MoviesActivity.this, movies);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(MoviesActivity.this));
        }
    }
    public void loadSQLData(){
        List<Movie> movies = database.movieDao().fetchAllData();
        mAdapter = new MyAdapter(MoviesActivity.this, movies);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MoviesActivity.this));
    }
}
