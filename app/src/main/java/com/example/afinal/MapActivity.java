package com.example.afinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_recents:
                        Toast.makeText(MapActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        Intent MainIntent = new Intent(MapActivity.this, ProfileActivity.class);
                        startActivity(MainIntent);
                        break;
                    case R.id.action_favorites:
                        Toast.makeText(MapActivity.this, "Business", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MapActivity.this, BusinessActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_nearby:
                        Toast.makeText(MapActivity.this, "Map", Toast.LENGTH_SHORT).show();
                        Intent mapIntent = new Intent(MapActivity.this, MapActivity.class);
                        startActivity(mapIntent);
                        break;
                }
                return true;
            }
        });
    }
}