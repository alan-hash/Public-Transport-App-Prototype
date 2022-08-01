package com.Estancia2.ttl.activities.driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import com.Estancia2.ttl.R;
import com.Estancia2.ttl.includes.MyToolbar;
import com.Estancia2.ttl.models.Driver;
import com.Estancia2.ttl.providers.AuthProvider;
import com.Estancia2.ttl.providers.DriverProvider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;



public class UpdateProfileDriverActivity extends AppCompatActivity {
    private Button mButtonUpdate;
    private TextView mTextViewName;
    private TextView mTextViewPhone;
    private TextView mTextViewVehiclePlate;



    private DriverProvider mDriverProvider;
    private AuthProvider mAuthProvider;


    private ProgressDialog mProgressDialog;
    private String mName;
    private String mPhone;
    private String mVehiclePlate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_driver);
        MyToolbar.show(this,"Actualizar Perfil",true);

        mButtonUpdate=findViewById(R.id.btnUpdateProfile);
        mTextViewName=findViewById(R.id.textInputName);
        mTextViewPhone=findViewById(R.id.textInputPhone);
        mTextViewVehiclePlate=findViewById(R.id.textInputVehiclePlate);

        mDriverProvider=new DriverProvider();
        mAuthProvider=new AuthProvider();
        mProgressDialog = new ProgressDialog(this);

        getDriverInfo();


        mButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });
    }



    private void getDriverInfo() {
        mDriverProvider.getDriver(mAuthProvider.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    String phone= dataSnapshot.child("phone").getValue().toString();
                    String vehiclePlate = dataSnapshot.child("vehiclePlate").getValue().toString();


                    mTextViewName.setText(name);
                    mTextViewPhone.setText(phone);
                    mTextViewVehiclePlate.setText(vehiclePlate);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void updateProfile() {
        mName=mTextViewName.getText().toString();
        mPhone=mTextViewPhone.getText().toString();
        mVehiclePlate=mTextViewVehiclePlate.getText().toString();

        if(!mName.equals("") && !mPhone.equals("") && !mVehiclePlate.equals("")){
            mProgressDialog.setMessage("Espere un Momento...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

            saveUser();

        }else{
            Toast.makeText(this, "Ingresa los campos correspondientes", Toast.LENGTH_SHORT).show();
        }

    }

    private void saveUser() {
        Driver driver=new Driver();
        driver.setName(mName);
        driver.setPhone(mPhone);
        driver.setVehiclePlate(mVehiclePlate);
        driver.setId(mAuthProvider.getId());
        mDriverProvider.update(driver).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mProgressDialog.dismiss();
                Toast.makeText(UpdateProfileDriverActivity.this, "Su informacion se actualizo correctamente", Toast.LENGTH_SHORT).show();
            }
        });
    }
}