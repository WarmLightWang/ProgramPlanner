package com.example.myapplication.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.element.Feedback;
import com.example.myapplication.element.Session;
import com.example.myapplication.engine.ManageFeedback;

import java.util.List;

public class ViewFeedbackActivity extends ProgramActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_feedback_view);
        setupUI(findViewById(R.id.developerFeedbackViewActivity));
        String projectIdStr = Session.getInstance().getProjectId();
        ManageFeedback manage = new ManageFeedback(this, projectIdStr);
        manage.getFeedbackList();
        setValidation(Session.getInstance().getUserName(), Session.getInstance().getProjectId());
        Button btBack = findViewById(R.id.buttonBack);
        btBack.setOnClickListener(v -> {
            validateRole();
            finish();
        });
    }

    public void setupFeedbackList(List<Feedback> feedbackList) {
        LinearLayout feedLayout = findViewById(R.id.feedbackList);
        if (feedbackList.isEmpty()) {
            String info = "There is no feedback for the project";
            TextView infoView = new TextView(this);
            infoView.setText(info);
            infoView.setTextSize(20);
            infoView.setPadding(5, 5, 5, 5);
            infoView.setClickable(false);
            feedLayout.addView(infoView);
        }

        for (int i = 0; i < feedbackList.size(); i++) {
            TextView feedView = new TextView(this);
            Feedback temp = feedbackList.get(i);
            String content = temp.comment + "\nBy: " + temp.username;
            feedView.setText(content);
            feedView.setTextSize(25);
            feedView.setPadding(5, 5, 5, 5);
            feedLayout.addView(feedView);
        }
    }
}
