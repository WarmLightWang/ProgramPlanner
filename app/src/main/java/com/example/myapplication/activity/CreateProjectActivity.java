package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.element.Session;
import com.example.myapplication.engine.ProjectCreate;

public class CreateProjectActivity extends ProgramActivity {
    TextView errView;
    Session session;
    ProjectCreate projectCreate;
    String projectName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);
        setupUI(findViewById(R.id.createProjectActivity));
        errView = findViewById(R.id.errorMessageTip);
        errView.setVisibility(View.INVISIBLE);
        session = Session.getInstance();
        projectCreate = new ProjectCreate(this);
        Button btConfirm = findViewById(R.id.buttonConfirm);
        btConfirm.setOnClickListener(v -> {
            if (validateWifi(false))
                createProject();
        });

        Button btCancel = findViewById(R.id.buttonCancel);
        btCancel.setOnClickListener(v -> backToMain());
    }

    private void createProject() {
        EditText nameEdit = findViewById(R.id.textBoxProjectName);
        projectName = nameEdit.getText().toString();

        if (projectName.equalsIgnoreCase("system") ||
                projectName.equalsIgnoreCase("systems") ||
                projectName.equalsIgnoreCase("admin")) {
            setErrView("This project name cannot be used");
            return;
        }

        projectCreate.createProject(projectName, session.getUserName());
    }

    public void finishAddProject() {
        Toast.makeText(getApplicationContext(), "New Project Created Successfully", Toast.LENGTH_SHORT).show();
    }


    public void backToMain() {
        Intent intent = new Intent(CreateProjectActivity.this, ProjectMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void setErrView(String message) {
        errView.setText(message);
        errView.setVisibility(View.VISIBLE);
    }
}
