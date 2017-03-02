package com.itechbd.locationdemo;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
           GoogleApiClient.OnConnectionFailedListener,LocationListener{

    public static final long UPDATE_INTERVAL_IN_MILLISECOND = 1000*1;
    public static final long UPDATE_MIN_DISTANCE_IN_METTER = 1;

    public static final int PERMISSION_ACCESS_COARSE_LOCATION = 3;
    public static final int PERMISSION_ACCESS_FINE_LOCATION = 4;

    public GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    public TextView showLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showLocation = (TextView) findViewById(R.id.showLocation);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECOND);
        mLocationRequest.setFastestInterval(UPDATE_INTERVAL_IN_MILLISECOND);
        mLocationRequest.setSmallestDisplacement(UPDATE_MIN_DISTANCE_IN_METTER);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_ACCESS_COARSE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_FINE_LOCATION);
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this,this,this).addApi(LocationServices.API).build();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       switch (requestCode){
           case  PERMISSION_ACCESS_COARSE_LOCATION:
               if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                   //All good
               }else {
                   Toast.makeText(this, "Need coarse location", Toast.LENGTH_LONG).show();
               }
               break;
           case  PERMISSION_ACCESS_FINE_LOCATION:
               if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                   //All good
               }else {
                   Toast.makeText(this, "Need fine location", Toast.LENGTH_LONG).show();
               }
               break;
       }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null){
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient != null){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("@LOCATION", connectionResult.getErrorMessage().toString());
    }

    @Override
    public void onLocationChanged(Location location) {

         if (location != null){
             showLocation.setText("Location : lat "+location.getLatitude()+" , long "+location.getLongitude());
         }
    }
}
