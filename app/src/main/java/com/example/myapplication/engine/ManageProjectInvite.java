package com.example.myapplication.engine;

import androidx.annotation.NonNull;

import com.example.myapplication.activity.InviteActivity;
import com.example.myapplication.element.Invitation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageProjectInvite {

    FirebaseDatabase firebase;
    DatabaseReference db_ref_project, db_ref_roles, db_ref_users;
    InviteActivity activity;
    List<String> userList;
    List<String> projectMemberList;
    String projectId;
    int inviteId;

    public ManageProjectInvite(InviteActivity activity, String projectId) {
        firebase = FirebaseDatabase.getInstance();
        db_ref_project = firebase.getReference("Project").child(projectId);
        db_ref_roles = firebase.getReference("Roles").child(projectId);
        db_ref_users = firebase.getReference("Users");
        retrieveUserData();
        retrieveMemberList();
        this.activity = activity;
        this.projectId = projectId;
    }

    public void inviteUser(String username, String projectName, String projectRoles) {
        if (username.isEmpty())
            activity.setErrText("Please enter a username");
        if (isUserExist(username) && !isMember(username)) {
            generateInviteId(username);
            sendInvite(username, projectName, projectRoles);
        }
    }

    public boolean isUserExist(String username) {
        for (String temp : userList) {
            if (temp.equals(username)) {
                return true;
            }
        }
        activity.setErrText("The user entered does not exist");
        return false;
    }

    public boolean isMember(String username) {
        for (String member : projectMemberList) {
            if (member.equals(username)) {
                activity.setErrText("The user entered is already in project");
                return true;
            }
        }
        return false;
    }

    private void retrieveUserData() {
        db_ref_users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList = new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    String username = snap.getKey();
                    userList.add(username);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void retrieveMemberList() {
        db_ref_roles.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                projectMemberList = new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    String member = snap.getKey();
                    boolean isMember = snap.child("Roles").exists();

                    if (isMember) {
                        projectMemberList.add(member);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendInvite(String username, String projectName, String projectRoles) {
        final String user = username;
        final String name = projectName;
        final String roles = projectRoles;
        System.out.println("Send Invite here");
        db_ref_users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Invitation invite = new Invitation(projectId, name, roles);
                db_ref_users.child(user).child("Invitation").child(Integer.toString(inviteId)).setValue(invite);
                activity.finishInvite();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getInviteCode() {
        db_ref_project.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String clientCode = snapshot.child("clientCode").getValue().toString();
                String devCode = snapshot.child("devCode").getValue().toString();
                boolean gotApplication = snapshot.child("Application").exists();
                System.out.println("Got Application : " + gotApplication);
                activity.setupCode(clientCode, devCode, gotApplication);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void generateInviteId(String username) {
        final String user = username;
        db_ref_users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot = snapshot.child(user).child("Invitation");
                for (DataSnapshot snap : snapshot.getChildren()) {
                    inviteId = Integer.parseInt(snap.getKey()) + 1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
