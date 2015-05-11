package com.geoalgorithm.googlelocationtestapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alan Poggetti on 05/05/2015.
 */
public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks, LocationListener, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    @Override
    public IBinder onBind(Intent intent) {


        return null;
    }

    @Override
    public void onCreate() {
        // code to execute when the service is first created
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startid) {
        buildGoogleApiClient();

        mGoogleApiClient.connect();

        return START_STICKY;

    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.e("Google API", "Connection failed... Error: "+connectionResult);

    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnectionSuspended(int i) {

        Log.d("Google API", "Suspended...");

    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1*60*1000);
        mLocationRequest.setSmallestDisplacement(15);
        mLocationRequest.setFastestInterval(1 * 60 * 1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
    }

    @Override
    public void onConnected(Bundle connectionHint) {

        Log.d("Google API", "Connected...");

        startLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {

        if(location.getAccuracy()<100) {

            List<Location> mLocations = new ArrayList<>();

            SharedPreferences prefs = getSharedPreferences("GooglePlayMapsTest", Context.MODE_PRIVATE);
            String locationJson = prefs.getString("locationJson", "");

            if (!locationJson.equals("")) {

                Gson gson = new Gson();
                mLocations = gson.fromJson(locationJson, new TypeToken<ArrayList<Location>>() {
                }.getType());

            }

            mLocations.add(location);

            Gson gson = new GsonBuilder().create();

            locationJson = gson.toJson(mLocations, new TypeToken<List<Location>>() {
            }.getType());

            SharedPreferences.Editor editor = getSharedPreferences("GooglePlayMapsTest", Context.MODE_PRIVATE).edit();

            editor.putString("locationJson", locationJson).commit();

            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(MapsActivity.NEW_LOCATION_RECEIVED));

        }
    }

    protected void startLocationUpdates() {

        if(mLocationRequest == null)
            createLocationRequest();

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

}
