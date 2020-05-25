package com.example.asus.navipark_01;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "MapActivity";
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private Boolean mLocationPermissionGranted = false;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private LocationRequest mLocationRequest;
    private LatLng latLng;
    private LatLng mMarkerPos;
    private Marker mMarker;
    private ImageView mCurrLoc;
    private ImageButton btnNext;
    private SharedPreferences prefs;
    private int xCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        getSupportActionBar().setTitle("Map");

        mCurrLoc = (ImageView)findViewById(R.id.ic_currrentLocation);
        btnNext = (ImageButton)findViewById(R.id.btnNext);

        getLocationPermission();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);

        mCurrLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMarker.remove();
                getDeviceLocation();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {
                putLatLng();

                Intent intent = new Intent(MapActivity.this, SummaryActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Toast.makeText(this, "onMapReady", Toast.LENGTH_SHORT).show();
        mMap = googleMap;

        configLocationRequest();

        getDeviceLocation();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);  //show the blue dot
    }

    private void getDeviceLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //Toast.makeText(this, "getDeviceLocation", Toast.LENGTH_SHORT).show();

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

                            mMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).draggable(true));

                            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    return true;
                                }
                            });

                            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                                @Override
                                public void onMarkerDragStart(Marker marker) {

                                }

                                @Override
                                public void onMarkerDrag(Marker marker) {

                                }

                                @Override
                                public void onMarkerDragEnd(Marker marker) {
                                    setMarkerLatLng();
                                }
                            });

                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19f));
                        }
                        else {
                            Log.d(TAG, "onCompleteListener : current location is null");
                            //Toast.makeText(MapActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        catch (SecurityException e)  {
            Log.e(TAG, "getDeviceLocation :  SecurityException : " + e.getMessage());
        }

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Log.d(TAG, "getDeviceLocation: check");
//            Toast.makeText(this, "getDeviceLocation: check", Toast.LENGTH_SHORT).show();
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        final Task location = mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                // Got last known location. In some rare situations this can be null.
//                Log.d(TAG, "onSuccess: location == null");
//                Toast.makeText(MapActivity.this, "onSuccess: location == null", Toast.LENGTH_SHORT).show();
//                if (location != null) {
//                    // Logic to handle location object\
//                    Log.d(TAG, "onSuccess: location != null");
//                    Toast.makeText(MapActivity.this, "onSuccess: location != null", Toast.LENGTH_SHORT).show();
//                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//                }
//            }
//        });
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

    private void configLocationRequest()  {
        //Toast.makeText(this, "configLocationRequest", Toast.LENGTH_SHORT).show();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
    }

    private double[] setMarkerLatLng()  {
        double lat, lng;
        double[] arrLatLng = new double[2];

        mMarkerPos = mMarker.getPosition();

        lat = mMarkerPos.latitude;
        lng = mMarkerPos.longitude;

        arrLatLng[0] = lat;
        arrLatLng[1] = lng;

        Log.d(TAG, "setMarkerLatLng: lat - "+lat+" lng - "+lng);

        return arrLatLng;
    }

    private void putLatLng()  {
        double[] arrLatLng = setMarkerLatLng();

        Log.d(TAG, "putLatLng: ("+arrLatLng[0]+", "+arrLatLng[1]+") / ("+Double.doubleToRawLongBits(arrLatLng[0])+", "+Double.doubleToRawLongBits(arrLatLng[1])+")");

        prefs = getSharedPreferences("xLatLng", MODE_PRIVATE);
        prefs.edit().putLong("xLat", Double.doubleToRawLongBits(arrLatLng[0])).putLong("xLng", Double.doubleToRawLongBits(arrLatLng[1])).commit();
    }
}
