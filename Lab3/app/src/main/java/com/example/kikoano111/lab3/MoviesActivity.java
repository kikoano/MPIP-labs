package com.example.kikoano111.lab3;

import android.app.Activity;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.content.Intent;
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
    public static RecyclerView recyclerView;
    public static RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        database = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, "MyDatabase").allowMainThreadQueries().build();
        List<Movie> movies = database.movieDao().fetchAllData();
        if(movies!=null) {
            for (Movie m : movies)
                Log.e("MoviesActivity", m.title);
        }
        recyclerView = (RecyclerView)findViewById(R.id.my_recycle);
        mAdapter = new MyAdapter(MoviesActivity.this,movies);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MoviesActivity.this));
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
                intent.putExtra("query",query);
                startService(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    private void loadAllMovies(){

    }
}
