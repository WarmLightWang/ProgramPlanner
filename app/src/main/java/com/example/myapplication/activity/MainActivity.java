package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import com.example.myapplication.R;

public class MainActivity extends ProgramActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
        ImageButton btLogin = findViewById(R.id.imageButtonLogIn);
        ImageButton btSignup = findViewById(R.id.imageButtonSignUp);
        btLogin.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, LoginActivity.class)));
        btSignup.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, SignUpActivity.class)));
    }
}
