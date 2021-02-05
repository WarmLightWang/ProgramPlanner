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
import com.example.myapplication.element.Session;
import com.example.myapplication.engine.ManageProjectInvite;
import com.example.myapplication.engine.Validation;

public class InviteActivity extends ProgramActivity {

    TextView customerView, developerView, errView;
    CardView notification;
    EditText userEdit;
    ManageProjectInvite manage;
    Spinner roleSpin;
    boolean isManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_code_page);
        setupUI(findViewById(R.id.projectInvitePage));
        errView = findViewById(R.id.errorMessageTip);
        errView.setVisibility(View.INVISIBLE);
        String username = Session.getInstance().getUserName();
        String projectId = Session.getInstance().getProjectId();
        validation = new Validation(username, projectId);
        notification = findViewById(R.id.notification);
        manage = new ManageProjectInvite(this, Session.getInstance().getProjectId());
        manage.getInviteCode();
        Intent intent = getIntent();
        isManager = intent.getExtras().getBoolean("isManager");
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

    public void setupCode(String clientCode, String devCode, boolean gotApplication) {
        customerView = findViewById(R.id.inviteCodeCustomer);
        customerView.setText(clientCode);
        developerView = findViewById(R.id.inviteCodeDeveloper);
        developerView.setText(devCode);

        if (gotApplication && isManager)
            notification.setVisibility(View.VISIBLE);
        else
            notification.setVisibility(View.INVISIBLE);
    }

    public void setErrText(String message) {
        errView.setText(message);
        errView.setVisibility(View.VISIBLE);
    }

    private void setupButton() {
        Button btInvite = findViewById(R.id.buttonInvite);
        btInvite.setOnClickListener(v -> {
            if (validateRole())
                inviteUser();
        });

        Button btView = findViewById(R.id.buttonApplication);
        btView.setOnClickListener(v -> {
            if (validateRole())
                viewApplication();
        });

        Button btDone = findViewById(R.id.buttonDone);
        btDone.setOnClickListener(v -> {
            validateRole();
            finish();
        });

        if (!isManager) {
            btView.setVisibility(View.GONE);
            notification.setVisibility(View.GONE);
        }
    }

    private void inviteUser() {
        userEdit = findViewById(R.id.textUsername);
        String username = userEdit.getText().toString();
        System.out.println("Invite Username: " + username);
        if (!username.isEmpty()) {
            String projectName = Session.getInstance().getProjectName();
            String projectRole = roleSpin.getSelectedItem().toString();
            manage.inviteUser(username, projectName, projectRole);
        } else {
            setErrText("Please enter a username!");
        }
    }

    public void finishInvite() {
        System.out.println("Sent Invitation");
        userEdit.getText().clear();
        Toast.makeText(getApplicationContext(), "Invitation Sent", Toast.LENGTH_SHORT).show();
    }

    private void viewApplication() {
        startActivity(new Intent(InviteActivity.this, ViewApplicationActivity.class));
    }
}
