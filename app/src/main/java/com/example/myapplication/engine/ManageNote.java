package com.example.myapplication.engine;

import androidx.annotation.NonNull;

import com.example.myapplication.activity.NotebookActivity;
import com.example.myapplication.element.Notebook;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageNote {

    FirebaseDatabase firebase;
    DatabaseReference db_ref;
    NotebookActivity activity;
    Notebook notebook;
    int noteId;

    public ManageNote(NotebookActivity activity, String projectId) {
        firebase = FirebaseDatabase.getInstance();
        db_ref = firebase.getReference("Project").child(projectId).child("Notebook");
        this.activity = activity;
    }

    public void getNoteList() {
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Notebook> notebookList = new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    String username = snap.child("username").getValue().toString();
                    String content = snap.child("content").getValue().toString();

                    Notebook temp = new Notebook(username, content);
                    notebookList.add(temp);
                }
                activity.setupNoteList(notebookList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addNote(Notebook notebook) {
        this.notebook = notebook;
        generateNoteId();
        addNoteToDatabase();
    }

    private void generateNoteId() {
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    noteId = Integer.parseInt(snap.getKey()) + 1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addNoteToDatabase() {
        db_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                db_ref.child(Integer.toString(noteId)).setValue(notebook);
                activity.finishAdd();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

