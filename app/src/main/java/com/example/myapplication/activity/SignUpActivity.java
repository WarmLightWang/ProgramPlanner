package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.element.Question;
import com.example.myapplication.element.User;
import com.example.myapplication.engine.SignUp;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SignUpActivity extends ProgramActivity {

    TextView errView;
    Spinner questionSpin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setupUI(findViewById(R.id.signUpActivity));
        errView = findViewById(R.id.errorMessage);
        errView.setVisibility(View.INVISIBLE);
        questionSpin = findViewById(R.id.spinnerSecurityQuestion);
        ArrayAdapter<String> questionAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, Question.QUESTION);
        questionAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        questionSpin.setAdapter(questionAdapter);
        FloatingActionButton btSignUp = findViewById(R.id.buttonNextStep);
        btSignUp.setOnClickListener(v -> {
            if (validateWifi(false))
                signup();
        });
    }

    private String getEditValue(int id) {
        EditText tempEdit = findViewById(id);
        return tempEdit.getText().toString();
    }

    private void signup() {
        String name = getEditValue(R.id.enterAccountName);
        String pass = getEditValue(R.id.enterPassword);
        String repass = getEditValue(R.id.reEnterPassword);
        String answer = getEditValue(R.id.enterAnswer);
        questionSpin = findViewById(R.id.spinnerSecurityQuestion);
        int quesIndex = questionSpin.getSelectedItemPosition();

        if (!pass.equals(repass)) {
            setErrView("Password incorrect");
            return;
        }

        if (name.isEmpty() || pass.isEmpty() || repass.isEmpty() || answer.isEmpty()) {
            setErrView("Fields are Empty");
            return;
        }

        if (name.equalsIgnoreCase("system") ||
                name.equalsIgnoreCase("systems") ||
                name.equalsIgnoreCase("admin")) {
            setErrView("This username cannot be used");
            return;
        }

        User user = new User(name, pass, quesIndex, answer);
        new SignUp(this, user);
    }

    public void setErrView(String errMsg) {
        errView.setText(errMsg);
        errView.setVisibility(View.VISIBLE);
    }

    public void finishSignUp() {
        Toast.makeText(getApplicationContext(), "SignUp successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //Getter added for testing purposes by Henry Koenig
    public TextView getErrView() {
        return errView;
    }
}
