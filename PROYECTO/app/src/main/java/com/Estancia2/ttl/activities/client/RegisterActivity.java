package com.Estancia2.ttl.activities.client;

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
import com.Estancia2.ttl.activities.driver.MapDriverActivity;
import com.Estancia2.ttl.activities.driver.RegisterDriverActivity;
import com.Estancia2.ttl.includes.MyToolbar;
import com.Estancia2.ttl.models.Client;
import com.Estancia2.ttl.providers.AuthProvider;
import com.Estancia2.ttl.providers.ClientProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import dmax.dialog.SpotsDialog;

public class RegisterActivity extends AppCompatActivity {
    AuthProvider mAuthProvider;
    ClientProvider mClientProvider;
    //views
    Button mButtonRegister;
    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputName;
    TextInputEditText mTextInputPhone;
    TextInputEditText mTextInputPassword;
    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        MyToolbar.show(this,"Registro de Usuario",true);

        mAuthProvider=new AuthProvider();
        mClientProvider= new ClientProvider();

        mDialog = new SpotsDialog.Builder().setContext(RegisterActivity.this).setMessage("Espere un momento").build();

        mButtonRegister=findViewById(R.id.btnRegister);
        mTextInputEmail=findViewById(R.id.textInputEmail);
        mTextInputName=findViewById(R.id.textInputName);
        mTextInputPhone=findViewById(R.id.textInputPhone);
        mTextInputPassword=findViewById(R.id.textInputPassword);


        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               clickRegister();
            }
        });
    }

    void clickRegister(){
        final String name=mTextInputName.getText().toString();
        final String phone=mTextInputPhone.getText().toString();
        final String email=mTextInputEmail.getText().toString();
        final String password=mTextInputPassword.getText().toString();

        if (!name.isEmpty()  && !phone.isEmpty() && !email.isEmpty() && !password.isEmpty()){
            if (password.length() >= 6){
                mDialog.show();
                register(name,phone,email,password);


            }
            else {
                Toast.makeText(this, "La contraseña debe de tener minimo 6 caracteres", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Ingresa todos los campos", Toast.LENGTH_SHORT).show();
        }
    }
    void register(final String name, String phone, String email,String password){
        mAuthProvider.register(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mDialog.hide();
                if (task.isSuccessful()){
                    String id=FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Client client=new Client(id,name,phone,email);
                    create(client);
                }
                else {
                    Toast.makeText(RegisterActivity.this,"Error al registrar usuario",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    void  create(Client client){
        mClientProvider.create(client).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent intent=new Intent(RegisterActivity.this, MapClientActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else {
                    Toast.makeText(RegisterActivity.this, "No se pudo registrar el cliente", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    /*
    void saveUser(String id,String name,String email){
        String selectedUser = mPref.getString("user", "");
        User user=new User();
        user.setEmail(email);
        user.setName(name);
        if (selectedUser.equals("driver")){
            mDatabase.child("Users").child("Drivers").child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                  if (task.isSuccessful()){
                      Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                  }
                  else{
                      Toast.makeText(RegisterActivity.this, "Fallo el registro", Toast.LENGTH_SHORT).show();
                  }
                }
            });
        }
        else if (selectedUser.equals("client")){
            mDatabase.child("Users").child("Clients").child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(RegisterActivity.this, "Fallo el registro", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

     */
}

