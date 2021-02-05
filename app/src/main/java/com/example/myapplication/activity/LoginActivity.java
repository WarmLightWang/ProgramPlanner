package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.element.Session;
import com.example.myapplication.element.User;
import com.example.myapplication.engine.Login;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class LoginActivity extends ProgramActivity {

    private TextView errView;
    private EditText userEdit;
    private EditText passEdit;
    Session session = Session.getInstance();
    Login login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        setupUI(findViewById(R.id.loginActivity));
        errView = findViewById(R.id.errormessage);
        errView.setVisibility(View.INVISIBLE);
        userEdit = findViewById(R.id.editTextAccountName);
        passEdit = findViewById(R.id.editTextPassword);
        login = new Login(this);
        setup();
    }

    private void setup() {
        FloatingActionButton btLogin = findViewById(R.id.buttonLogIn);
        btLogin.setOnClickListener(v -> {
            if (validateWifi(false))
                login();
        });

        TextView forgotText = findViewById(R.id.forgotPassword);
        forgotText.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, ForgetPassActivity.class)));
    }

    private void login() {
        String username = userEdit.getText().toString();
        String pass = passEdit.getText().toString();
        User user = new User(username, pass);
        login.login(user);
    }

    public void setErrText(String errMsg) {
        errView.setText(errMsg);
        errView.setVisibility(View.VISIBLE);
        passEdit.getText().clear();
    }

    public void finishLogin(String username) {
        session.setUserName(username);
        userEdit.getText().clear();
        passEdit.getText().clear();
        startActivity(new Intent(LoginActivity.this, ProjectMainActivity.class));
    }
}
