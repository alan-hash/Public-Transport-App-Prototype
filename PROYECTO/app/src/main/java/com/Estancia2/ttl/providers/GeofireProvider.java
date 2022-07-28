package com.Estancia2.ttl.providers;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GeofireProvider {

    private DatabaseReference mDatabase;
    private GeoFire mGeoFire;

    public GeofireProvider(String active_drivers){
        mDatabase= FirebaseDatabase.getInstance().getReference().child("active_drivers");
        mGeoFire=new GeoFire(mDatabase);
    }
    public void saveLocation(String idDriver, LatLng latlng){
        mGeoFire.setLocation(idDriver,new GeoLocation(latlng.latitude,latlng.longitude));

    }
    public void  removeLocation(String idDriver){
        mGeoFire.removeLocation(idDriver);

    }

    public GeoQuery getActivedDrivers(LatLng latLng, double radius) {
        GeoQuery geoQuery = mGeoFire.queryAtLocation(new GeoLocation(latLng.latitude, latLng.longitude), radius);
        geoQuery.removeAllListeners();
        return geoQuery;
    }


}
