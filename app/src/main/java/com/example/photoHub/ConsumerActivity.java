package com.example.photoHub;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ConsumerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Adapter adapter;
    private ArrayList<Photographer> photographerList = new ArrayList<>();
    private ArrayList<Photographer> filteredList = new ArrayList<>();
    private EditText searchInput;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer);
        makeStatusBarLight();  // Light status bar for better visibility

        // Bind Views
        searchInput = findViewById(R.id.searchInput);
        recyclerView = findViewById(R.id.rvw);
        progressBar = findViewById(R.id.progressBar);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, filteredList);
        recyclerView.setAdapter(adapter);

        // Load from Firebase
        loadPhotographers();

        // Filter on typing
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterPhotographers(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void loadPhotographers() {
        progressBar.setVisibility(View.VISIBLE);  // Show loader

        FirebaseDatabase.getInstance().getReference("photographers")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override public void onDataChange(@NonNull DataSnapshot snapshot) {
                        photographerList.clear();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            String uid = snap.getKey();
                            String name = snap.child("name").getValue(String.class);
                            String exp = snap.child("experience").getValue(String.class);
                            String profileBase64 = snap.child("portfolioImage1").getValue(String.class);

                            byte[] img = profileBase64 != null ? android.util.Base64.decode(profileBase64, android.util.Base64.DEFAULT) : null;
                            Photographer p = new Photographer(name, exp, img);
                            p.setUid(uid);
                            photographerList.add(p);
                        }

                        filteredList.clear();
                        filteredList.addAll(photographerList);
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);  // Hide loader
                    }

                    @Override public void onCancelled(@NonNull DatabaseError error) {
                        progressBar.setVisibility(View.GONE);  // Hide loader on failure
                    }
                });
    }

    private void filterPhotographers(String text) {
        filteredList.clear();
        for (Photographer p : photographerList) {
            if (p.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(p);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void makeStatusBarLight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.setStatusBarColor(Color.WHITE);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}
