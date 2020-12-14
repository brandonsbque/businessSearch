package com.example.afinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

    ArrayList<HashMap<String, String>> contactList2;

    String favIDvalue = "";
    String userID = "";
    public static final String transferFaveID = "com.example.afinal.favIDvalue";
    public static final String transferUserID = "com.example.afinal.userID";

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
                        //Toast.makeText(FavoriteActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        Intent MainIntent = new Intent(FavoriteActivity.this, ProfileActivity.class);
                        startActivity(MainIntent);
                        break;
                    case R.id.action_favorites://i didnt realize this was named favorites, but im too deep to fix it now
                        //Toast.makeText(FavoriteActivity.this, "Business", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(FavoriteActivity.this, BusinessActivity.class);
                        intent.putExtra(transferUserID, userID);
                        startActivity(intent);
                        break;
                    case R.id.action_favorite:
                        //Toast.makeText(FavoriteActivity.this, "favorites", Toast.LENGTH_SHORT).show();
                        Intent favoritesIntent = new Intent(FavoriteActivity.this, FavoriteActivity.class);
                        startActivity(favoritesIntent);
                        break;
                }
                return true;
            }
        });

        favoriteTitle = (TextView)findViewById(R.id.favoriteTitle);

        Intent intent = getIntent();
        userID = intent.getStringExtra(ProfileActivity.transferUserEmail); //go add if user navigates from business tab
        if(userID == null){
            userID = intent.getStringExtra(BusinessActivity.transfertheID); //go add if user navigates from business tab
        }

        favoriteTitle.setText("user: " + userID);

        contactList2 = new ArrayList<>();
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
                                    contactList2.add(theList);
                                }
                                final ListAdapter adapter = new SimpleAdapter(FavoriteActivity.this, contactList2,
                                        R.layout.list_item, new String[]{"name", "address"},
                                        new int[]{R.id.name, R.id.completeAddress});
                                lv.setAdapter(adapter);

                                //start of business id transfer
                                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        //Toast.makeText(FavoriteActivity.this, "Business: "+adapter.getItem(position), Toast.LENGTH_SHORT).show();
                                        HashMap<String, String> specificBusiness = contactList2.get(position);
                                        String theID = specificBusiness.get("businessId");
                                        favIDvalue = String.valueOf(theID);
                                        Intent intent = new Intent(FavoriteActivity.this, BusinessDetails.class);
                                        //Log.v("testing", theID);
                                        intent.putExtra(transferFaveID, favIDvalue);
                                        startActivity(intent);
                                    }
                                });
                                //end of business id transfer

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