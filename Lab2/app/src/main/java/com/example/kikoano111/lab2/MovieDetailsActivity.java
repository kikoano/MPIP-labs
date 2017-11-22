package com.example.kikoano111.lab2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity {

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
        new AsyncFetch(movieName).execute();
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

    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(MovieDetailsActivity.this);
        HttpURLConnection conn;
        String search = "";
        URL url = null;

        public AsyncFetch(String search) {
            this.search = search;
            //Toast.makeText(MoviesActivity.this, Uri.parse(search).toString(), Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL("http://www.omdbapi.com/?t=" + Uri.encode(search) + "&plot=short&apikey=94732e92");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();

            pdLoading.dismiss();
            try {

                JSONObject jb = new JSONObject(result);

                //movieData.posterImage = json_data.getString("Poster");
                textViewPlot.setText(jb.getString("Plot"));
                Glide.with(MovieDetailsActivity.this).load(jb.getString("Poster"))
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .into(img);


            } catch (JSONException e) {
                Toast.makeText(MovieDetailsActivity.this, "No Found!", Toast.LENGTH_LONG).show();
            }

        }
    }
}