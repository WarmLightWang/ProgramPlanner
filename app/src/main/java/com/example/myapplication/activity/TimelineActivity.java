package com.example.myapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.myapplication.R;
import com.example.myapplication.element.Event;
import com.example.myapplication.element.Session;
import com.example.myapplication.engine.ManageTimeline;
import com.example.myapplication.engine.Validation;

import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends ProgramActivity {

    Session session;
    List<Event> eventList;
    List<String> deleteList;
    ManageTimeline manage;
    LinearLayout eventLayout;
    Button btAdd, btDelete, btCancel, btEdit, btBack;
    boolean isDelete = false, isEdit = false, isDeveloper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline_view);
        setupUI(findViewById(R.id.timelineActivity));
        session = Session.getInstance();
        String projectId = session.getProjectId();
        String username = session.getUserName();
        validation = new Validation(username, projectId);
        manage = new ManageTimeline(this, projectId);
        eventList = new ArrayList<>();
        deleteList = new ArrayList<>();
        manage.getEventList();
        Intent intent = getIntent();
        isDeveloper = intent.getExtras().getBoolean("isDeveloper");
        setup(isDeveloper);
    }

    private void setup(boolean isDeveloper) {
        btEdit = findViewById(R.id.buttonEdit);
        btEdit.setOnClickListener(v -> {
            if (validateRole())
                toEdit();
        });

        btAdd = findViewById(R.id.buttonAdd);
        btAdd.setOnClickListener(v -> {
            if (validateRole())
                toAdd();
        });

        btDelete = findViewById(R.id.buttonDelete);
        btDelete.setOnClickListener(v -> {
            if (validateRole())
                if (!isDelete)
                    toDelete();
                else
                    confirmDeleteEvent(TimelineActivity.this, deleteList);
        });

        btCancel = findViewById(R.id.buttonCancel);
        btCancel.setVisibility(View.INVISIBLE);
        btCancel.setOnClickListener(v -> {
            if (validateRole())
                toDelete();
        });

        btBack = findViewById(R.id.buttonBack);
        btBack.setOnClickListener(v -> {
            validateRole();
            finish();
        });

        if (!isDeveloper) {
            btEdit.setVisibility(View.GONE);
            btAdd.setVisibility(View.GONE);
            btDelete.setVisibility(View.GONE);
            btCancel.setVisibility(View.GONE);
        }
    }

    public void setupEventList(List<Event> list) {
        this.eventList = list;
        eventLayout = findViewById(R.id.EventLayout);
        eventLayout.removeAllViews();
        if (!isDelete) {
            if (list.isEmpty()) {
                String info = "There is no event for the project";
                TextView infoView = new TextView(this);
                infoView.setText(info);
                infoView.setTextSize(20);
                infoView.setPadding(5, 5, 5, 5);
                infoView.setClickable(false);
                eventLayout.addView(infoView);
                hideButton(true);
            } else {
                hideButton(false);
            }
        }
        System.out.println("EventList: " + eventList);
        for (int i = 0; i < eventList.size(); i++) {
            final TextView eventView = new TextView(this);
            Event temp = eventList.get(i);
            final String event = temp.eventDate + ": " + temp.eventTitle;

            eventView.setText(event);
            eventView.setTextSize(25);
            eventView.setPadding(5, 5, 5, 5);
            eventView.setOnClickListener(v -> {
                int index = ((ViewGroup) eventView.getParent()).indexOfChild(eventView);
                if (isDelete) {
                    deleteList.add(eventList.get(index).eventId);
                    eventView.setVisibility(View.GONE);
                } else if (isEdit) {
                    editEvent(eventList.get(index).eventId);
                }
            });
            eventLayout.addView(eventView);
        }
    }

    private void toEdit() {
        String editMsg;
        if (!isEdit) {
            isEdit = true;
            btAdd.setVisibility(View.INVISIBLE);
            btCancel.setVisibility(View.INVISIBLE);
            btBack.setVisibility(View.INVISIBLE);
            btDelete.setVisibility(View.INVISIBLE);
            editMsg = "Cancel Edit";
        } else {
            isEdit = false;
            btAdd.setVisibility(View.VISIBLE);
            btBack.setVisibility(View.VISIBLE);
            btDelete.setVisibility(View.VISIBLE);
            editMsg = "Edit";
        }
        btEdit.setText(editMsg);
    }

    private void hideButton(boolean isHide) {
        if (isHide) {
            btEdit.setVisibility(View.INVISIBLE);
            btDelete.setVisibility(View.INVISIBLE);
        } else {
            btEdit.setVisibility(View.VISIBLE);
            btDelete.setVisibility(View.VISIBLE);
        }
    }

    private void editEvent(String eventId) {
        Intent intent = new Intent(TimelineActivity.this, EventActivity.class);
        intent.putExtra("eventId", eventId);
        startActivity(intent);
    }

    private void toAdd() {
        Intent intent = new Intent(TimelineActivity.this, EventActivity.class);
        intent.putExtra("isTimeline", true);
        startActivity(intent);
    }

    private void toDelete() {
        String label;
        if (!isDelete) {
            isDelete = true;
            btAdd.setVisibility(View.INVISIBLE);
            btEdit.setVisibility(View.INVISIBLE);
            btCancel.setVisibility(View.VISIBLE);
            btBack.setVisibility(View.INVISIBLE);
            label = "Confirm Delete";
        } else {
            isDelete = false;
            cancelDelete();
            btAdd.setVisibility(View.VISIBLE);
            btEdit.setVisibility(View.VISIBLE);
            btBack.setVisibility(View.VISIBLE);
            btCancel.setVisibility(View.INVISIBLE);
            label = "Delete";
        }
        btDelete.setText(label);
    }

    public void reset() {
        deleteList.clear();
        toDelete();
    }

    private void resetEventLayout() {
        int count = eventLayout.getChildCount();
        List<Event> tempEventList = new ArrayList<>(eventList);
        eventList.clear();
        for (int i = 0; i < count; i++) {
            TextView eventView = (TextView) eventLayout.getChildAt(i);
            int visible = eventView.getVisibility();
            if (visible != View.GONE) {
                eventList.add(tempEventList.get(i));
            }
        }
    }

    private void cancelDelete() {
        if (!deleteList.isEmpty())
            Toast.makeText(getApplicationContext(), "Cancel Event Delete", Toast.LENGTH_SHORT).show();
        deleteList.clear();
        setupEventList(eventList);
    }

    private void confirmDeleteEvent(Context context, List<String> deleteList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Event Confirmation");
        String deleteInfo;
        if (!deleteList.isEmpty()) {
            StringBuilder temp = new StringBuilder("Are you sure you want to delete the event with the following event Id?\n");

            for (String event : deleteList)
                temp.append(String.format("%s, ", event));

            deleteInfo = temp.toString();
            builder.setPositiveButton("Confirm", (dialog, which) -> manage.confirmRemove(deleteList));
            builder.setNegativeButton("Cancel", (dialog, which) -> cancelDelete());

        } else {
            deleteInfo = "There is no event chosen to delete";
            builder.setNeutralButton("Cancel", (dialog, which) -> {

            });
        }
        builder.setMessage(deleteInfo);
        builder.show();
    }
}
