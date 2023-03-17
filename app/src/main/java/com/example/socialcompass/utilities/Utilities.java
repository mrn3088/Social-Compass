/**
 * This file has Utilities class which includes a lot of
 * static methods used in the app
 */

package com.example.socialcompass.utilities;

import static java.lang.Math.log;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This class is Utilities class including a lot of static methods
 * used in the app
 */
public class Utilities {
    static final int MAX_LABEL_LENGTH = 20;
    public static final int INVISIBLE_CIRCLE = 3000;

    public static final int DISPLAY_MARGIN = 450;

    static final String USE_PHONE_ORIENTATION = "-1";
    static final String INCORRECT_EMPTY = "You need to enter at least one location!";
    static final String INCORRECT_FORMAT = "your coordinates are not entered in correct format!\nPl" +
            "ease enter two number separated by a space.";
    static final String INCORRECT_LOCATION = "your location does not exist:\n" +
            "Latitude should be in [-90, 90], Longitude should be in [-180, 180]";
    static final Map<String, String> valueDisplayMap = new HashMap<>() {{
        put("-360.0 -360.0", "");
    }};

    public static final String DEFAULT_URL = "https://socialcompass.goto.ucsd.edu/";

    /**
     * Display an alert dialog
     *
     * @param activity Activity to display alert on
     * @param message  Message to display
     */
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


    /**
     * Convert radians to degrees
     *
     * @param rad Radians
     * @return Degrees float
     */
    public static float radiansToDegreesFloat(float rad) {
        return (float) (rad * 180 / Math.PI);
    }

    /**
     * Convert radians to degrees
     *
     * @param rad Radians
     * @return Degrees double
     */
    public static double radiansToDegreesDouble(double rad) {
        return (double) rad * 180 / Math.PI;
    }

    /**
     * find the relative angle between two points
     *
     * @param currentLocation current location
     * @param destination     destination
     * @return relative angle
     */
    public static float relativeAngle(Pair<Double, Double> currentLocation, Pair<Double, Double> destination) {
        return relativeAngleUtils(currentLocation.first, currentLocation.second, destination.first, destination.second);
    }


    /**
     * find the relative angle between two points
     *
     * @param userLat  user latitude
     * @param userLong user longitude
     * @param destLat  destination latitude
     * @param destLong destination longitude
     * @return relative angle
     */
    public static float relativeAngleUtils(Double userLat, Double userLong, Double destLat, Double destLong) {
        Double latitudeDifference = destLat - userLat;
        Double longitudeDifference = destLong - userLong;

        if (latitudeDifference > 0 && longitudeDifference > 0) {
            return (float) radiansToDegreesDouble(Math.atan(longitudeDifference / latitudeDifference));
        }

        if (latitudeDifference < 0 && longitudeDifference > 0) {
            return (float) (180 - radiansToDegreesDouble(Math.atan(-longitudeDifference / latitudeDifference)));
        }

        if (latitudeDifference < 0 && longitudeDifference < 0) {
            return (float) (180 + radiansToDegreesDouble(Math.atan(longitudeDifference / latitudeDifference)));
        }

        if (latitudeDifference > 0 && longitudeDifference < 0) {
            return (float) (360 - radiansToDegreesDouble(Math.atan(-longitudeDifference / latitudeDifference)));
        }

        return 0f;
    }

    /**
     * Parse a coordinate string into a float array
     *
     * @param str Coordinate string
     * @return Optional float array of latitude and longitude
     */
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


    /**
     * Parse a label string into a string
     *
     * @param labelNames Label string
     * @return true if total number of labels is 3, false otherwise
     */
    public static boolean namedLabels(List<String> labelNames) {
        int totalNames = 0;
        for (int i = 0; i < labelNames.size(); i++) {
            if (!labelNames.get(i).isBlank()) {
                totalNames++;
            }
        }
        return totalNames == 3;
    }

    /**
     * Check if label lengths are valid
     *
     * @param labelNames List of label names
     * @return true if all label lengths are less than 20, false otherwise
     */
    public static boolean validLabelLengths(List<String> labelNames) {
        for (int i = 0; i < labelNames.size(); i++) {
            if (labelNames.get(i).length() > 20) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if coordinates are valid
     *
     * @param coordinates List of coordinates
     * @return true if all coordinates are within the valid range, false otherwise
     */
    public static boolean validCoordinates(List<float[]> coordinates) {
        for (int i = 0; i < coordinates.size(); i++) {
            if (!validCoordinates(coordinates.get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if coordinates are valid
     *
     * @param coordinate
     * @return true if coordinates are valid false otherwise
     */
    public static boolean validCoordinates(float[] coordinate) {
        return (Math.abs(coordinate[0]) <= 90 && Math.abs(coordinate[1]) <= 180);
    }


    /**
     * Check if orientation is valid
     *
     * @param orientation
     * @return true if orientation is valid false otherwise
     */
    public static boolean validOrientation(String orientation) {
        if (orientation.equals(USE_PHONE_ORIENTATION)) return true;
        try {
            int orientationValue = Integer.parseInt(orientation);
            return orientationValue <= 359 && orientationValue >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * display string
     *
     * @param str
     * @return the string to display
     */
    public static String getDisplayStr(String str) {
        return valueDisplayMap.getOrDefault(str, str);
    }

    public static double calculateDistance(double lat1, double long1, double lat2, double long2) {
        return 3958.8 * Math.acos(Math.cos((lat1 / 180.0) * Math.PI) * Math.cos((lat2 / 180.0) * Math.PI) * Math.cos(((long1 - long2) / 180.0) * Math.PI) + Math.sin((lat1 / 180.0) * Math.PI) * Math.sin((lat2 / 180.0) * Math.PI));
    }

    private static double getOuterCircleActualDistance(int state) {
        if (state == 1) {
            return 1.0;
        } else if (state == 2) {
            return 10.0;
        } else if (state == 3) {
            return 500.0;
        } else {
            return 1000.0;
        }
    }

    public static double calculateUserViewRadius(double distance, int state) {
        if (state == 1) {
            if (distance >= 1.0) {
                return DISPLAY_MARGIN;
            }
            return distance / 1.0 * DISPLAY_MARGIN;
        } else if (state == 2) {
            if (distance >= 10.0) {
                return DISPLAY_MARGIN;
            } else if (distance >= 1.0) {
                return (distance - 1.0) / (10.0 - 1.0) * 225.5 + 225.5;
            } else {
                return distance / 1.0 * 225.5;
            }
        } else if (state == 3) {
            if (distance >= 500.0) {
                return DISPLAY_MARGIN;
            } else if (distance >= 10.0) {
                return (distance - 10.0) / (500.0 - 10.0) * 150 + 300;
            } else if (distance >= 1.0) {
                return (distance - 1.0) / (10.0 - 1.0) * 150 + 150;
            } else {
                return distance / 1.0 * 150;
            }
        } else {
            if (distance >= 12450.5) {
                return DISPLAY_MARGIN;
            } else if (distance >= 500.0) {
                //return (log(distance - 500.0)) / (log(12450.5) - log(500.0)) * 100 + 350;
                return (distance-500.0)/(12450.5-500.0)*100+350;
            } else if (distance >= 10.0) {
                return (distance - 10.0) / (500.0 - 10.0) * 100 + 250;
            } else if (distance >= 1.0) {
                return (distance - 1.0) / (10.0 - 1.0) * 100 + 150;
            } else {
                return distance / 1.0 * 150;
            }
        }
    }
}


