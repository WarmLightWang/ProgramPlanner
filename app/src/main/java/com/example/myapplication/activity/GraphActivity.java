package com.example.myapplication.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.engine.ImageAdapter;
import com.example.myapplication.R;
import com.example.myapplication.element.Image;
import com.example.myapplication.element.Session;
import com.example.myapplication.engine.Validation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GraphActivity extends ProgramActivity implements ImageAdapter.OnItemClickListen {
    private RecyclerView RecView;
    private ImageAdapter IAdapter;
    private DatabaseReference DB_Ref;
    private FirebaseStorage Storage;
    private List<Image> Limages;
    private ProgressBar pro_Cir;
    private List<String> Del_list;
    private final static int REQUEST_CODE = 11;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_view);
        RecView = findViewById(R.id.recycler_vi);
        RecView.setLayoutManager(new LinearLayoutManager(this));
        RecView.setHasFixedSize(true);
        pro_Cir = findViewById(R.id.progress_circular);
        Limages = new ArrayList<>();
        Del_list = new ArrayList<>();
        String projectId = Session.getInstance().getProjectId();
        String username = Session.getInstance().getUserName();
        validation = new Validation(username, projectId);
        IAdapter = new ImageAdapter(GraphActivity.this, Limages);
        RecView.setAdapter(IAdapter);
        IAdapter.setOnItemClickListener(GraphActivity.this);
        DB_Ref = FirebaseDatabase.getInstance().getReference("Project").child(Session.getInstance().getProjectId()).child("Images");
        Storage = FirebaseStorage.getInstance();
        Read_Images();
        setupButton();
    }

    public void clearList() {
        Del_list.clear();
        Limages.clear();
    }

    public void setImage(List<Image> imageList, List<String> keyList) {
        this.Limages = imageList;
        this.Del_list = keyList;
        System.out.println("Image: " + Limages.toString());
        IAdapter.notifyDataSetChanged();
        pro_Cir.setVisibility(View.INVISIBLE);
    }

    public void loadErr() {
        Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
        pro_Cir.setVisibility(View.INVISIBLE);
    }

    private void Read_Images() {
        DB_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Del_list.clear();
                Limages.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Image images = snapshot.getValue(Image.class);
                    Del_list.add(snapshot.getKey());
                    Limages.add(images);
                }

                IAdapter.notifyDataSetChanged();
                pro_Cir.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                pro_Cir.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void setupButton() {
        Button btUpload = findViewById(R.id.buttonUpload);
        btUpload.setOnClickListener(v -> {
            if (validateRole())
                startActivity(new Intent(GraphActivity.this, ImageChooser.class));
        });

        Button btBack = findViewById(R.id.buttonBack);
        btBack.setOnClickListener(v -> {
            validateRole();
            finish();
        });
    }

    @Override
    public void onItemClick(int position) {
        Image Item = Limages.get(position);
        Intent intent = new Intent(this, FullScreenImageActivity.class);
        intent.setData(Uri.parse(Item.ImageUrl));
        startActivity(intent);
    }

    public void finishDelete(int pos) {
        Del_list.remove(pos);
        Toast.makeText(getApplicationContext(), "Item Deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnDeleteClick(int position) {
        Image Item = Limages.get(position);
        final String Key = Del_list.get(position);
        final int pot = position;
        StorageReference S_Ref = Storage.getReferenceFromUrl(Item.ImageUrl);
        S_Ref.delete().addOnSuccessListener(aVoid -> {
            DB_Ref.child(Key).removeValue();
            Del_list.remove(pot);
            Toast.makeText(getApplicationContext(), "Item Deleted", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void OnSaveClick(int position) {
        checkPermission();
        if (checkPermission()) {
            Image Item = Limages.get(position);
            Picasso.with(getApplicationContext()).load(Item.ImageUrl).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    FileOutputStream outputStream;
                    try {
                        File path = getFilesDir();
                        String fileUri = path.getAbsoluteFile() + File.separator + System.currentTimeMillis() + ".jpg";
                        outputStream = new FileOutputStream(fileUri);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        outputStream.flush();
                        outputStream.close();
                        Toast.makeText(getApplicationContext(), "Item Saved", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Permission needed", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkPermission() {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if ((ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[0]) != PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[1]) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
            return false;
        }
        return true;
    }
}
