/**
 * This file has Service interface which defines the template for sensor service
 */
package com.example.socialcompass.service;

import android.app.Activity;

/**
 * This interface Service is used to define the template for sensor service
 */
public interface Service {

    public abstract void registerSensorListeners();

    public abstract void unregisterSensorListeners();

    public abstract void onPause();


}
