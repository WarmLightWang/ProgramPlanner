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
import com.example.myapplication.element.Session;
import com.example.myapplication.element.Task;
import com.example.myapplication.engine.ManageTaskView;
import com.example.myapplication.engine.Validation;

import java.util.ArrayList;
import java.util.List;

public class TaskAssignActivity extends ProgramActivity {

    List<Task> taskList;
    List<String> deleteList;
    ManageTaskView manageTaskView;
    LinearLayout taskLayout;
    boolean isDelete = false, isEdit = false;
    Button btAdd, btEdit, btDelete, btCancel, btBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_assignment_view);
        setupUI(findViewById(R.id.taskViewActivity));
        String projectId = Session.getInstance().getProjectId();
        String username = Session.getInstance().getUserName();
        manageTaskView = new ManageTaskView(this, projectId);
        validation = new Validation(username, projectId);
        taskList = new ArrayList<>();
        deleteList = new ArrayList<>();
        manageTaskView.getTaskList();
        setupButton();
    }

    private void setupButton() {
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
                    confirmDeleteTask(TaskAssignActivity.this, deleteList);
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

    public void setupTaskView(List<Task> list) {
        this.taskList = list;
        setupTaskView();
    }

    private void setupTaskView() {
        taskLayout = findViewById(R.id.TaskList);
        taskLayout.removeAllViews();

        if (!isDelete) {
            if (taskList.isEmpty()) {
                String info = "There is no task for the project";
                TextView infoView = new TextView(this);
                infoView.setText(info);
                infoView.setTextSize(20);
                infoView.setPadding(5, 5, 5, 5);
                infoView.setClickable(false);
                taskLayout.addView(infoView);
                hideButton(true);
            } else {
                hideButton(false);
            }
        }

        for (int i = 0; i < taskList.size(); i++) {
            final TextView taskView = new TextView(this);
            Task temp = taskList.get(i);
            String taskInfo = temp.toString();
            taskView.setText(taskInfo);
            taskView.setTextSize(25);
            taskView.setPadding(5, 5, 5, 5);
            taskView.setClickable(true);
            taskView.setOnClickListener(v -> {
                int index = ((ViewGroup) taskView.getParent()).indexOfChild(taskView);

                if (isDelete) {
                    deleteList.add(taskList.get(index).taskId);
                    taskView.setVisibility(View.GONE);
                } else if (isEdit) {
                    editTask(taskList.get(index).taskId);
                }
            });
            taskLayout.addView(taskView);
        }
    }

    private void toEdit() {
        String editMsg;
        if (!isEdit) {
            isEdit = true;
            btAdd.setVisibility(View.INVISIBLE);
            btCancel.setVisibility(View.INVISIBLE);
            btDelete.setVisibility(View.INVISIBLE);
            btBack.setVisibility(View.INVISIBLE);
            editMsg = "Cancel Edit";
        } else {
            isEdit = false;
            resetTaskLayout();
            btAdd.setVisibility(View.VISIBLE);
            btEdit.setVisibility(View.VISIBLE);
            btBack.setVisibility(View.VISIBLE);
            btDelete.setVisibility(View.VISIBLE);
            editMsg = "Edit";
        }
        btEdit.setText(editMsg);
    }

    private void editTask(String taskId) {
        Intent intent = new Intent(TaskAssignActivity.this, AddTaskActivity.class);
        intent.putExtra("taskId", taskId);
        startActivity(intent);
    }

    private void toAdd() {
        Intent intent = new Intent(TaskAssignActivity.this, AddTaskActivity.class);
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

            label = "Confirm";
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

    private void resetTaskLayout() {
        int count = taskLayout.getChildCount();
        List<Task> tempTaskList = new ArrayList<>(taskList);

        taskList.clear();
        for (int i = 0; i < count; i++) {
            TextView taskView = (TextView) taskLayout.getChildAt(i);
            int visible = taskView.getVisibility();
            if (visible != View.GONE) {
                taskList.add(tempTaskList.get(i));
            }
        }
    }

    private void cancelDelete() {
        if (!deleteList.isEmpty())
            Toast.makeText(getApplicationContext(), "Cancel Task Delete", Toast.LENGTH_SHORT).show();
        deleteList.clear();
        setupTaskView(taskList);
    }

    private void confirmDeleteTask(Context context, List<String> deleteList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Task Confirmation");
        String deleteInfo;
        if (!deleteList.isEmpty()) {
            StringBuilder temp = new StringBuilder("Are you sure you want to delete the task with the following Task ID?\n");

            for (String task : deleteList)
                temp.append(String.format("%s, ", task));

            deleteInfo = temp.toString();
            builder.setPositiveButton("Confirm", (dialog, which) -> manageTaskView.confirmRemove(deleteList));
            builder.setNegativeButton("Cancel", (dialog, which) -> cancelDelete());
        } else {
            deleteInfo = "There is no task chosen to delete";
            builder.setNeutralButton("Cancel", (dialog, which) -> {

            });
        }

        builder.setMessage(deleteInfo);
        builder.show();
    }
}
