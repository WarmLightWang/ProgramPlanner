package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.element.Session;
import com.example.myapplication.engine.ForgetPass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ForgetPassActivity extends ProgramActivity {

    private TextView errView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        setupUI(findViewById(R.id.forgetPassActivity));
        errView = findViewById(R.id.errorMessage);
        errView.setVisibility(View.INVISIBLE);
        FloatingActionButton btNext = findViewById(R.id.buttonNextStep);
        btNext.setOnClickListener(v -> verifyUsername());
    }

    private void verifyUsername() {
        EditText userEdit = findViewById(R.id.accountName);
        String username = userEdit.getText().toString();
        new ForgetPass(this, username);
    }

    public void toForgetQuestion(String username) {

        Intent intent = new Intent(ForgetPassActivity.this, ForgetQuesActivity.class);
        Session.getInstance().setUserName(username);
        startActivity(intent);
    }

    public void setErrText(String errMsg) {
        errView.setText(errMsg);
        errView.setVisibility(View.VISIBLE);
    }
}
