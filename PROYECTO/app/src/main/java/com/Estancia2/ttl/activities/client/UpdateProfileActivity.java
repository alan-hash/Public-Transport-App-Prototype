package com.Estancia2.ttl.activities.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.Estancia2.ttl.R;
import com.Estancia2.ttl.includes.MyToolbar;
import com.Estancia2.ttl.providers.AuthProvider;
import com.Estancia2.ttl.providers.ClientProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class UpdateProfileActivity extends AppCompatActivity {
    private ImageView mImageViewProfile;
    private Button mButtonUpdate;
    private TextView mTextViewName;
    private TextView mTextViewPhone;

    private ClientProvider mClientProvider;
    private AuthProvider mAuthProvider;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        MyToolbar.show(this,"Actualizar Perfil",true);


        mImageViewProfile=findViewById(R.id.imageViewProfile);
        mButtonUpdate=findViewById(R.id.btnUpdateProfile);
        mTextViewName=findViewById(R.id.textInputName);
        mTextViewPhone=findViewById(R.id.textInputPhone);

        mClientProvider=new ClientProvider();
        mAuthProvider=new AuthProvider();

        getClientInfo();

        mButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();

            }
        });




    }

    private void getClientInfo(){
        mClientProvider.getClient(mAuthProvider.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    mTextViewName.setText(name);

                    String phone = dataSnapshot.child("phone").getValue().toString();
                    mTextViewPhone.setText(phone);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void updateProfile() {
    }
}