package com.example.asus.navipark_01;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class DirectionsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "DirectionsActivity";
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private Boolean mLocationPermissionGranted = false;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private LatLng latLng;
    private double xLat, xLng;
    private String index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);

        getSupportActionBar().setTitle("Directions");

        index = getIntent().getStringExtra("index");
        Log.d(TAG, "onCreate: index - "+index);

        getMarkerLatLng();
        getLocationPermission();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ImageButton btnDone = (ImageButton) findViewById(R.id.btnDone);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
                Intent intent = new Intent(DirectionsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getDeviceLocation();

        LatLng latLng = new LatLng(xLat, xLng);
        mMap.addMarker(new MarkerOptions().position(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    private void getDeviceLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Toast.makeText(this, "getDeviceLocation", Toast.LENGTH_SHORT).show();

        try  {
            if(mLocationPermissionGranted)  {
                final Task location = mFusedLocationClient.getLastLocation();

                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onCompleteListener : found location");
                            Location currentLocation = (Location) task.getResult();

                            currentLocation.getBearing();

                            latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f));
                        }
                        else {
                            Log.d(TAG, "onCompleteListener : current location is null");
                            Toast.makeText(DirectionsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        catch (SecurityException e)  {
            Log.e(TAG, "getDeviceLocation :  SecurityException : " + e.getMessage());
        }
    }

    private void getLocationPermission()  {
        Log.d(TAG, "getLocationPermission : getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)  {
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)  {
                mLocationPermissionGranted = true;
                //initMap();
            }
            else  {
                ActivityCompat.requestPermissions(this, permissions, 0001);
            }
        }
        else  {
            ActivityCompat.requestPermissions(this, permissions, 0001);
        }
    }

    private void getMarkerLatLng()  {
        Long xLatLong, xLngLong;

        xLatLong = getSharedPreferences("xLatLng", MODE_PRIVATE).getLong("xLat_"+index, -1);
        xLngLong = getSharedPreferences("xLatLng", MODE_PRIVATE).getLong("xLng_"+index, -1);

        Log.d(TAG, "getMarkerLatLng: ("+xLatLong+", "+xLngLong+")"+"        ["+index+"]");

        xLat = Double.longBitsToDouble(xLatLong);
        xLng = Double.longBitsToDouble(xLngLong);
    }
}
