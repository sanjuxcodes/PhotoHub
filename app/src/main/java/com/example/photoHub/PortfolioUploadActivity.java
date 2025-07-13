package com.example.photoHub;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PortfolioUploadActivity extends AppCompatActivity {

    private static final int IMAGE_COUNT = 9;
    private ImageView[] imageViews = new ImageView[IMAGE_COUNT];
    private byte[][] imageBytes = new byte[IMAGE_COUNT][];
    private int currentImageIndex = -1;

    private String name, exp, service, contact, social, driveLink;

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        imageViews[currentImageIndex].setImageBitmap(bitmap);
                        //h
                        imageBytes[currentImageIndex] = bitmapToByteArray(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
                    }
                    finally {
                        Toast.makeText(this,"Done",Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio_upload);
        makeStatusBarLight();


        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        exp = intent.getStringExtra("experience");
        service = intent.getStringExtra("service");
        contact = intent.getStringExtra("contact");
        social = intent.getStringExtra("social");
        driveLink = intent.getStringExtra("driveLink");


        for (int i = 0; i < IMAGE_COUNT; i++) {
            String viewId = "image_" + i;
            int resId = getResources().getIdentifier(viewId, "id", getPackageName());
            imageViews[i] = findViewById(resId);
            final int index = i;
            imageViews[i].setOnClickListener(v -> {
                currentImageIndex = index;
                pickImage();
            });
        }

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(v -> handleSubmit());
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        return stream.toByteArray();
    }

    private void handleSubmit() {
        for (int i = 0; i < 5; i++) {
            if (imageBytes[i] == null) {
                Toast.makeText(this, "Please upload more images", Toast.LENGTH_SHORT).show();
                return;
            }
        }


        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("photographers").child(uid);

        Map<String, Object> photographerData = new HashMap<>();
        photographerData.put("name", name);
        photographerData.put("experience", exp);
        photographerData.put("service", service);
        photographerData.put("contact", contact);
        photographerData.put("socialLink", social);
        photographerData.put("driveLink", driveLink);


        for (int i = 0; i < IMAGE_COUNT; i++) {
            photographerData.put("portfolioImage" + (i + 1), Base64.encodeToString(imageBytes[i], Base64.DEFAULT));
        }

        dbRef.setValue(photographerData)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Portfolio uploaded successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, ConsumerActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    private void makeStatusBarLight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.setStatusBarColor(Color.WHITE);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}

