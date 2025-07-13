package com.example.photoHub;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SupportActivity extends AppCompatActivity {

    private LinearLayout emailCard, callCard, faqCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.support_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // back button
            getSupportActionBar().setTitle("PhotoHub Support");
        }

        // Get Intent Data
        String source = getIntent().getStringExtra("source");
        if (source != null) {
            Toast.makeText(this, "Opened from: " + source, Toast.LENGTH_SHORT).show();
        }

        // Initialize views
        emailCard = findViewById(R.id.card_email);
        callCard = findViewById(R.id.card_call);
        faqCard = findViewById(R.id.card_faq);

        emailCard.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:sd9735122@gmail.com"));
            startActivity(Intent.createChooser(intent, "Send Email"));
        });

        callCard.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:9832156715"));
            startActivity(intent);
        });

        faqCard.setOnClickListener(v ->
                Toast.makeText(this, "FAQs coming soon!", Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Navigate back
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
