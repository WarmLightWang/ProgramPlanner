package com.example.myapplication.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.element.Log;
import com.example.myapplication.element.Session;
import com.example.myapplication.engine.ManageLog;
import com.example.myapplication.engine.Validation;

import java.util.ArrayList;
import java.util.List;

public class LogViewActivity extends ProgramActivity {

    Session session;
    List<Log> logList;
    ManageLog manageLog;
    LinearLayout logLayout;
    Log newLog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_view);
        setupUI(findViewById(R.id.logActivity));
        session = Session.getInstance();
        String username = session.getUserName();
        String projectId = session.getProjectId();
        validation = new Validation(username, projectId);
        manageLog = new ManageLog(this, projectId);
        logList = new ArrayList<>();
        manageLog.getLogList();
        setupButton();
    }

    public void setupLogList(List<Log> logList) {
        this.logList = logList;
        logLayout = findViewById(R.id.logList);
        if (logList.isEmpty()) {
            String info = "There is no log for the project";
            TextView infoView = new TextView(this);
            infoView.setText(info);
            infoView.setTextSize(20);
            infoView.setPadding(5, 5, 5, 5);
            infoView.setClickable(false);
            logLayout.addView(infoView);
        }

        for (int i = 0; i < logList.size(); i++) {
            TextView logView = new TextView(this);
            Log temp = logList.get(i);
            String content = temp.date + " : \n" + temp.content + " \nBy: " + temp.username + "\n";
            logView.setText(content);
            logView.setTextSize(20);
            logView.setPadding(5, 5, 5, 5);
            logLayout.addView(logView);
        }
    }

    private void setupButton() {
        Button btSubmit = findViewById(R.id.buttonSubmit);
        btSubmit.setOnClickListener(v -> {
            if (validateRole()) {
                logList.clear();
                logLayout.removeAllViews();
                submit();
            }
        });

        Button btBack = findViewById(R.id.buttonBack);
        btBack.setOnClickListener(v -> {
            validateRole();
            finish();
        });
    }

    private void submit() {
        EditText logEdit = findViewById(R.id.editTextTextMultiLine3);
        String logContent = logEdit.getText().toString();
        if (!logContent.isEmpty()) {
            String username = session.getUserName();
            newLog = new Log(logContent, username);
            manageLog.addLog(newLog);
        } else {
            Toast.makeText(getApplicationContext(), "Nothing to add. Please enter a log", Toast.LENGTH_SHORT).show();
        }
        logEdit.getText().clear();
    }

    public void finishAddLog() {
        Toast.makeText(getApplicationContext(), "New Log is Added", Toast.LENGTH_SHORT).show();
    }
}
