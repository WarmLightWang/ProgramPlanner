package com.example.myapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.myapplication.R;
import com.example.myapplication.element.Session;
import com.example.myapplication.engine.ManageDeveloper;

public class DeveloperActivity extends ProgramActivity {

    boolean isConfirm;
    boolean isManager;
    String username, projectId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_main);
        setupUI(findViewById(R.id.developerMainActivity));
        Intent intent = getIntent();
        isManager = intent.getExtras().getBoolean("isManager");
        TextView titleView = findViewById(R.id.mainTitle);
        if (isManager) {
            String title = "Manager Main Page";
            titleView.setText(title);
        }
        setup(isManager);

        TextView projectNameView = findViewById(R.id.ProjectNameTitle);
        projectNameView.setText(Session.getInstance().getProjectName());
        username = Session.getInstance().getUserName();
        projectId = Session.getInstance().getProjectId();
        setValidation(username, projectId);
    }

    private void setup(boolean isManager) {
        Button btTask = findViewById(R.id.buttonTaskAssignment);
        btTask.setOnClickListener(v -> {
            if (validateRole())
                toTaskAssignment();
        });

        Button btInvite = findViewById(R.id.buttonInvite);
        btInvite.setOnClickListener(v -> {
            if (validateRole())
                toInvite();
        });

        Button btNote = findViewById(R.id.buttonNoteBook);
        btNote.setOnClickListener(v -> {
            if (validateRole())
                toNotebook();
        });

        Button btGraph = findViewById(R.id.buttonGraph);
        btGraph.setOnClickListener(v -> {
            if (validateRole())
                toGraph();
        });

        Button btLog = findViewById(R.id.buttonLog);
        btLog.setOnClickListener(v -> {
            if (validateRole())
                toLog();
        });

        Button btTimeline = findViewById(R.id.buttonTimeLine);
        btTimeline.setOnClickListener(v -> {
            if (validateRole())
                toTimeline();
        });

        Button btViewFeedback = findViewById(R.id.buttonViewFeedBack);
        btViewFeedback.setOnClickListener(v -> {
            if (validateRole())
                toViewFeedback();
        });

        Button btManageRole = findViewById(R.id.buttonManageRole);
        btManageRole.setOnClickListener(v -> toManageRole());

        Button btDelete = findViewById(R.id.buttonDelete);
        btDelete.setOnClickListener(v -> deleteProject());

        Button btBack = findViewById(R.id.buttonBack);
        btBack.setOnClickListener(v -> onBackPressed());

        if (!isManager) {
            btManageRole.setVisibility(View.GONE);
            btDelete.setVisibility(View.GONE);
        }
    }

    private void toTaskAssignment() {
        startActivity(new Intent(DeveloperActivity.this, TaskAssignActivity.class));
    }

    private void toInvite() {
        Intent intent = new Intent(DeveloperActivity.this, InviteActivity.class);
        intent.putExtra("isManager", isManager);
        startActivity(intent);
    }

    private void toNotebook() {
        startActivity(new Intent(DeveloperActivity.this, NotebookActivity.class));
    }

    private void toGraph() {

        startActivity(new Intent(DeveloperActivity.this, GraphActivity.class));
    }

    private void toLog() {
        startActivity(new Intent(DeveloperActivity.this, LogViewActivity.class));
    }

    private void toTimeline() {
        Intent intent = new Intent(DeveloperActivity.this, TimelineActivity.class);
        intent.putExtra("isDeveloper", true);
        startActivity(intent);
    }

    private void toViewFeedback() {
        startActivity(new Intent(DeveloperActivity.this, ViewFeedbackActivity.class));
    }

    private void toManageRole() {
        startActivity(new Intent(DeveloperActivity.this, RoleViewActivity.class));
    }

    private void deleteProject() {
        String message = "Are you sure you want to delete the project?";
        String title = "Delete Project";

        boolean isConfirm = deleteConfirmation(DeveloperActivity.this, message, title);
        if (isConfirm) {
            System.out.println("Execute delete");
            ManageDeveloper manage = new ManageDeveloper(this);
            manage.removeData(Session.getInstance().getProjectId());
        }
    }

    private boolean deleteConfirmation(Context context, String message, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        System.out.println("Build alert dialog");
        builder.setMessage(message)
                .setTitle(title);
        builder.setPositiveButton("Confirm", (dialog, which) -> isConfirm = true);
        builder.setNegativeButton("Cancel", (dialog, which) -> isConfirm = false);

        builder.show();
        return isConfirm;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DeveloperActivity.this, ProjectMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void finishDelete() {
        Toast.makeText(getApplicationContext(), "Project delete Sucessfully", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }
}
