package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.element.Session;
import com.example.myapplication.engine.ForgetPass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ForgetQuesActivity extends ProgramActivity {

    private TextView errView;
    private TextView quesView;
    private ForgetPass forget;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass_ques);
        setupUI(findViewById(R.id.forgetPassQuestionActivity));
        errView = findViewById(R.id.errorMessage);
        errView.setVisibility(View.INVISIBLE);
        quesView = findViewById(R.id.securityQuestionTip);
        String username = Session.getInstance().getUserName();
        forget = new ForgetPass(ForgetQuesActivity.this, username);
        forget.getQuestion();
        FloatingActionButton btConfirm = findViewById(R.id.buttonNextStep);
        btConfirm.setOnClickListener(v -> {
            EditText ansEdit = findViewById(R.id.questionAnswer);
            String quesAns = ansEdit.getText().toString();
            forget.verifyQuestion(quesAns);
        });
    }

    public void setErrText(String errMsg) {
        errView.setText(errMsg);
        errView.setVisibility(View.VISIBLE);
    }

    public void setQuesView(String question) {
        quesView.setText(question);
    }

    public void finishVerify() {
        startActivity(new Intent(ForgetQuesActivity.this, ResetPassActivity.class));
    }
}
