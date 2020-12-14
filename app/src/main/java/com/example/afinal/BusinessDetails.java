package com.example.afinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class BusinessDetails extends AppCompatActivity {
    String IDvalue = "";
    String latValue = "";
    String lonValue = "";
    String Baddress = "";
    String Bname = "";
    TextView detailsTitle, businessName, businessRating, businessAddress, businessPhone, businessCategory, businessLatitude, businessLongitude;
    Button btnMapView, btnFavorite;
    String ACCESS_TOKEN = "TfF146dfseHj6iOkzv8q0Ks0C49MArBejc00zUuLLkirTXtnsB2oKquz23eHygukkQ62FSG93m50e5P7zNwuq_KsAX_MQkeJE0nCRHLAaio86Wh_VvUsx1nP1l_QX3Yx";

    public static final String transferLat = "com.example.afinal.latValue";
    public static final String transferLon = "com.example.afinal.lonValue";

    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_details);

        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will
                //execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = getIntent();
        IDvalue = intent.getStringExtra(BusinessActivity.transferID);
        if(IDvalue == null){
        IDvalue = intent.getStringExtra(FavoriteActivity.transferFaveID);}

        detailsTitle = (TextView)findViewById(R.id.detailsTitle);
        businessName = (TextView)findViewById(R.id.businessName);
        businessRating = (TextView)findViewById(R.id.businessRating);
        businessAddress = (TextView)findViewById(R.id.businessAddress);
        businessPhone = (TextView)findViewById(R.id.businessPhone);
        businessCategory = (TextView)findViewById(R.id.businessCategory);
        businessLatitude = (TextView)findViewById(R.id.businessLatitude);
        businessLongitude = (TextView)findViewById(R.id.businessLongitude);
        btnMapView = (Button)findViewById(R.id.btnMapView);
        btnFavorite = (Button)findViewById(R.id.btnFavorite);

        String userID = intent.getStringExtra(BusinessActivity.transfertheID);
        if(userID == null){
            userID = intent.getStringExtra(FavoriteActivity.transferFaveID);
        }
        detailsTitle.setText("ID: " + userID);

        //detailsTitle.setText("ID: " + IDvalue); only used this line for testing
        String YelpURL = "https://api.yelp.com/v3/businesses/" + IDvalue;
        getData(YelpURL);

        btnMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapActivity();
                Toast.makeText(getApplicationContext(), "Brandon Michael Que\nZ23479912", Toast.LENGTH_LONG).show();
            }
        });

        final DocumentReference docRef = FirebaseFirestore.getInstance().collection(userID).document(IDvalue);

        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, Object> document = new HashMap<>();
                document.put("Business Name", Bname);
                document.put("Address", Baddress);
                document.put("Business Id", IDvalue);
                //If saving to document is a success
                docRef.set(document).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("RESULT", "Document has been saved");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("RESULT", "Document has not been saved");
                    }
                });
                Toast.makeText(getApplicationContext(), "Business Saved to Favorites", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void openMapActivity(){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra(transferLat, latValue);
        intent.putExtra(transferLon, lonValue);
        startActivity(intent);
    }

    public void getData(String YelpURL){
        JsonObjectRequest getData = new JsonObjectRequest(Request.Method.GET, YelpURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject coordObject = response.getJSONObject("coordinates");
                    JSONArray arrayCategory = response.getJSONArray("categories");
                    JSONObject object = arrayCategory.getJSONObject(0);
                    JSONObject mainObject = response.getJSONObject("location");

                    String address1 = String.valueOf(mainObject.getString("address1"));
                    String addressCity = String.valueOf(mainObject.getString("city"));
                    String addressState = String.valueOf(mainObject.getString("state"));
                    String addressZip = String.valueOf(mainObject.getString("zip_code"));

                    Bname = response.getString("name");
                    String Bphone = response.getString("display_phone");
                    latValue = String.valueOf(coordObject.getDouble("latitude"));
                    lonValue = String.valueOf(coordObject.getDouble("longitude"));
                    String Bcategory = object.getString("title");
                    String Brating = response.getString("rating");
                    Baddress = address1 + ", " + addressCity + ", " + addressState + " " + addressZip;

                    businessName.setText(Bname);
                    businessRating.setText("Rating: " + Brating + " / 5.0");
                    businessAddress.setText("Address: " + Baddress);
                    businessPhone.setText("Phone: " + Bphone);
                    businessCategory.setText("Category: " + Bcategory);
                    businessLatitude.setText("Latitude: " + latValue);
                    businessLongitude.setText("Longitude: " + lonValue);

                } catch(JSONException theError){
                    theError.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){

            }
        }){
            //This is for Headers If You Needed
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Authorization","bearer " + ACCESS_TOKEN);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(getData);

    }

}