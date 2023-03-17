package com.example.socialcompass;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import com.example.socialcompass.entity.Position;
import com.example.socialcompass.utilities.locationCalculations;

public class PositionTests {

    // check that two positions which are in the same spot, are
    // zero meters away from each other
    @Test
    public void checkSamePosition() {
        Position positionOne = new Position(0,0);
        Position positionTwo = new Position(0,0);
        final double distance = locationCalculations.distance(positionOne, positionTwo);
        assertEquals(locationCalculations.distance(positionOne, positionTwo), 0, 1);
    }
    @Test
    public void checkOneHundredKilometer() {
        Position positionOne = new Position(40,40);
        Position positionTwo = new Position(41,41);
        double expectedKilometerDifference = 137.7;
        double expectedMeterDifference = expectedKilometerDifference * 1000;
        assertEquals(expectedMeterDifference, locationCalculations.distance(positionOne, positionTwo), 3200);
        assertEquals(expectedMeterDifference, locationCalculations.distance(positionTwo, positionOne), 3200);
    }
    @Test
    public void checkTenKilometer() {
        Position positionOne = new Position(40.0, 40.0);
        Position positionTwo = new Position(40.1, 40.1);
        double expectedKilometerDifference = 14.00;
        double expectedMeterDifference = expectedKilometerDifference * 1000;
        assertEquals(expectedMeterDifference, locationCalculations.distance(positionOne, positionTwo), 20);
        assertEquals(expectedMeterDifference, locationCalculations.distance(positionTwo, positionOne), 20);
    }
    @Test
    public void checkOneKilometer() {
        Position positionOne = new Position(40.00, 40.00);
        Position positionTwo = new Position(40.01, 40.01);
        double expectedKilometerDifference = 1.40;
        double expectedMeterDifference = expectedKilometerDifference * 1000;
        assertEquals(expectedMeterDifference, locationCalculations.distance(positionOne, positionTwo), 5);
        assertEquals(expectedMeterDifference, locationCalculations.distance(positionTwo, positionOne), 5);
    }


}
