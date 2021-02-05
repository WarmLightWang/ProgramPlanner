package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.myapplication.R;
import com.example.myapplication.element.Project;
import com.example.myapplication.element.Session;
import com.example.myapplication.engine.JoinProject;

public class JoinProjectActivity extends ProgramActivity {

    TextView errView;
    CardView notification;
    Session session;
    JoinProject joinProject;
    Button btJoin, btApply, btView;
    Spinner roleSpin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_project);
        setupUI(findViewById(R.id.projectJoinPage));
        errView = findViewById(R.id.errorMessageTip);
        errView.setVisibility(View.INVISIBLE);
        notification = findViewById(R.id.notification);
        session = Session.getInstance();
        joinProject = new JoinProject(this, session.getUserName());
        setupSpinner();
        setupButton();
    }

    private void setupSpinner() {
        roleSpin = findViewById(R.id.spinnerRole);
        String[] roles = {"client", "developer"};
        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, roles);
        roleAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        roleSpin.setAdapter(roleAdapter);
    }

    public void setNotification(boolean gotInvitation) {
        if (gotInvitation)
            notification.setVisibility(View.VISIBLE);
        else
            notification.setVisibility(View.INVISIBLE);
    }

    private void setupButton() {
        btJoin = findViewById(R.id.buttonJoin);
        btJoin.setOnClickListener(v -> {
            if (validateWifi(false))
                joinProject();
        });

        btApply = findViewById(R.id.buttonApply);
        btApply.setOnClickListener(v -> {
            if (validateWifi(false))
                applyProject();
        });

        btView = findViewById(R.id.buttonInvite);
        btView.setOnClickListener(v -> {
            if (validateWifi(false))
                toViewInvite();
        });

        Button btCancel = findViewById(R.id.buttonCancel);
        btCancel.setOnClickListener(v -> {
            Intent intent = new Intent(JoinProjectActivity.this, ProjectMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private void joinProject() {
        EditText inviteEdit = findViewById(R.id.textBoxInvitationCode);
        String inviteCode = inviteEdit.getText().toString();
        if (inviteCode.isEmpty()) {
            String errMsg = "Please enter an invite Code";
            setErrText(errMsg);
        } else {
            joinProject.joinProject(inviteCode, session.getUserName());
        }
    }

    private void applyProject() {
        EditText projectEdit = findViewById(R.id.textBoxProjectName);
        String projectName = projectEdit.getText().toString();
        String projectRoles = roleSpin.getSelectedItem().toString();
        if (projectName.isEmpty()) {
            String errMsg = "Please enter an project name";
            setErrText(errMsg);
        } else {
            joinProject.applyProject(projectName, projectRoles, session.getUserName());
        }
    }

    private void toViewInvite() {
        startActivity(new Intent(JoinProjectActivity.this, ViewInvitationActivity.class));
    }

    public void setProjectValue(Project project) {
        session.setCurrProject(project);
    }

    public void finishJoin(boolean isDeveloper) {
        if (isDeveloper)
            toDeveloper();
        else
            toClient();
    }

    public void finishApply() {
        Toast.makeText(getApplicationContext(), "Apply Successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void setErrText(String errMsg) {
        errView.setText(errMsg);
        errView.setVisibility(View.VISIBLE);
    }

    private void toDeveloper() {
        Intent intent = new Intent(JoinProjectActivity.this, DeveloperActivity.class);
        intent.putExtra("isManager", false);
        startActivity(intent);
    }

    private void toClient() {
        startActivity(new Intent(JoinProjectActivity.this, CustomerActivity.class));
    }
}
