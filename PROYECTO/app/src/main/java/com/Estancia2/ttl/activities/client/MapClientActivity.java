package com.Estancia2.ttl.activities.client;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;

import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


import com.Estancia2.ttl.R;
import com.Estancia2.ttl.activities.MainActivity;
import com.Estancia2.ttl.includes.MyToolbar;
import com.Estancia2.ttl.providers.AuthProvider;
import com.Estancia2.ttl.providers.GeofireProvider;
import com.Estancia2.ttl.providers.GoogleApiProvider;
import com.Estancia2.ttl.utils.DecodePoints;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;

import com.google.firebase.database.DatabaseError;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapClientActivity extends AppCompatActivity implements OnMapReadyCallback {



    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    private AuthProvider mAuthProvider;

    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocation;
    private GeofireProvider mGeofireProvider;
    private final static int LOCATION_REQUEST_CODE = 1;
    private final static int SETTINGS_REQUEST_CODE = 2;

    private Marker mMarker;
    private  LatLng mCurrentLatlng;
    private Button mButtonRequestInfo;

    private List<Marker>mDriverMarker= new ArrayList<>();
    private boolean mIsFirstTime=true;
    private GoogleApiProvider mGoogleApiProvider;

    private LatLng mOriginLatLng;
    private LatLng mDestinationLatLng;
    private LatLng mOriginDriver;


    private List<LatLng> mPolylineList;
    private PolylineOptions mPolylineOptions;

    //Sensores
    private Vibrator vibrator;
    private SoundPool soundPool;
    private int[]sonidos=new int[1];



    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (getApplicationContext() != null) {
                    if (mMarker !=null){
                        mMarker.remove();
                    }
                    mCurrentLatlng=new LatLng(location.getLatitude(),location.getLongitude());

                    mMarker=mMap.addMarker(new MarkerOptions().position(
                            new LatLng(location.getLatitude(),location.getLongitude())
                            )
                                    .title("Tu Ubicacion")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.maps))


                    );


                    // OBTENER LA LOCALIZACION DEL USUARIO EN TIEMPO REAL
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                    .zoom(17f)
                                    .build()
                    ));
                    if (mIsFirstTime){
                        mIsFirstTime =false;
                        getActiveDrivers();
                    }
                }
            }
        }
    };





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_client);
        MyToolbar.show(this, "Pasajero", false);

        mAuthProvider = new AuthProvider();
        mGeofireProvider= new GeofireProvider("active_drivers");
        mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mButtonRequestInfo=findViewById(R.id.btnRequestInfo);
        mOriginLatLng=new LatLng( 19.815707283695566, -98.95651848788431);
        mDestinationLatLng=new LatLng(19.8248712062147,-98.97743782479284);
        mGoogleApiProvider=new GoogleApiProvider(MapClientActivity.this);
        mMapFragment.getMapAsync(this);

        //Sensores
        vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
        if(Build.VERSION.SDK_INT==Build.VERSION_CODES.LOLLIPOP){
            soundPool=new SoundPool.Builder().setMaxStreams(1).build();
        }else {
            soundPool=new SoundPool(1, AudioManager.STREAM_MUSIC,1);
        }

        //CARGAMOS LOS SONIDOS
        sonidos[0]=soundPool.load(this,R.raw.btninfo,1);


        //accion para mostra info
        mButtonRequestInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundPool.play(sonidos[0],1,1,1,0,1 );
               vibrator.vibrate(100);
                requestInfo();
                distance();
            }
        });


    }

    private void drawRoute() {
        mGoogleApiProvider.getDirections(mOriginLatLng, mDestinationLatLng).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {

                    JSONObject jsonObject = new JSONObject(response.body());
                    JSONArray jsonArray = jsonObject.getJSONArray("routes");
                    JSONObject route = jsonArray.getJSONObject(0);
                    JSONObject polylines = route.getJSONObject("overview_polyline");
                    String points = polylines.getString("points");
                    mPolylineList = DecodePoints.decodePoly(points);
                    mPolylineOptions = new PolylineOptions();
                    mPolylineOptions.color(Color.MAGENTA);
                    mPolylineOptions.width(16f);
                    mPolylineOptions.startCap(new SquareCap());
                    mPolylineOptions.jointType(JointType.ROUND);
                    mPolylineOptions.addAll(mPolylineList);
                    mMap.addPolyline(mPolylineOptions);

                } catch(Exception e) {
                    Log.d("Error", "Error encontrado " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }



    //accion para mostra info
    private void requestInfo() {
        Intent intent=new Intent(MapClientActivity.this,NotificationBookingActivity.class);
        startActivity(intent);

    }



    public void getActiveDrivers(){

        mGeofireProvider.getActivedDrivers(mCurrentLatlng,10).addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {


                //Añadir el marcador del chofer
                for (Marker marker:mDriverMarker){
                    if (marker.getTag() !=null){
                        if (marker.getTag().equals(key)){
                            return;
                        }
                    }
                }
                LatLng driverLatlng=new LatLng(location.latitude,location.longitude);
                mOriginDriver=driverLatlng;
                Marker marker=mMap.addMarker(new MarkerOptions().position(driverLatlng).title("Transporte Publico").icon(BitmapDescriptorFactory.fromResource(R.drawable.transport)));
                marker.setTag(key);
                mDriverMarker.add(marker);
            }

            @Override
            public void onKeyExited(String key) {
                for (Marker marker:mDriverMarker){
                    if (marker.getTag() !=null){
                        if (marker.getTag().equals(key)){
                            marker.remove();
                            mDriverMarker.remove(marker);
                            return;
                        }
                    }
                }

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                //Actualizar posicion del tranporte publico
                for (Marker marker:mDriverMarker){
                    if (marker.getTag() !=null){
                        if (marker.getTag().equals(key)){
                            marker.setPosition(new LatLng(location.latitude,location.longitude));

                        }
                    }
                }

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });

    }

    public void requestDriver(String distance,String min) {
        Intent intent = new Intent(getBaseContext(), NotificationBookingActivity.class);
        intent.putExtra("min", min);
        intent.putExtra("distance", distance);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    private void distance() {
        mGoogleApiProvider.getDirections(mOriginDriver, mCurrentLatlng).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    JSONArray jsonArray = jsonObject.getJSONArray("routes");
                    JSONObject route = jsonArray.getJSONObject(0);

                    JSONArray legs =  route.getJSONArray("legs");
                    JSONObject leg = legs.getJSONObject(0);
                    JSONObject distance = leg.getJSONObject("distance");
                    JSONObject duration = leg.getJSONObject("duration");
                    String distanceText = distance.getString("text");
                    String durationText = duration.getString("text");
                    requestDriver(distanceText,durationText);

                }

                catch(Exception e) {
                    Log.d("Error", "Error encontrado " + e.getMessage());
                }

            }


            @Override
            public void onFailure(Call<String> call, Throwable t) {


            }

        });


    }





    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(5);

        mMap.addMarker(new MarkerOptions().position(mOriginLatLng).title("Destino").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_pinblue)));
        mMap.addMarker(new MarkerOptions().position(mDestinationLatLng).title("ORIGEN").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_pinred)));


        startLocation();
        drawRoute();


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (gpsActived()) {
                        mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    } else {
                        showAlertDialogNOGPS();
                    }
                } else {
                    checkLocationPermissions();
                }
            } else {
                checkLocationPermissions();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_REQUEST_CODE && gpsActived()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }
        else  if (requestCode == SETTINGS_REQUEST_CODE && gpsActived()){
            showAlertDialogNOGPS();
        }
    }

    private void showAlertDialogNOGPS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Por favor activa tu ubicacion para continuar")
                .setPositiveButton("Configuraciones", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), SETTINGS_REQUEST_CODE);
                    }
                }).create().show();
    }

    private boolean gpsActived() {
        boolean isActive = false;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            isActive = true;
        }
        return isActive;
    }

    private void startLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (gpsActived()) {
                    mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                }
                else {
                    showAlertDialogNOGPS();
                }
            }
            else {
                checkLocationPermissions();
            }
        } else {
            if (gpsActived()) {
                mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            }
            else {
                showAlertDialogNOGPS();
            }
        }
    }

    private void checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Proporciona los permisos para continuar")
                        .setMessage("Esta aplicacion requiere de los permisos de ubicacion para poder utilizarse")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MapClientActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                            }
                        })
                        .create()
                        .show();
            }
            else {
                ActivityCompat.requestPermissions(MapClientActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.client_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logout();
        }
        if (item.getItemId() == R.id.action_update) {
            Intent intent=new Intent(MapClientActivity.this,UpdateProfileActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    void logout() {
        mAuthProvider.logout();
        Intent intent = new Intent(MapClientActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}