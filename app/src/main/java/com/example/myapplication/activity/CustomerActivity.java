package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.element.Session;

public class CustomerActivity extends ProgramActivity {

    Session session = Session.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);
        setupUI(findViewById(R.id.customerMainActivity));
        TextView titleView = findViewById(R.id.ProjectNameTitle);
        titleView.setText(session.getCurrProject().projectName);
        String username = session.getUserName();
        setValidation(username, session.getProjectId());
        Button btFeedback = findViewById(R.id.buttonLeaveFeedBack);
        btFeedback.setOnClickListener(v -> {
            if (validateRole())
                toFeedback();
        });

        Button btTimeline = findViewById(R.id.buttonTimeline);
        btTimeline.setOnClickListener(v -> {
            if (validateRole())
                toTimeline();
        });

        Button btBack = findViewById(R.id.buttonBack);
        btBack.setOnClickListener(v -> onBackPressed());
    }

    private void toFeedback() {
        startActivity(new Intent(CustomerActivity.this, FeedbackActivity.class));
    }

    private void toTimeline() {
        Intent intent = new Intent(CustomerActivity.this, TimelineActivity.class);
        intent.putExtra("isDeveloper", false);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CustomerActivity.this, ProjectMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
