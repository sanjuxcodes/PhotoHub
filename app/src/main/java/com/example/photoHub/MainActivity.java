package com.example.photoHub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {

    Button btnPhotographer, btnConsumer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        btnPhotographer = findViewById(R.id.btnPhotographer);
        btnConsumer = findViewById(R.id.btnConsumer);



        btnPhotographer.setOnClickListener(view -> {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            FirebaseDatabase.getInstance().getReference("photographers")
                    .child(uid)
                    .get()
                    .addOnSuccessListener(dataSnapshot -> {
                        if (dataSnapshot.exists()) {

                            startActivity(new Intent(MainActivity.this, PhotographerDashboardActivity.class));
                        } else {

                            startActivity(new Intent(MainActivity.this, PhotographerFormActivity.class));
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(MainActivity.this, "Error fetching user info: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });


        btnConsumer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ConsumerActivity.class);
                startActivity(intent);
            }
        });
    }
}
