package com.example.socialcompass;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Pair;

import java.util.Optional;

public class Utilities {
    static final int MAX_LABEL_LENGTH = 20;

    public static void displayAlert(Activity activity, String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);
        alertBuilder
                .setTitle("Alert!")
                .setMessage(message)
                .setPositiveButton("Ok", (dialog, id) -> {
                    dialog.cancel();
                })
                .setCancelable(true);
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    public static float radiansToDegreesFloat(float rad){
        return (float)(rad*180/Math.PI);
    }

    public static double radiansToDegreesDouble(double rad){
        return (double) rad*180/Math.PI;
    }

    public static float relativeAngle(Pair<Double, Double> currentLocation, Pair<Double, Double> destination){
        Double latitudeDifference = destination.first-currentLocation.first;
        Double longitudeDifference = destination.second-currentLocation.second;

        if(latitudeDifference>0 && longitudeDifference>0){
            return (float) radiansToDegreesDouble(Math.atan(longitudeDifference/latitudeDifference));
        }

        if(latitudeDifference<0 && longitudeDifference>0){
            return (float) (180 - radiansToDegreesDouble(Math.atan(-longitudeDifference/latitudeDifference)));
        }

        if(latitudeDifference<0 && longitudeDifference<0){
            return (float) (180 + radiansToDegreesDouble(Math.atan(longitudeDifference/latitudeDifference)));
        }

        if(latitudeDifference>0 && longitudeDifference<0){
            return (float) (360 - radiansToDegreesDouble(Math.atan(-longitudeDifference/latitudeDifference)));
        }

        return 0f;

    }

    public static Optional<float[]> parseCoordinate(String str) {
        String[] cordArr = str.split(" ");
        if (cordArr.length != 2) {
            return Optional.empty();
        }
        try {
            float latitude = Float.parseFloat(cordArr[0]);
            float longitude = Float.parseFloat(cordArr[1]);
            return Optional.of(new float[]{latitude, longitude});
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }




}


