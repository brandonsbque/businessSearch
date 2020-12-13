package com.example.afinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class BusinessDetails extends AppCompatActivity {
    String IDvalue = "";
    TextView detailsTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_details);

        Intent intent = getIntent();
        IDvalue = intent.getStringExtra(BusinessActivity.transferID);

        detailsTitle = (TextView)findViewById(R.id.detailsTitle);
        detailsTitle.setText("ID: " + IDvalue);

    }
}