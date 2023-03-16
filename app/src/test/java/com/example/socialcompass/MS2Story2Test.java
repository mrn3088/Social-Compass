package com.example.socialcompass;

import static org.junit.Assert.assertEquals;

import com.example.socialcompass.utilities.Utilities;

import org.junit.Test;

public class MS2Story2Test {

    @Test
    public void calculateDistanceTest(){

        double lat1 = 0.0;
        double long1 = 0.0;
        double lat2 = 90.0;
        double long2 = 90.0;
        double calculatedDistance = 0;
        double expectedDistance = 6218.47;

        calculatedDistance = Utilities.calculateDistance(lat1, long1,lat2,long2);

        assertEquals(expectedDistance, calculatedDistance, 0.02);
    }


}
