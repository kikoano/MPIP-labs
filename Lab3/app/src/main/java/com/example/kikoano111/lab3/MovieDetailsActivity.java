package com.example.kikoano111.lab3;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class MovieDetailsActivity extends Activity {

    //private Toolbar toolbar;
    private TextView textViewTitle;
    private TextView textViewPlot;
    private TextView textViewYear;
    private TextView textViewType;
    private ImageView img;
    private ResponseReceiverPlot receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        textViewTitle = findViewById(R.id.titleMov);
        textViewPlot =  findViewById(R.id.plotMov);
        textViewYear = findViewById(R.id.yearMov);
        textViewType =  findViewById(R.id.typeMov);
        img =  findViewById(R.id.imgMov);

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
        Movie current = this.getIntent().getParcelableExtra(MyAdapter.INTENT_MOVIE);
        textViewTitle.setText(current.getTitle());
        textViewYear.setText("Release year: "+current.getYear());
        textViewType.setText("Type: "+current.getType());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.ic_launcher);
        requestOptions.error(R.drawable.ic_launcher_background);

        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(current.getPoster())
                .into(img);

        if(isNetworkAvailable()) {
            Log.e("MovieDetailsActivity", "Yes");
            Intent intent = new Intent(getApplicationContext(), GetMoviePlot.class);
            intent.putExtra("id", current.getImdbID());
            startService(intent);
        }
        else
            Log.e("MovieDetailsActivity","No");
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
        intent.putExtra(Intent.EXTRA_TEXT, String.format("Check out this movie!\n%s\n%s",
                textViewTitle.getText().toString(), textViewPlot.getText().toString()));
        startActivity(Intent.createChooser(intent, "Share via..."));
    }
    public class ResponseReceiverPlot extends BroadcastReceiver {
        public static final String ACTION_RESP = "RECEIVE_PLOT";

        @Override
        public void onReceive(Context context, Intent intent) {
            textViewPlot =  findViewById(R.id.plotMov);
            textViewPlot.setText(intent.getStringExtra("plot"));
        }
    }
    @Override
    protected void onResume() {
        IntentFilter filter = new IntentFilter(ResponseReceiverPlot.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ResponseReceiverPlot();
        registerReceiver(receiver, filter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }

}
