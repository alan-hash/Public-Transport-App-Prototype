package com.Estancia2.ttl.activities.client;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.os.PowerManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.Estancia2.ttl.R;

import com.Estancia2.ttl.providers.AuthProvider;
import com.Estancia2.ttl.providers.ClientBookingProvider;
import com.Estancia2.ttl.providers.GeofireProvider;

import com.google.android.gms.maps.model.LatLng;


import java.util.HashMap;
import java.util.Map;




public class NotificationBookingActivity extends AppCompatActivity {

    private TextView mTextViewMin;
    private TextView mTextViewDistance;
    private TextView mTextViewCounter;
    private Button mButtonAccept;
    private String mExtraIdDriver;


    private String mExtraMin;
    private String mExtraDistance;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_booking);
        mTextViewMin = findViewById(R.id.textViewMin);
        mTextViewDistance = findViewById(R.id.textViewDistance);
        mButtonAccept = findViewById(R.id.btnAccept);


        mExtraMin = getIntent().getStringExtra("min");
        mExtraDistance = getIntent().getStringExtra("distance");


        mTextViewMin.setText(mExtraMin);
        mTextViewDistance.setText(mExtraDistance);

        mButtonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptBooking();
            }
        });



    }




    private void acceptBooking() {

        Intent intent = new Intent(NotificationBookingActivity.this, MapClientActivity.class);
        startActivity(intent);
    }

    }
