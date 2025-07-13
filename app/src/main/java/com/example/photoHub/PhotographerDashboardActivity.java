package com.example.photoHub;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.photoHub.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PhotographerDashboardActivity extends AppCompatActivity {

    private ImageView profileImg;
    private TextView nameText, experienceText, servicesText, contactText, linksText;
    private GridLayout portfolioGrid;

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    private ProgressBar Loader;
    private View scrollContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photographer_dashboard);
        syncStatusBar();

        // Initialize views in correct order
        Loader = findViewById(R.id.dashboardLoader);
        scrollContent = findViewById(R.id.dashboardScroll);
        scrollContent.setVisibility(View.GONE);
        Loader.setVisibility(View.VISIBLE);

        Toolbar toolbar = findViewById(R.id.topToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        dbRef = FirebaseDatabase.getInstance().getReference("photographers").child(uid);

        profileImg = findViewById(R.id.profileImage);
        nameText = findViewById(R.id.nameText);
        experienceText = findViewById(R.id.experienceText);
        servicesText = findViewById(R.id.servicesText);
        contactText = findViewById(R.id.contactText);
        linksText = findViewById(R.id.linksText);
        portfolioGrid = findViewById(R.id.portfolioGrid);

        findViewById(R.id.editPortfolioBtn).setOnClickListener(v ->
                startActivity(new Intent(this, PortfolioUploadActivity.class))
        );

        fetchPhotographerData();
    }

    private void fetchPhotographerData() {
        dbRef.get().addOnSuccessListener(snapshot -> {
            Loader.setVisibility(View.GONE);
            scrollContent.setVisibility(View.VISIBLE);

            if (!snapshot.exists()) {
                Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = snapshot.child("name").getValue(String.class);
            String exp = snapshot.child("experience").getValue(String.class);
            String service = snapshot.child("service").getValue(String.class);
            String contact = snapshot.child("contact").getValue(String.class);
            String social = snapshot.child("socialLink").getValue(String.class);
            String drive = snapshot.child("driveLink").getValue(String.class);

            nameText.setText(name);
            experienceText.setText(exp + " years");
            servicesText.setText(service);
            contactText.setText(contact);

            // Contact number clickable
            contactText.setOnClickListener(v -> {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + contact));
                startActivity(dialIntent);
            });

            // Links clickable
            String linksHtml = "";
            if (social != null && !social.isEmpty()) {
                linksHtml += "<a href='" + social + "'>Instagram</a><br>";
            }
            if (drive != null && !drive.isEmpty()) {
                linksHtml += "<a href='" + drive + "'>Portfolio</a>";
            }
            if (!linksHtml.isEmpty()) {
                linksText.setText(Html.fromHtml(linksHtml));
                linksText.setMovementMethod(LinkMovementMethod.getInstance());
            } else {
                linksText.setText("No links available");
            }

            // Load portfolio images
            portfolioGrid.removeAllViews();
            int spacing = 24;
            int columnCount = 3;
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            int imageSize = (screenWidth - (spacing * (columnCount + 1))) / columnCount;
            portfolioGrid.setColumnCount(columnCount);

            for (int i = 1; i <= 9; i++) {
                String img64 = snapshot.child("portfolioImage" + i).getValue(String.class);

                if (img64 != null && !img64.trim().isEmpty()) {
                    try {
                        byte[] imgBytes = android.util.Base64.decode(img64.trim(), android.util.Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);

                        if (bitmap != null) {
                            // Set as profile image if first
                            if (i == 1) profileImg.setImageBitmap(bitmap);

                            ImageView imageView = new ImageView(this);
                            imageView.setImageBitmap(bitmap);
                            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
                            lp.width = imageSize;
                            lp.height = imageSize;
                            lp.setMargins(spacing, spacing, spacing, spacing);
                            imageView.setLayoutParams(lp);

                            portfolioGrid.addView(imageView);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }).addOnFailureListener(e ->
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }

    private void syncStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.WHITE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tlbr_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.abtus) {
            startActivity(new Intent(this, AboutActivity.class));
        } else if (id == R.id.sprt) {
            startActivity(new Intent(this, SupportActivity.class));
        } else if (id == R.id.logout) {
            mAuth.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
