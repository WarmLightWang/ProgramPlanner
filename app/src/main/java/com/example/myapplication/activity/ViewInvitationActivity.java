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
import com.example.myapplication.element.Invitation;
import com.example.myapplication.element.Session;
import com.example.myapplication.engine.ManageInvitation;

import java.util.ArrayList;
import java.util.List;

public class ViewInvitationActivity extends ProgramActivity {

    LinearLayout inviteLayout;
    List<Invitation> inviteList;
    List<Integer> inviteViewIdList;
    Session session;
    ManageInvitation manageInvite;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_invitation);
        setupUI(findViewById(R.id.inviteViewActivity));
        inviteLayout = findViewById(R.id.inviteLayout);
        inviteViewIdList = new ArrayList<>();
        session = Session.getInstance();
        manageInvite = new ManageInvitation(this, session.getUserName());
        manageInvite.getInvitationList();
    }

    public void setupLayout(List<Invitation> inviteList) {
        this.inviteList = inviteList;
        inviteLayout = findViewById(R.id.inviteLayout);
        inviteLayout.removeAllViews();
        if (inviteList.isEmpty()) {
            String info = "There is no invitation received";
            TextView infoView = new TextView(this);
            infoView.setText(info);
            infoView.setTextSize(20);
            infoView.setPadding(5, 5, 5, 5);
            infoView.setClickable(false);
            inviteLayout.addView(infoView);
        }

        for (int i = 0; i < inviteList.size(); i++) {
            final TextView inviteView = new TextView(this);
            Invitation temp = inviteList.get(i);

            String inviteInfo = String.format("%s : %s  (%s)", temp.projectId, temp.projectName, temp.projectRole);
            int viewId = View.generateViewId();
            inviteViewIdList.add(viewId);
            inviteView.setId(viewId);
            inviteView.setText(inviteInfo);
            inviteView.setTextSize(20);
            inviteView.setPadding(5, 5, 5, 5);
            inviteView.setClickable(true);
            inviteView.setOnClickListener(v -> {
                int index = inviteLayout.indexOfChild(inviteView);
                viewInvite(index);
            });
            inviteLayout.addView(inviteView);
        }
    }

    private void viewInvite(int index) {
        //Appear dialog box
        Invitation invitation = inviteList.get(index);
        showInviteDialog(ViewInvitationActivity.this, invitation);
    }

    public void finishViewInvite(String message) {
        Toast.makeText(ViewInvitationActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void showInviteDialog(Context context, final Invitation invitation) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String inviteDetails = invitation.toString();
        builder.setMessage(inviteDetails)
                .setTitle("Project Invitation");
        builder.setPositiveButton("Accept", (dialog, which) -> manageInvite.acceptInvite(invitation));
        builder.setNegativeButton("Decline", (dialog, which) -> manageInvite.declineInvite(invitation));
        builder.setNeutralButton("Cancel", (dialog, which) -> {
        });
        builder.show();
    }
}
