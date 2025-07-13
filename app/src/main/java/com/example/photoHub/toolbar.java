package com.example.photoHub;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.core.view.WindowCompat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class toolbar extends AppCompatActivity {
    Toolbar tlbr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        setContentView(R.layout.activity_toolbar);

        tlbr = findViewById(R.id.toolbar); // safe now

        setSupportActionBar(tlbr);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tlbr.setTitle("PhotoHub");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.tlbr_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.abtus) {
            // Handle About Us
        } else if (itemId == R.id.sprt) {
            // Handle Support
        } else if (itemId == android.R.id.home) {
            onBackPressed(); // correct method
        } else
            System.out.println("hi");
        //invite

        return super.onOptionsItemSelected(item);


    }
}