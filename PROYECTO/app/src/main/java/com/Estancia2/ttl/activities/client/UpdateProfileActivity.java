package com.Estancia2.ttl.activities.client;

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
import com.Estancia2.ttl.models.Client;

import com.Estancia2.ttl.providers.AuthProvider;
import com.Estancia2.ttl.providers.ClientProvider;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class UpdateProfileActivity extends AppCompatActivity {

    private Button mButtonUpdate;
    private TextView mTextViewName;
    private TextView mTextViewPhone;




    private ClientProvider mClientProvider;
    private AuthProvider mAuthProvider;


    private ProgressDialog mProgressDialog;
    private String mName;
    private String mPhone;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        MyToolbar.show(this,"Actualizar Perfil",true);

        mButtonUpdate=findViewById(R.id.btnUpdateProfile);
        mTextViewName=findViewById(R.id.textInputName);
        mTextViewPhone=findViewById(R.id.textInputPhone);


        mClientProvider=new ClientProvider();
        mAuthProvider=new AuthProvider();
        mProgressDialog = new ProgressDialog(this);

        getClientInfo();


        mButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });
    }


    private void getClientInfo() {
        mClientProvider.getClient(mAuthProvider.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    String phone=dataSnapshot.child("phone").getValue().toString();

                    mTextViewName.setText(name);
                    mTextViewPhone.setText(phone);
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


        if(!mName.equals("") && !mPhone.equals("") ){
            mProgressDialog.setMessage("Espere un Momento...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

            saveUser();

        }else{
            Toast.makeText(this, "Ingresa los campos correspondientes", Toast.LENGTH_SHORT).show();
        }

    }

    private void saveUser() {
        Client client=new Client();
        client.setName(mName);
        client.setPhone(mPhone);

        client.setId(mAuthProvider.getId());
        mClientProvider.update(client).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mProgressDialog.dismiss();
                Toast.makeText(UpdateProfileActivity.this, "Su informacion se actualizo correctamente", Toast.LENGTH_SHORT).show();
            }
        });
    }
}