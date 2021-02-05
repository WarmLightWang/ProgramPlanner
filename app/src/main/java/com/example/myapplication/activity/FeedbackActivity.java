package com.example.myapplication.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.element.Feedback;
import com.example.myapplication.element.Session;
import com.example.myapplication.engine.ManageFeedback;

public class FeedbackActivity extends ProgramActivity {

    Session session;
    Feedback newFeedback;
    String projectIdStr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_view);
        setupUI(findViewById(R.id.feedbackViewActivity));
        session = Session.getInstance();
        projectIdStr = session.getProjectId();
        String username = session.getUserName();
        setValidation(username, projectIdStr);
        Button btConfirm = findViewById(R.id.button);
        btConfirm.setOnClickListener(v -> {
            if (validateRole())
                submitFeedback();
        });

        Button btBack = findViewById(R.id.button2);
        btBack.setOnClickListener(v -> {
            validateRole();
            finish();
        });
    }

    private void submitFeedback() {
        EditText feedEdit = findViewById(R.id.textBoxFeedBack);
        String feedback = feedEdit.getText().toString();
        newFeedback = new Feedback(session.getUserName(), feedback);
        ManageFeedback manage = new ManageFeedback(this, projectIdStr);
        manage.addFeedback(newFeedback);
    }

    public void finishSubmit() {
        Toast.makeText(getApplicationContext(), "New Feedback is Added", Toast.LENGTH_SHORT).show();
        finish();
    }
}
