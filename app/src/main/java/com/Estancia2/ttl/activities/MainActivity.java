package com.Estancia2.ttl.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.Estancia2.ttl.R;
import com.Estancia2.ttl.activities.client.MapClientActivity;
import com.Estancia2.ttl.activities.driver.MapDriverActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Button mButtonIAmpasajero;
    Button mButtonIAmdriver;
    SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPref=getApplicationContext().getSharedPreferences("typeUser",MODE_PRIVATE);
        SharedPreferences.Editor editor =mPref.edit();


        mButtonIAmpasajero =findViewById(R.id.btnIAmpasajero);
        mButtonIAmdriver =findViewById(R.id.btnIAmdriver);


        mButtonIAmpasajero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("user","client");
                editor.apply();
                goToSelectAuth();
            }
        });
        mButtonIAmdriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("user","driver");
                editor.apply();
                goToSelectAuth();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() !=null){
            String user=mPref.getString("user","");
            if (user.equals("client")){
                Intent intent=new Intent(MainActivity.this, MapClientActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            else{
                Intent intent=new Intent(MainActivity.this, MapDriverActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

        }

    }



    private void goToSelectAuth() {
        Intent intent = new Intent(MainActivity.this, selectOptionAuthActivity.class);
        startActivity(intent);
    }


}