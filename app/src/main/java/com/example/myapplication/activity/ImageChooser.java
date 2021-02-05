package com.example.myapplication.activity;

import androidx.annotation.Nullable;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.element.Image;
import com.example.myapplication.element.Session;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class ImageChooser extends ProgramActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ProgressBar ProBar;
    private Uri ImageUri;
    private ImageView ImageV;
    private StorageReference StorageRef;
    private DatabaseReference DB_Ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_chooser);
        String projectIdStr = Session.getInstance().getProjectId();
        StorageRef = FirebaseStorage.getInstance().getReference("Images");
        DB_Ref = FirebaseDatabase.getInstance().getReference("Project").child(projectIdStr).child("Images");
        setupButton();
        ImageV = findViewById(R.id.image_view);
        ProBar = findViewById(R.id.pro_bar);
    }

    private void setupButton() {
        Button bt_image = findViewById(R.id.bt_choose_image);
        bt_image.setOnClickListener(v -> FileChooser());
        Button bt_upload = findViewById(R.id.bt_upload_image);
        bt_upload.setOnClickListener(v -> Upload());
        Button bt_back = findViewById(R.id.bt_back);
        bt_back.setOnClickListener(v -> finish());

    }

    private void FileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            ImageUri = data.getData();
            Picasso.with(this).load(ImageUri).into(ImageV);
        }
    }

    private void Upload() {
        if (ImageUri != null) {
            StorageReference Ref = StorageRef.child(System.currentTimeMillis()
                    + "." + FileExt(ImageUri));
            Ref.putFile(ImageUri).addOnSuccessListener(taskSnapshot -> {
                Handler handler = new Handler();
                handler.postDelayed(() -> ProBar.setProgress(0), 500);

                Toast.makeText(getApplicationContext(), "Upload Successfully!", Toast.LENGTH_SHORT).show();

                com.google.android.gms.tasks.Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl();
                downloadUri.addOnSuccessListener(uri -> {
                    String uploadId = DB_Ref.push().getKey();
                    String imageUri = uri.toString();
                    Image image = new Image(imageUri);
                    System.out.println("ImageUri = " + imageUri);
                    DB_Ref.child(uploadId).setValue(image);
                });
                finish();

            }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Upload failed!", Toast.LENGTH_SHORT).show()).addOnProgressListener(snapshot -> {
                double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                ProBar.setProgress((int) progress);
            });
        } else {
            Toast.makeText(getApplicationContext(), "No file selected!", Toast.LENGTH_SHORT).show();
        }
    }

    private String FileExt(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}
