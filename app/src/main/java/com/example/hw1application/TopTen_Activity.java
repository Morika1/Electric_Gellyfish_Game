package com.example.hw1application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class TopTen_Activity extends AppCompatActivity {

    private Fragment_List fragment_list;
    private Fragment_Map fragment_map;
    private  Score newScore = null;
    private int score =0;
    FusedLocationProviderClient fusedLocationProviderClient;
    private ExtendedFloatingActionButton topten_BTN_back;


    CallBack_UserProtocol callBack_userProtocol = new CallBack_UserProtocol() {
        @Override
        public void user(Score score) {
            showUserLocation(score);
        }

    };


    CallBack_viewReadyProtocol callBack_viewReadyProtocol = new CallBack_viewReadyProtocol() {
        @Override
        public void continueWork() {
            //continueWork();
            fragment_list.handleScore(score);

        }
    };

    private LocationCallback locationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location lastLocation = locationResult.getLastLocation();
            newScore.setLocation(new double[]{lastLocation.getLatitude(), lastLocation.getLongitude()});
        }
    };

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ten);

        findViews();
        initViews();

        fragment_list = new Fragment_List();
        fragment_map = new Fragment_Map();
        Intent prevIntent = getIntent();
        score = prevIntent.getExtras().getInt("score");

        fragment_list.setCallBack_userProtocol(callBack_userProtocol);
        fragment_list.setCallBack_viewReadyProtocol(callBack_viewReadyProtocol);

        if(newScore != null){
            fusedLocationProviderClient = LocationServices.
                    getFusedLocationProviderClient(this);

            fetchLastLocation();
            fragment_map.updateLocations(fragment_list.getTopTen().getTopScores());
        }

        getSupportFragmentManager()
                .beginTransaction().replace(R.id.topoten_LAY_mapoftopten, fragment_map)
                .commit();
        getSupportFragmentManager().beginTransaction().add(R.id.topoten_LAY_listoftopten, fragment_list).commit();



    }

    private void initViews() {
        topten_BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked();
            }
        });
    }

    private void clicked() {
        Intent intent = new Intent(this, OpenPage_Activity.class);
        startActivity(intent);
        finish();
    }

    private void findViews() {
        topten_BTN_back= findViewById(R.id.topten_BTN_back);
    }

    @SuppressLint("MissingPermission")
    public void fetchLastLocation() {

        if (ActivityCompat.
                checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 123);

            return;

        } else {

            if (isLocationEnabled()) {

                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            newScore.setLocation(new double[]{location.getLatitude(), location.getLongitude()});
                        }
                    }
                });

            } else { // location is not enabled
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        }
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(500)
                .setMaxUpdateDelayMillis(1000)
                .build();

        // setting LocationRequest
        // on FusedLocationClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper());
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }



    private void showUserLocation(Score score) {
        fragment_map.zoom(score.getLocation()[0], score.getLocation()[1]);
    }

}



