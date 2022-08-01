package com.Estancia2.ttl.activities.driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.Estancia2.ttl.R;
import com.Estancia2.ttl.activities.MainActivity;
import com.Estancia2.ttl.activities.client.RegisterActivity;
import com.Estancia2.ttl.includes.MyToolbar;
import com.Estancia2.ttl.models.Client;
import com.Estancia2.ttl.models.Driver;
import com.Estancia2.ttl.providers.AuthProvider;
import com.Estancia2.ttl.providers.ClientProvider;
import com.Estancia2.ttl.providers.DriverProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import dmax.dialog.SpotsDialog;

public class RegisterDriverActivity extends AppCompatActivity {

    AuthProvider mAuthProvider;
    DriverProvider mDriverProvider;
    //views
    Button mButtonRegister;
    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputName;
    TextInputEditText mTextInputPhone;
    TextInputEditText getmTextInputVehiclePlate;
    TextInputEditText mTextInputPassword;
    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_driver);
        MyToolbar.show(this,"Registro de Conductor",true);

        mAuthProvider=new AuthProvider();
        mDriverProvider= new DriverProvider();

        mDialog = new SpotsDialog.Builder().setContext(RegisterDriverActivity.this).setMessage("Espere un momento").build();

        mButtonRegister=findViewById(R.id.btnRegister);
        mTextInputEmail=findViewById(R.id.textInputEmail);
        mTextInputName=findViewById(R.id.textInputName);
        mTextInputPhone=findViewById(R.id.textInputPhone);
        getmTextInputVehiclePlate=findViewById(R.id.textInputVehiclePlate);
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
        final String vehiclePlate=getmTextInputVehiclePlate.getText().toString();
        final String password=mTextInputPassword.getText().toString();

        if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !phone.isEmpty() && !vehiclePlate.isEmpty()){
            if (password.length() >= 6){
                mDialog.show();
                register(name,phone,email,password,vehiclePlate);
            }
            else {
                Toast.makeText(this, "La contraseña debe de tener minimo 6 caracteres", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Ingresa todos los campos", Toast.LENGTH_SHORT).show();
        }
    }
    void register(final String name,String vehicleName,String email,String password,String vehiclePlate){
        mAuthProvider.register(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mDialog.hide();
                if (task.isSuccessful()){
                    String id= FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Driver driver=new Driver(id,name,vehicleName,email,vehiclePlate);
                    create(driver);
                }
                else {
                    Toast.makeText(RegisterDriverActivity.this,"Error al registrar usuario",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void  create(Driver driver){
        mDriverProvider.create(driver).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //Toast.makeText(RegisterDriverActivity.this, "el registro fue exitoso", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(RegisterDriverActivity.this, MapDriverActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else {
                    Toast.makeText(RegisterDriverActivity.this, "No se pudo registrar el cliente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}