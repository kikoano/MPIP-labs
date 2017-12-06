package com.example.kikoano111.lab3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieDetailsActivity extends Activity {

    //private Toolbar toolbar;
    private TextView textViewTitle;
    private TextView textViewPlot;
    private ImageView img;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        textViewTitle = (TextView) findViewById(R.id.titleMov);
        textViewPlot = (TextView) findViewById(R.id.plotMov);
        img = (ImageView) findViewById(R.id.imgMov);

        initActivity();
        initToolbar();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.share_bar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    private void initToolbar() {
       /* toolbar = (Toolbar) findViewById(R.id.action_share);
        setSupportActionBar(toolbar);*/
    }

    private void initActivity() {
        Intent intent = getIntent();
        String movieName = intent.getStringExtra(MyAdapter.MyHolder.EXTRA_MOVIE_TITLE);
        textViewTitle.setText(movieName);
        View view = findViewById(R.id.movieDetailsLayout);
        intent.putExtra("query","94732e92");
        intent.putExtra("title",movieName);
        startService(intent);
        //
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_share:
                shareMovie();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void shareMovie() {
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, String.format("Check out this movie!\n\n\n%s\n\n%s",
                textViewTitle.getText().toString(), textViewPlot.getText().toString()));
        startActivity(Intent.createChooser(intent, "Share via..."));
    }

}
