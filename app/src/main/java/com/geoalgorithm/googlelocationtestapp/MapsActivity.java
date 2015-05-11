package com.geoalgorithm.googlelocationtestapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private List<Location> mLocations;
    private List<Marker> mMarkers = new ArrayList<>();
    private Marker lastMarker;

    public static final String NEW_LOCATION_RECEIVED = "new_location_received";

    private List<String> mLocationTitles = new ArrayList<>();
    private ListView mDrawerList;
    private MyArrayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        adapter = new MyArrayAdapter(this, mLocationTitles,
                R.layout.list_item);
        mDrawerList.setAdapter(adapter);
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        setUpMapIfNeeded();

        //We start the location service
        startService(new Intent(this, LocationService.class));

    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps between markers */
    private void selectItem(int position) {

        mDrawerList.setItemChecked(position, true);
        moveCamera(mLocations.get(position));

        if(lastMarker != null)
            lastMarker.hideInfoWindow();

        if(adapter != null) {
            adapter.setSelectedRow(position);
            adapter.notifyDataSetChanged();
        }

        lastMarker = mMarkers.get(position);

        lastMarker.showInfoWindow();
    }

    @Override
    protected void onResume() {

        super.onResume();
        setUpMapIfNeeded();

        //We register our location receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocationReceiver, new IntentFilter(NEW_LOCATION_RECEIVED));

    }

    /**
     * Let's not forget to unregister our Receiver and stop location updates
     */
    @Override
    protected void onStop(){

        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mLocationReceiver);

    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated..
     *
     * Also, we initialize the Library with our provided API Key
     */

    private void setUpMapIfNeeded() {

        if (mMap == null) {

            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();

            if(mLocations == null && mMap != null) {

                loadMarkersOnList(true,false);

            }

        }

    }

    private void loadMarkersOnList(boolean loadMarkersOnMap, boolean loadLastMarker){

        SharedPreferences prefs = getSharedPreferences("GooglePlayMapsTest", Context.MODE_PRIVATE);
        String locationJson = prefs.getString("locationJson", "");

        if(!locationJson.equals("")){

            Gson gson = new Gson();
            mLocations = gson.fromJson(locationJson,new TypeToken<ArrayList<Location>>(){}.getType());

            Collections.reverse(mLocations);

        }
        if(mLocations == null){
            mLocations = new ArrayList<>();
        }

        if(mLocationTitles == null)
            mLocationTitles = new ArrayList<>();
        else
            mLocationTitles.clear();

        for(Location location : mLocations) {

            String date = Utils.dateFormatter(new Date(location.getTime()));

            if(loadMarkersOnMap) {

                addMarker(location,false);

            }
            mLocationTitles.add(date + ", Accuracy = " + location.getAccuracy() + " m");

        }

        if(loadLastMarker && mLocations.size()>0){
            addMarker(mLocations.get(0),true);
        }

        if(mLocations.size()>0)
         selectItem(0);

       adapter = new MyArrayAdapter(this,mLocationTitles,
                R.layout.list_item);

        mDrawerList.setAdapter(adapter);

    }






    /**
     * We configure our BroadcastReceiver in order to store our new location and place a marker on our map
     */
    private final BroadcastReceiver mLocationReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(NEW_LOCATION_RECEIVED)){

                //We make sure that we have a valid location
                if(mMap != null){

                    SharedPreferences prefs = getSharedPreferences("GooglePlayMapsTest", Context.MODE_PRIVATE);
                    String locationJson = prefs.getString("locationJson", "");

                    if(!locationJson.equals("")){

                        loadMarkersOnList(false, true);

                    }

                }

            }

        }

    };

    /**
     * Simple method that will move the camera to a location in our map
     * @param location
     */

    private void moveCamera(Location location){

        CameraUpdate zoom=CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),
                location.getLongitude()), 15);

        mMap.animateCamera(zoom);

    }


    /**
     * Simple method that will add a marker on our map and marker collection with a snippet
     * @param location
     */
    private void addMarker(Location location,boolean setFirst){

        Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),
                location.getLongitude())).title("Location with accuracy " + location.getAccuracy()));

        String date = Utils.dateFormatter(new Date(location.getTime()));

        marker.setSnippet("Taken "+date);

        if(setFirst)
            mMarkers.add(0,marker);
        else
            mMarkers.add(marker);

    }


}
