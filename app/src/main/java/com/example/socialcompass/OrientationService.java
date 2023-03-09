/**
 * This file has OrientationService class which is used to track orientation
 */
package com.example.socialcompass;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


/**
 * This class is OrientationService class used to track users' location
 */
public class OrientationService implements SensorEventListener, Service {
    private static OrientationService instance;
    private final SensorManager sensorManager;
    private float[] accelerometerReading;
    private float[] magnetometerReading;
    private MutableLiveData<Float> azimuth;

    /**
     * init OrientationService
     *
     * @param activity Context needed to initiate SensorManager
     */
    protected OrientationService(Activity activity) {
        this.azimuth = new MutableLiveData<>();
        this.sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        this.registerSensorListeners();
    }

    /**
     * Register sensor listeners
     */
    public void registerSensorListeners() {
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Singleton for OrientationService
     *
     * @param activity Context needed to initiate SensorManager
     * @return OrientationService instance
     */
    public static OrientationService singleton(Activity activity) {
        if (instance == null) {
            instance = new OrientationService(activity);
        }
        return instance;
    }

    /**
     * Called when sensor values have changed.
     *
     * @param event The new sensor event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerReading = event.values;
        }

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magnetometerReading = event.values;
        }
        if (accelerometerReading != null && magnetometerReading != null) {
            onBothSensorDataAvailable();
        }
    }

    /**
     * Called when the accuracy of the registered sensor has changed.
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    /**
     * Called when both sensor data is available
     */
    private void onBothSensorDataAvailable() {
        if (accelerometerReading == null || magnetometerReading == null) {
            throw new IllegalArgumentException("Both sensors must be available to compute orientation.");
        }

        float[] r = new float[9];
        float[] i = new float[9];

        boolean success = SensorManager.getRotationMatrix(r, i, accelerometerReading, magnetometerReading);

        if (success) {
            float[] orientation = new float[3];
            SensorManager.getOrientation(r, orientation);

            this.azimuth.postValue(orientation[0]);
        }
    }

    /**
     * Unregister sensor listeners
     */
    public void unregisterSensorListeners() {
        sensorManager.unregisterListener(this);
    }

    /**
     * Get orientation
     *
     * @return orientation
     */
    public LiveData<Float> getOrientation() {
        return this.azimuth;
    }

    /**
     * Set mock orientation data
     *
     * @param mockDataSource mock data source
     */
    public void setMockOrientationSource(MutableLiveData<Float> mockDataSource) {
        unregisterSensorListeners();
        this.azimuth = mockDataSource;
    }

    /**
     * Called when the activity is starting.
     */
    public void onPause() {
        this.unregisterSensorListeners();
    }

    /**
     * Called when the activity will start interacting with the user.
     */
    public void setMockOrientationData(MutableLiveData<Float> mockData) {
        unregisterSensorListeners();
        this.azimuth = mockData;
    }


}
