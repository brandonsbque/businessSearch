package com.example.afinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FavoriteActivity extends AppCompatActivity {

    private String TAG = FavoriteActivity.class.getSimpleName();
    private ListView lv;
    TextView favoriteTitle;

    ArrayList<HashMap<String, String>> contactList;

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
                    case R.id.action_favorites://i didnt realize this was named favorites, but im too deep to fix it now
                        Toast.makeText(FavoriteActivity.this, "Business", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(FavoriteActivity.this, BusinessActivity.class);
                        startActivity(intent);
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

        favoriteTitle = (TextView)findViewById(R.id.favoriteTitle);

        Intent intent = getIntent();
        String userID = intent.getStringExtra(ProfileActivity.transferUserEmail);
        favoriteTitle.setText("user: " + userID);

        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list2);

        Task<QuerySnapshot> query = FirebaseFirestore.getInstance().collection(userID).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int count = 0;
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    String name = document.getString("Business Name");
                                    String businessId = document.getString("Business Id");
                                    String address = document.getString("Address");


                                    //Could also do Map<String, Object> myData = documentSnapshot.getData();
                                    HashMap<String, String> theList = new HashMap<>();
                                    theList.put("name", name);
                                    theList.put("businessId", businessId);
                                    theList.put("address", address);
                                    contactList.add(theList);
                                }
                                ListAdapter adapter = new SimpleAdapter(FavoriteActivity.this, contactList,
                                        R.layout.list_item, new String[]{"name", "address"},
                                        new int[]{R.id.name, R.id.completeAddress});
                                lv.setAdapter(adapter);

                            }
                        } else {
                            Log.d("favoriteResult", "Error getting documents: ", task.getException());
                        }
                        Log.d("favoriteResult", String.valueOf(count));
                    }
                });

        //new GetContacts().execute();

    }



}