package com.example.socialcompass.utilities;

import com.example.socialcompass.entity.Position;

public class locationCalculations {
    public static float relativeAngleUtils(Position userLocation, Position destination) {
        Double latitudeDifference = destination.getLatitude() - userLocation.getLatitude();
        Double longitudeDifference = destination.getLongitude() - userLocation.getLongitude();

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

    public static float relativeAngle(Position currentLocation, Position destination) {
        return relativeAngleUtils(currentLocation, destination);
    }

    public static double radiansToDegreesDouble(double rad) {
        return (double) rad * 180 / Math.PI;
    }

    public static float radiansToDegreesFloat(float rad) {
        return (float) (rad * 180 / Math.PI);
    }

    public static double degreesToRadiansDouble(double degrees) {
        return degrees / (180 / Math.PI);
    }

    public static double milesToMeters(double miles) {
        return miles * 1609.34;
    }

    public static double distance(Position locationOne, Position locationTwo) {
        // if Positions are equal return zero distance
        if (locationOne.getLatitude() == locationTwo.getLatitude()
                && locationOne.getLongitude() == locationTwo.getLongitude()) {
            return 0;
        }
        // calculating radian values for longitude, and latitude of both locations
        final double latitudeOneRadian = degreesToRadiansDouble(locationOne.getLatitude());
        final double longitudeOneRadian = degreesToRadiansDouble(locationOne.getLongitude());


        final double latitudeTwoRadian = degreesToRadiansDouble(locationTwo.getLatitude());
        final double longitudeTwoRadian = degreesToRadiansDouble(locationTwo.getLongitude());

        final double haversineConstant = 3963.0;

        // calculated using haversine formula
        final double distanceInMiles = haversineConstant *
                Math.acos((Math.sin(latitudeOneRadian) * Math.sin(latitudeTwoRadian)) +
                        Math.cos(latitudeOneRadian) * Math.cos(latitudeTwoRadian) * Math.cos(longitudeTwoRadian - latitudeOneRadian));

        // conversion from miles to meters
        return milesToMeters(distanceInMiles);
    }


}
