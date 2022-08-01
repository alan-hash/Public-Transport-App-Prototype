package com.Estancia2.ttl.activities.client;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Estancia2.ttl.R;
import com.Estancia2.ttl.includes.MyToolbar;
import com.Estancia2.ttl.models.Client;
import com.Estancia2.ttl.providers.AuthProvider;
import com.Estancia2.ttl.providers.ClientProvider;
import com.Estancia2.ttl.utils.CompressorBitmapImage;
import com.Estancia2.ttl.utils.FileUtil;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.File;

import id.zelory.compressor.Compressor;

public class UpdateProfileActivity extends AppCompatActivity {
    private ImageView mImageViewProfile;
    private Button mButtonUpdate;
    private TextView mTextViewName;
    private TextView mTextViewPhone;

    private ClientProvider mClientProvider;
    private AuthProvider mAuthProvider;

    private File  mImageFile;
    private String  mImage;
    private final  int GALLERY_REQUEST =1;
    private ProgressDialog mProgressDialog;
    private String mName;
    private String mPhone;


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
        mProgressDialog = new ProgressDialog(this);

        getClientInfo();

        mImageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        mButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });
    }



    private void openGallery() {
        Toast.makeText(this, "Ingresa los campos correspondientes", Toast.LENGTH_SHORT).show();

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST );
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GALLERY_REQUEST && resultCode==RESULT_OK){
            try {
                mImageFile = FileUtil.from(this, data.getData());
                mImageViewProfile.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));

            }catch (Exception e){
                Log.d("ERROR","Mensaje: "+e.getMessage());
            }

        }
    }

    private void getClientInfo() {
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
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void updateProfile() {
        mName=mTextViewName.getText().toString();
        mPhone=mTextViewPhone.getText().toString();
        if(!mName.equals("") && !mPhone.equals("") && mImageFile !=null){
            mProgressDialog.setMessage("Espere un Momento...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
            
            saveImage();

        }else{
            Toast.makeText(this, "Ingresa los campos correspondientes", Toast.LENGTH_SHORT).show();
        }

    }

    private void saveImage() {
        byte[] imageByte = CompressorBitmapImage.getImage(this, mImageFile.getPath(), 500, 500);
        final StorageReference storage = FirebaseStorage.getInstance().getReference().child("client_images").child(mAuthProvider.getId() + ".jpg");
        UploadTask  uploadTask=storage.putBytes(imageByte);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String image=uri.toString();
                            Client client=new Client();
                            client.setImage(image);
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
                    });

                }
                else
                {
                    Toast.makeText(UpdateProfileActivity.this, "Hubo un error al subir la imagen", Toast.LENGTH_SHORT).show();

                }

            }
        });


    }
}