package com.example.photoHub;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PhotographerDetailsActivity extends AppCompatActivity {

    private ImageView profileImage;
    private TextView nameText, expText, serviceText;
    private GridLayout portfolioContainer;
    private ImageView phoneIcon, instagramIcon, driveIcon, backBtn;
    private ProgressBar progressBar;

    private String phone, socialLink, driveLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photographer_details);
        makeStatusBarLight();

        // View bindings
        progressBar = findViewById(R.id.progressBar);
        profileImage = findViewById(R.id.profileImage);
        nameText = findViewById(R.id.pnameText);
        expText = findViewById(R.id.pexperienceText);
        serviceText = findViewById(R.id.pserviceText);
        portfolioContainer = findViewById(R.id.portfolioGrid);
        phoneIcon = findViewById(R.id.phoneIcon);
        instagramIcon = findViewById(R.id.instagramIcon);
        driveIcon = findViewById(R.id.portfolioIcon);
        backBtn = findViewById(R.id.backBtn);

        // Back button
        backBtn.setOnClickListener(v -> onBackPressed());

        // Get UID and load data
        String uid = getIntent().getStringExtra("uid");
        if (uid != null) {
            loadPhotographerDetails(uid);
        } else {
            Toast.makeText(this, "No photographer ID provided", Toast.LENGTH_SHORT).show();
        }

        // Click listeners
        phoneIcon.setOnClickListener(v -> {
            if (phone != null && !phone.isEmpty()) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone)));
            }
        });

        instagramIcon.setOnClickListener(v -> {
            if (socialLink != null && !socialLink.isEmpty()) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(socialLink)));
            }
        });

        driveIcon.setOnClickListener(v -> {
            if (driveLink != null && !driveLink.isEmpty()) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(driveLink)));
            }
        });
    }

    private void loadPhotographerDetails(String uid) {
        progressBar.setVisibility(View.VISIBLE);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("photographers").child(uid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);

                if (!snapshot.exists()) {
                    Toast.makeText(PhotographerDetailsActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Set basic info
                String name = snapshot.child("name").getValue(String.class);
                String exp = snapshot.child("experience").getValue(String.class);
                String service = snapshot.child("service").getValue(String.class);

                phone = snapshot.child("contact").getValue(String.class);
                socialLink = snapshot.child("socialLink").getValue(String.class);
                driveLink = snapshot.child("driveLink").getValue(String.class);

                nameText.setText(name);
                expText.setText(exp + " years");
                serviceText.setText(service);

                // Grid layout setup
                int columnCount = 2;
                int spacing = 16;
                int screenWidth = getResources().getDisplayMetrics().widthPixels;
                int imageSize = (screenWidth / columnCount) - (spacing * 2);

                for (int i = 1; i <= 9; i++) {
                    String key = "portfolioImage" + i;
                    String base64 = snapshot.child(key).getValue(String.class);

                    if (base64 != null && !base64.trim().isEmpty()) {
                        try {
                            byte[] bytes = Base64.decode(base64.trim(), Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                            if (i == 1) {
                                profileImage.setImageBitmap(bitmap);
                            } else {
                                ImageView imageView = new ImageView(PhotographerDetailsActivity.this);
                                imageView.setImageBitmap(bitmap);
                                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                                params.width = imageSize;
                                params.height = imageSize;
                                params.setMargins(spacing, spacing, spacing, spacing);
                                imageView.setLayoutParams(params);

                                portfolioContainer.addView(imageView);
                            }
                        } catch (Exception e) {
                            e.printStackTrace(); // skip broken image
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(PhotographerDetailsActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
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
