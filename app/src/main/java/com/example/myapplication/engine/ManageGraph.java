package com.example.myapplication.engine;

import androidx.annotation.NonNull;

import com.example.myapplication.activity.GraphActivity;
import com.example.myapplication.element.Image;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ManageGraph {

    FirebaseDatabase firebase;
    DatabaseReference db_ref;
    FirebaseStorage storage;
    GraphActivity activity;

    public ManageGraph(GraphActivity activity, String projectId) {
        firebase = FirebaseDatabase.getInstance();
        db_ref = firebase.getReference("Project").child(projectId).child("Images");
        storage = FirebaseStorage.getInstance();
        this.activity = activity;
    }

    public void readImage() {
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                activity.clearList();
                List<Image> imageList = new ArrayList<>();
                List<String> keyList = new ArrayList<>();

                for (DataSnapshot snap : snapshot.getChildren()) {
                    Image image = snap.getValue(Image.class);
                    keyList.add(snap.getKey());
                    imageList.add(image);
                    System.out.println("Image: " + image.ImageUrl);
                }
                activity.setImage(imageList, keyList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                activity.loadErr();
            }
        });
    }

    public void deleteImage(final String key, String imageUrl, final int pos) {
        StorageReference stg_ref = storage.getReferenceFromUrl(imageUrl);
        stg_ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                db_ref.child(key).removeValue();
                activity.finishDelete(pos);
            }
        });
    }
}
