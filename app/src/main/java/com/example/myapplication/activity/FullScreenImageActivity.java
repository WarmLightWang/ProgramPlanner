package com.example.myapplication.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

public class FullScreenImageActivity extends ProgramActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        ImageView fullScreen = findViewById(R.id.FullScreenImageView);
        Intent intent = getIntent();
        if (intent != null) {
            Uri imageUri = intent.getData();
            if (imageUri != null && fullScreen != null) {
                Picasso.with(this)
                        .load(imageUri)
                        .into(fullScreen);
            }
        }
        fullScreen.setOnClickListener(v -> finish());
    }
}
