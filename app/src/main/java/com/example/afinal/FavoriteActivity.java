package com.example.afinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FavoriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_favorite);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_recents:
                        Toast.makeText(FavoriteActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        Intent MainIntent = new Intent(FavoriteActivity.this, ProfileActivity.class);
                        startActivity(MainIntent);
                        break;
                    case R.id.action_favorites:
                        Toast.makeText(FavoriteActivity.this, "Business", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(FavoriteActivity.this, BusinessActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_nearby:
                        Toast.makeText(FavoriteActivity.this, "Map", Toast.LENGTH_SHORT).show();
                        Intent mapIntent = new Intent(FavoriteActivity.this, MapActivity.class);
                        startActivity(mapIntent);
                        break;
                    case R.id.action_favorite:
                        Toast.makeText(FavoriteActivity.this, "favorites", Toast.LENGTH_SHORT).show();
                        Intent favoritesIntent = new Intent(FavoriteActivity.this, FavoriteActivity.class);
                        startActivity(favoritesIntent);
                        break;
                }
                return true;
            }
        });
    }
}