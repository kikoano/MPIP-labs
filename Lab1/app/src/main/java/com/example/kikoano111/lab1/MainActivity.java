package com.example.kikoano111.lab1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            String text = bundle.getString("text", "NO");

            TextView EAText = findViewById(R.id.ExplicitATextView);
            EAText.setText(text);
        }
    }

    public void startEA(View view){
        Intent intent = new Intent(this, ExplicitActivity.class);
        startActivity(intent);
    }

    public void startIA(View view){
        Intent intent = new Intent("mk.ukim.finki.mpip.IMPLICIT_ACTION");
        startActivity(intent);
    }

    public void sendAction(View view){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_SUBJECT, "MPiP Send Title");
        intent.putExtra(Intent.EXTRA_TEXT, "Content send from MainActivity");
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, "Send Action"));
    }

    public void selectImage(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                ImageView imageView = findViewById(R.id.imageView);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
