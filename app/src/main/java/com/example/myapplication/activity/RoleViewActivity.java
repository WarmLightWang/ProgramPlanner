package com.example.myapplication.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.myapplication.R;
import com.example.myapplication.element.Roles;
import com.example.myapplication.element.Session;
import com.example.myapplication.engine.ManageRoles;

import java.util.ArrayList;
import java.util.List;

public class RoleViewActivity extends ProgramActivity {

    LinearLayout roleLayout;
    ManageRoles manageRoles;
    Session session;
    List<Integer> roleViewIdList;
    List<Roles> rolesList;
    List<String> kickList;
    Button btKick, btCancel, btChangeRole, btBack;
    boolean isKick = false, isChange = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_role);
        setupUI(findViewById(R.id.roleManageActivity));
        roleLayout = findViewById(R.id.roleLayout);
        session = Session.getInstance();
        roleViewIdList = new ArrayList<>();
        kickList = new ArrayList<>();
        manageRoles = new ManageRoles(this, session.getProjectId());
        manageRoles.getRolesList();
        setupButton();
    }

    private void hideButton(boolean isHide) {
        if (isHide) {
            btKick.setVisibility(View.INVISIBLE);
            btChangeRole.setVisibility(View.INVISIBLE);
        } else {
            btKick.setVisibility(View.VISIBLE);
            btChangeRole.setVisibility(View.VISIBLE);
        }
    }

    public void setupLayout(final List<Roles> rolesList) {
        this.rolesList = rolesList;
        roleLayout = findViewById(R.id.roleLayout);
        roleLayout.removeAllViews();

        if (!isKick)
            hideButton(rolesList.size() == 1);

        for (int i = 0; i < rolesList.size(); i++) {
            final TextView roleView = new TextView(this);
            Roles temp = rolesList.get(i);
            int viewId = View.generateViewId();
            roleViewIdList.add(viewId);
            roleView.setId(viewId);
            roleView.setText(temp.toString());
            roleView.setTextSize(20);
            roleView.setPadding(5, 5, 5, 5);
            if (!temp.roles.equalsIgnoreCase("manager")) {
                roleView.setClickable(true);
                roleView.setOnClickListener(v -> {
                    int index = roleLayout.indexOfChild(roleView);
                    if (isKick) {
                        kickList.add(rolesList.get(index).username);
                        roleView.setVisibility(View.GONE);

                    } else if (isChange) {
                        Roles currRoles = rolesList.get(index);
                        if (currRoles.roles.equalsIgnoreCase("developer")) {
                            currRoles.roles = "client";
                        } else {
                            currRoles.roles = "developer";
                        }
                        roleView.setText(currRoles.toString());
                        rolesList.set(index, currRoles);
                    }
                });
            }
            roleLayout.addView(roleView);
        }
    }

    private void setupButton() {
        btCancel = findViewById(R.id.buttonCancel);
        btCancel.setVisibility(View.INVISIBLE);
        btCancel.setOnClickListener(v -> {
            if (isKick) {
                toKick();
            } else if (isChange) {
                toChange();
                recreate();
            }
        });

        btKick = findViewById(R.id.buttonKick);
        btKick.setOnClickListener(v -> {
            if (!isKick)
                toKick();
            else
                confirmKickDialog(RoleViewActivity.this, kickList);
        });

        btChangeRole = findViewById(R.id.buttonChangeRole);
        btChangeRole.setOnClickListener(v -> {
            if (!isChange)
                toChange();
            else {
                manageRoles.changeRole(rolesList);
            }
        });
        btBack = findViewById(R.id.buttonBack);
        btBack.setOnClickListener(v -> finish());
    }

    private void toChange() {
        String change = "Change Role";
        String confirm = "Confirm";
        if (!isChange) {
            isChange = true;
            btChangeRole.setText(confirm);
            btKick.setVisibility(View.INVISIBLE);
            btBack.setVisibility(View.INVISIBLE);
            btCancel.setVisibility(View.VISIBLE);
        } else {
            isChange = false;
            btChangeRole.setText(change);
            btKick.setVisibility(View.VISIBLE);
            btBack.setVisibility(View.VISIBLE);
            btCancel.setVisibility(View.INVISIBLE);
        }
    }

    private void toKick() {
        String kick = "Kick Member";
        String confirm = "Confirm";
        if (!isKick) {
            isKick = true;
            btChangeRole.setVisibility(View.INVISIBLE);
            btBack.setVisibility(View.INVISIBLE);
            btCancel.setVisibility(View.VISIBLE);
            btKick.setText(confirm);
        } else {
            cancelKick();
            isKick = false;
            btChangeRole.setVisibility(View.VISIBLE);
            btBack.setVisibility(View.VISIBLE);
            btCancel.setVisibility(View.INVISIBLE);
            btKick.setText(kick);
        }
    }

    private void resetRoleLayout() {
        int count = roleLayout.getChildCount();
        List<Roles> tempRoleList = new ArrayList<>(rolesList);
        rolesList.clear();
        for (int i = 0; i < count; i++) {
            TextView roleView = (TextView) roleLayout.getChildAt(i);
            int visible = roleView.getVisibility();
            if (visible != View.GONE) {
                rolesList.add(tempRoleList.get(i));
            }
        }
    }

    public void reset() {
        Toast.makeText(this, "Member kicked Successfully", Toast.LENGTH_SHORT).show();
        kickList.clear();
        toKick();
    }

    public void finishChangeRole() {
        Toast.makeText(this, "Roles Changed Successfully", Toast.LENGTH_SHORT).show();
        toChange();
    }

    private void confirmKickDialog(Context context, List<String> kickMemberList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Kick Confirmation");
        String kickInfo;
        if (!kickMemberList.isEmpty()) {
            StringBuilder temp = new StringBuilder("Are you sure you want to kick the following members? \n");

            for (String member : kickMemberList)
                temp.append(String.format("\t%s\n", member));

            kickInfo = temp.toString();
            builder.setPositiveButton("Confirm", (dialog, which) -> manageRoles.kickMember(kickMemberList));
            builder.setNegativeButton("Cancel", (dialog, which) -> cancelKick());
        } else {
            kickInfo = "There is no member chosen to delete";
            builder.setNeutralButton("Cancel", (dialog, which) -> {
            });
        }
        builder.setMessage(kickInfo);
        builder.show();
    }

    private void cancelKick() {
        if (!kickList.isEmpty())
            Toast.makeText(getApplicationContext(), "Cancel Kick Member", Toast.LENGTH_SHORT).show();
        kickList.clear();
        setupLayout(rolesList);
    }
}
