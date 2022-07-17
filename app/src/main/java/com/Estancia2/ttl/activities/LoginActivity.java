package com.Estancia2.ttl.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.Estancia2.ttl.R;
import com.Estancia2.ttl.activities.client.MapClientActivity;
import com.Estancia2.ttl.activities.driver.MapDriverActivity;
import com.Estancia2.ttl.activities.driver.RegisterDriverActivity;
import com.Estancia2.ttl.includes.MyToolbar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputPassword;
    Button mButtonLogin;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    AlertDialog mDialog;
    SharedPreferences mPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MyToolbar.show(this,"Login de Usuario",true);
        mTextInputEmail= findViewById(R.id.textInputEmail);//error/
        mTextInputPassword= findViewById(R.id.textInputPassword);
        mButtonLogin= findViewById(R.id.btnLogin);
        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mDialog = new SpotsDialog.Builder().setContext(LoginActivity.this).setMessage("Espere un momento").build();
        mPref = getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);
        mButtonLogin.setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }
    private void login(){
        String email=mTextInputEmail.getText().toString();
        String password=mTextInputPassword.getText().toString();
        if(!email.isEmpty() && !password.isEmpty()){
          if (password.length() >= 6){
              mDialog.show();
              mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                  if(task.isSuccessful()){
                      String user=mPref.getString("user","");
                      if (user.equals("client")){
                          Intent intent=new Intent(LoginActivity.this, MapClientActivity.class);
                          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                          startActivity(intent);
                      }
                      else{
                          Intent intent=new Intent(LoginActivity.this, MapDriverActivity.class);
                          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                          startActivity(intent);
                      }
                  }
                  else {
                      Toast.makeText(LoginActivity.this, "El Correo o Contraseña son invalidos", Toast.LENGTH_SHORT).show();
                  }
                  mDialog.dismiss();
                  }
              });
          }
          else{
              Toast.makeText(LoginActivity.this, "La contraseña debe de ser mayor a 6 caracteres", Toast.LENGTH_SHORT).show();
          }
        }
        else{
            Toast.makeText(LoginActivity.this, "El Correo y la contraseña son obligatorios", Toast.LENGTH_SHORT).show();

        }
    }
}