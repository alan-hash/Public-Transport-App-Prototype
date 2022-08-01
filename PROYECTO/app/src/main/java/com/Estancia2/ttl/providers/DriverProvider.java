package com.Estancia2.ttl.providers;

import com.Estancia2.ttl.models.Client;
import com.Estancia2.ttl.models.Driver;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class DriverProvider {

    DatabaseReference mDatabase;

    public DriverProvider (){
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers");
    }
    public Task<Void> create(Driver driver){
        return mDatabase.child(driver.getId()).setValue(driver);

    }

    public Task<Void> update(Driver driver) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", driver.getName());
        map.put("phone", driver.getPhone());
        map.put("VehiclePlate", driver.getVehiclePlate());

        return mDatabase.child(driver.getId()).updateChildren(map);
    }


    public DatabaseReference getDriver(String idDriver) {
        return mDatabase.child(idDriver);
    }



}
