package com.example.photoHub;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class PhotographerFormActivity extends AppCompatActivity {

    private TextInputEditText nameInput, expInput, serviceInput, contactInput, socialInput, driveLinkInput;
    private MaterialButton saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photographer_form);
        makeStatusBarLight();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(v -> finish());
        }


        nameInput = findViewById(R.id.productionName);
        expInput = findViewById(R.id.experience);
        serviceInput = findViewById(R.id.services);
        contactInput = findViewById(R.id.contact);
        socialInput = findViewById(R.id.social);
        driveLinkInput = findViewById(R.id.portfolio);
        saveButton = findViewById(R.id.svbtn);

        saveButton.setOnClickListener(v -> savePhotographerData());
    }

    private void savePhotographerData() {

        String name = safeText(nameInput);
        String exp = safeText(expInput);
        String service = safeText(serviceInput);
        String contact = safeText(contactInput);
        String social = safeText(socialInput);
        String driveLink = safeText(driveLinkInput);

        boolean valid = true;

        if (name.isEmpty()) {
            nameInput.setError("Required");
            valid = false;
        }
        if (exp.isEmpty()) {
            expInput.setError("Required");
            valid = false;
        }
        if (contact.isEmpty()) {
            contactInput.setError("Required");
            valid = false;
        }

        if (!valid) return;
        Toast.makeText(this, "Saved successfully!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, PortfolioUploadActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("experience", exp);
        intent.putExtra("service", service);
        intent.putExtra("contact", contact);
        intent.putExtra("social", social);
        intent.putExtra("driveLink", driveLink);
        startActivity(intent);
    }

    private String safeText(TextInputEditText editText) {
        return editText.getText() != null ? editText.getText().toString().trim() : "";
    }

    private void makeStatusBarLight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.setStatusBarColor(Color.WHITE);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

}