package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.element.Project;
import com.example.myapplication.element.Roles;
import com.example.myapplication.element.Session;
import com.example.myapplication.engine.NotMyAccount;
import com.example.myapplication.engine.ProjectMain;

import java.util.ArrayList;
import java.util.List;

public class ProjectMainActivity extends ProgramActivity {

    List<Roles> projectList;
    List<Integer> projectIdList;
    LinearLayout projectLayout;
    Session session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_view);
        session = Session.getInstance();
        TextView welcomeView = findViewById(R.id.WelcomeMessage);
        String welcome = session.getUserName();
        checkCrash();
        welcomeView.setText(welcome);
        projectIdList = new ArrayList<>();
        ProjectMain main = new ProjectMain(this, session.getUserName());
        main.getProjectList();

        Button btCreate = findViewById(R.id.buttonCreate);
        btCreate.setOnClickListener(v -> createProject());

        Button btJoin = findViewById(R.id.buttonJoin);
        btJoin.setOnClickListener(v -> joinProject());

        Button btLogout = findViewById(R.id.buttonLogOut);
        btLogout.setOnClickListener(v -> logout());

        TextView NotAccount = findViewById(R.id.notYourAccountTip);
        NotAccount.setOnClickListener(v -> OpenDia());
    }

    private void checkCrash() {
        if (session.getUserName() == null) {
            logout();
        }
    }

    public void setupProjectList(List<Roles> projectList) {
        this.projectList = projectList;
        projectLayout = findViewById(R.id.projectList);
        projectLayout.removeAllViews();
        if (projectList.isEmpty()) {
            String info = "There is no project for the user";
            TextView infoView = new TextView(this);
            infoView.setText(info);
            infoView.setTextSize(20);
            infoView.setPadding(5, 5, 5, 5);
            infoView.setClickable(false);
            projectLayout.addView(infoView);
        }

        for (int i = 0; i < projectList.size(); i++) {
            final TextView projectView = new TextView(this);
            Roles temp = projectList.get(i);
            String role = String.format("%s:%s  (%s)", temp.projectId, temp.projectName, temp.roles);
            int viewId = View.generateViewId();
            projectIdList.add(viewId);

            projectView.setId(viewId);
            projectView.setText(role);
            projectView.setTextSize(20);
            projectView.setPadding(5, 5, 5, 5);
            projectView.setClickable(true);
            projectView.setOnClickListener(v -> {
                int index = projectLayout.indexOfChild(projectView);
                toProjectActivity(index);
            });
            projectLayout.addView(projectView);
        }
    }

    private void createProject() {
        projectList.clear();
        projectIdList.clear();
        startActivity(new Intent(ProjectMainActivity.this, CreateProjectActivity.class));
    }

    private void joinProject() {
        projectList.clear();
        projectIdList.clear();
        startActivity(new Intent(ProjectMainActivity.this, JoinProjectActivity.class));
    }

    private void toProjectActivity(int projectIndex) {
        Roles role = projectList.get(projectIndex);
        long projectId = Long.parseLong(role.projectId);
        String projectName = role.projectName;
        Project project = new Project(projectId, projectName);
        session.setCurrProject(project);
        String userRole = role.roles;
        Intent intent;

        if (userRole.equalsIgnoreCase("manager")) {
            intent = new Intent(ProjectMainActivity.this, DeveloperActivity.class);
            intent.putExtra("isManager", true);

        } else if (userRole.equalsIgnoreCase("developer")) {
            intent = new Intent(ProjectMainActivity.this, DeveloperActivity.class);
            intent.putExtra("isManager", false);
        } else {
            intent = new Intent(ProjectMainActivity.this, CustomerActivity.class);
        }
        startActivity(intent);
    }

    private void logout() {
        Intent intent = new Intent(ProjectMainActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void OpenDia() {
        NotMyAccount acc = new NotMyAccount();
        acc.show(getSupportFragmentManager(), "NotMyAccount");
    }
}
