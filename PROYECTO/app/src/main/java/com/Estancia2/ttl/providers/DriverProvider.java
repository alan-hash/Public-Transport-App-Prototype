package com.Estancia2.ttl.providers;

import com.Estancia2.ttl.models.Client;
import com.Estancia2.ttl.models.Driver;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverProvider {

    DatabaseReference mDatabase;

    public DriverProvider (){
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers");
    }
    public Task<Void> create(Driver driver){
        return mDatabase.child(driver.getId()).setValue(driver);

    }

}
