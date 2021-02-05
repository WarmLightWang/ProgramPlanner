package com.example.myapplication.engine;

import androidx.annotation.NonNull;

import com.example.myapplication.activity.ViewInvitationActivity;
import com.example.myapplication.element.Invitation;
import com.example.myapplication.element.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageInvitation {

    FirebaseDatabase firebase;
    DatabaseReference db_ref_user, db_ref_roles;
    ViewInvitationActivity activity;
    String username;
    List<Invitation> invitationList;

    public ManageInvitation(ViewInvitationActivity activity, String username) {
        firebase = FirebaseDatabase.getInstance();
        db_ref_user = firebase.getReference("Users");
        db_ref_roles = firebase.getReference("Roles");
        this.username = username;
        this.activity = activity;
    }

    public void acceptInvite(Invitation invitation) {
        String inviteId = invitation.inviteId;
        String projectId = invitation.projectId;
        String projectRole = invitation.projectRole;
        updateRole(projectId, projectRole);
        deleteInvite(inviteId);

        String logMessage = String.format("%s's accepted the invitation and " +
                "joined the project team.", username);
        Log log = new Log(logMessage, "SYSTEM");
        new ManageLog(projectId, log);
        activity.finishViewInvite("Accepted Invitation");
    }

    public void declineInvite(Invitation invitation) {
        deleteInvite(invitation.inviteId);
        activity.finishViewInvite("Declined Invitation");
    }

    public void getInvitationList() {
        db_ref_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                invitationList = new ArrayList<>();
                snapshot = snapshot.child(username).child("Invitation");
                for (DataSnapshot snap : snapshot.getChildren()) {
                    String inviteId = snap.getKey();
                    String projectId = snap.child("projectId").getValue().toString();
                    String projectName = snap.child("projectName").getValue().toString();
                    String projectRole = snap.child("projectRole").getValue().toString();

                    Invitation tempInvite = new Invitation(inviteId, projectId, projectName, projectRole);
                    invitationList.add(tempInvite);
                }
                activity.setupLayout(invitationList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void deleteInvite(final String inviteId) {
        db_ref_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                db_ref_user.child(username).child("Invitation").child(inviteId).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateRole(final String projectId, final String projectRole) {
        db_ref_roles.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                db_ref_roles.child(projectId).child(username).child("Roles").setValue(projectRole);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
