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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

public class BusinessActivity extends AppCompatActivity {

    //ListView Stuff
    private String TAG = BusinessActivity.class.getSimpleName();
    private ListView lv;

    ArrayList<HashMap<String, String>> contactList;
    //End of ListView Stuff

    //USER INPUT STUFF
    EditText inputTerm, inputLocation;
    String userTerm="";
    String userLocation="";
    Button btnSearch, btnLocation;
    //end of user input stuff

    String IDvalue = "";
    String url = "";
    public static final String transferID = "com.example.afinal.IDvalue";

    GPSTracker gps;
    double currentLatitude, currentLongitude;

    TextView businessTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_favorites);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_recents:
                        Toast.makeText(BusinessActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        Intent MainIntent = new Intent(BusinessActivity.this, ProfileActivity.class);
                        startActivity(MainIntent);
                        break;
                    case R.id.action_favorites://i didnt realize this was named favorites, but im too deep to fix it now
                        Toast.makeText(BusinessActivity.this, "Business", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(BusinessActivity.this, BusinessActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_favorite:
                        Toast.makeText(BusinessActivity.this, "favorites", Toast.LENGTH_SHORT).show();
                        Intent favoritesIntent = new Intent(BusinessActivity.this, FavoriteActivity.class);
                        startActivity(favoritesIntent);
                        break;
                }
                return true;
            }
        });

        //user input stuff
        inputTerm = (EditText)findViewById(R.id.inputTerm);
        inputLocation = (EditText)findViewById(R.id.inputLocation);
        btnSearch = (Button)findViewById(R.id.btnSearch);
        btnLocation = (Button)findViewById(R.id.btnLocation);
        businessTitle = (TextView)findViewById((R.id.businessTitle));
        //end of user input stuff

        //ListView stuff
        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);



        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new GetResults().execute();

            }
        });

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create class object
                gps = new GPSTracker(BusinessActivity.this);

                // check if GPS enabled
                if(gps.canGetLocation()){

                    currentLatitude = gps.getLatitude();
                    currentLongitude = gps.getLongitude();

                    // \n is for new line
                    //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
                            //+ currentLatitude + "\nLong: " + currentLongitude, Toast.LENGTH_LONG).show();

                    new GetResults().execute();
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }


            }
        });
        //End of listView stuff



    }
//end of onCreate

    public void openBusinessDetails(){
        Intent intent = new Intent(this, BusinessDetails.class);
        //intent.putExtra(transferLat, latitudeValue);
        //intent.putExtra(transferLon, longitudeValue);
        startActivity(intent);
    }

    //ListView Stuff
    private class GetResults extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(BusinessActivity.this,"Fetching results",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            userTerm = inputTerm.getText().toString();
            userLocation = inputLocation.getText().toString();
            if(userLocation.matches("")) {
                url = "https://api.yelp.com/v3/businesses/search?term=" + userTerm + "&latitude=" + currentLatitude + "&longitude=" + currentLongitude;
            }else{
                url = "https://api.yelp.com/v3/businesses/search?term=" + userTerm + "&location=" + userLocation;
            }
            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray businesses = jsonObj.getJSONArray("businesses");

                    // looping through All Contacts
                    for (int i = 0; i < businesses.length(); i++) {
                        JSONObject b = businesses.getJSONObject(i);
                        String id = b.getString("id");
                        String name = b.getString("name");

                        // Phone node is JSON Object
                        JSONObject location = b.getJSONObject("location");
                        String address1 = location.getString("address1");
                        String city = location.getString("city");
                        String state = location.getString("state");
                        String zip_code = location.getString("zip_code");
                        String completeAddress = "" + address1 + ", " + city + ", " + state + " " + zip_code;

                        // tmp hash map for single contact
                        HashMap<String, String> businessData = new HashMap<>();

                        // adding each child node to HashMap key => value
                        businessData.put("id", id);
                        businessData.put("name", name);
                        businessData.put("completeAddress", completeAddress);

                        // adding contact to contact list
                        contactList.add(businessData);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            final ListAdapter adapter = new SimpleAdapter(BusinessActivity.this, contactList,
                    R.layout.list_item, new String[]{ "name","completeAddress"},
                    new int[]{R.id.name, R.id.completeAddress});
            lv.setAdapter(adapter);

            //start of business id transfer
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(BusinessActivity.this, "Business: "+adapter.getItem(position), Toast.LENGTH_SHORT).show();
                    HashMap<String, String> specificBusiness = contactList.get(position);
                    String theID = specificBusiness.get("id");
                    IDvalue = String.valueOf(theID);
                    Intent intent = new Intent(BusinessActivity.this, BusinessDetails.class);
                    Log.v("testing", theID);
                    intent.putExtra(transferID, IDvalue);
                    startActivity(intent);
                }
            });
            //end of business id transfer
        }
    }
    //End of ListView Stuff
}