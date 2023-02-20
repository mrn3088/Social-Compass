/**
 * This file has LocationService class which is used
 * to track the location
 */

package com.example.socialcompass;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * This class is LocationService class used to track users' location
 */
public class LocationService implements LocationListener {
    private static LocationService instance;
    private Activity activity;

    private MutableLiveData<Pair<Double, Double>> locationValue;

    private final LocationManager locationManager;

    /**
     * Singleton for LocationService
     *
     * @param activity Context needed to initiate LocationManager
     * @return LocationService instance
     */
    public static LocationService singleton(Activity activity) {
        if (instance == null) {
            instance = new LocationService(activity);
        }

        return instance;
    }

    /**
     * Constructor for LocationService
     *
     * @param activity Context needed to initiate LocationManager
     */
    protected LocationService(Activity activity) {
        this.locationValue = new MutableLiveData<>();
        this.activity = activity;
        this.locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        //Register sensor listeners
        this.registerLocationListener();
    }
    
    /**
     * Register location listener
     */
    private void registerLocationListener() {
        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            throw new IllegalStateException("Apps needs location permission to get latest location");
        }

        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    /**
     * Callback for location updates
     *
     * @param location New location
     */
    @Override
    public void onLocationChanged(@NonNull Location location) {
        this.locationValue.postValue(new Pair<Double, Double>(location.getLatitude(),
                location.getLongitude())
        );
    }

    /**
     * Unregister location listener
     */
    public void unregisterLocationListener(){
        locationManager.removeUpdates(this);
    }

    /**
     * Set mock location source
     *
     * @param mockDataSource Mock location source
     */
    public void setMockOrientationSource(MutableLiveData<Pair<Double,Double>> mockDataSource){
        unregisterLocationListener();
        this.locationValue = mockDataSource;
    }

    /**
     * Get location
     *
     * @return Location
     */
    public LiveData<Pair<Double,Double>> getLocation() {
        return this.locationValue;
    }

    /**
     * Unregister location listener on pause
     */
    public void onPause(){
        this.unregisterLocationListener();
    }
}
