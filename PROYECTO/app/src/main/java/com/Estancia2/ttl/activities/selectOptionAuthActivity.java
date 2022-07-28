package com.Estancia2.ttl.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.Estancia2.ttl.R;
import com.Estancia2.ttl.activities.client.RegisterActivity;
import com.Estancia2.ttl.activities.driver.RegisterDriverActivity;
import com.Estancia2.ttl.includes.MyToolbar;

public class selectOptionAuthActivity extends AppCompatActivity {

    Button mButtonGoToLogin;
    Button mButtonGoToRegister;
    SharedPreferences mPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_option_auth);
      MyToolbar.show(this, "Seleccionar opcion", true);

        mButtonGoToLogin    = findViewById(R.id.btnGoToLogin);
        mButtonGoToRegister = findViewById(R.id.btnGoToRegister);
        mButtonGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLogin();
            }
        });
        mButtonGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegister();
            }
        });

        mPref = getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);

    }

    public void goToLogin() {
        Intent intent = new Intent(selectOptionAuthActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void goToRegister() {
        String typeUser = mPref.getString("user", "");
        if (typeUser.equals("client")) {
            Intent intent = new Intent(selectOptionAuthActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(selectOptionAuthActivity.this, RegisterDriverActivity.class);
            startActivity(intent);
        }
    }
}