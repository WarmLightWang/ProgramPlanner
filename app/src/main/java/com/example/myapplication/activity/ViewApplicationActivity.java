package com.example.myapplication.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.myapplication.R;
import com.example.myapplication.element.Application;
import com.example.myapplication.element.Session;
import com.example.myapplication.engine.ManageApplication;

import java.util.ArrayList;
import java.util.List;

public class ViewApplicationActivity extends ProgramActivity {

    LinearLayout applicationLayout;
    List<Application> applicationList;
    List<Integer> appViewIdList;
    Session session;
    ManageApplication manageApp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_application);
        setupUI(findViewById(R.id.applicationViewActivity));
        applicationLayout = findViewById(R.id.applicationLayout);
        appViewIdList = new ArrayList<>();
        session = Session.getInstance();
        manageApp = new ManageApplication(this, session.getProjectId());
        manageApp.getApplicationList();
    }

    public void setupLayout(List<Application> applicationList) {
        this.applicationList = applicationList;
        applicationLayout = findViewById(R.id.applicationLayout);
        applicationLayout.removeAllViews();

        if (applicationList.isEmpty()) {
            String info = "There is no application for the project";
            TextView infoView = new TextView(this);
            infoView.setText(info);
            infoView.setTextSize(20);
            infoView.setPadding(5, 5, 5, 5);
            infoView.setClickable(false);
            applicationLayout.addView(infoView);
        }

        for (int i = 0; i < applicationList.size(); i++) {
            final TextView appView = new TextView(this);
            Application temp = applicationList.get(i);
            String appInfo = String.format("%s : %s", temp.username, temp.roles);
            int viewId = View.generateViewId();
            appViewIdList.add(viewId);
            appView.setId(viewId);
            appView.setText(appInfo);
            appView.setTextSize(20);
            appView.setPadding(5, 5, 5, 5);
            appView.setClickable(true);
            appView.setOnClickListener(v -> {
                int index = applicationLayout.indexOfChild(appView);
                viewApplication(index);
            });
            applicationLayout.addView(appView);
        }
    }

    private void viewApplication(int index) {
        Application application = applicationList.get(index);
        String title = "Project Application";
        showApplicationDialog(ViewApplicationActivity.this, application, title);
    }

    public void finishViewApplication(String message) {
        Toast.makeText(ViewApplicationActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void showApplicationDialog(Context context, final Application application, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String appDetails = application.toString();
        builder.setMessage(appDetails)
                .setTitle(title);
        builder.setPositiveButton("Accept", (dialog, which) -> manageApp.acceptApplication(application));
        builder.setNegativeButton("Reject", (dialog, which) -> manageApp.rejectApplication(application));
        builder.setNeutralButton("Cancel", (dialog, which) -> {
        });
        builder.show();
    }
}
