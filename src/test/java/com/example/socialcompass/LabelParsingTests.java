/**
 * This file has tests for label parsing
 */

package com.example.socialcompass;

import static com.example.socialcompass.utilities.Utilities.parseCoordinate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.example.socialcompass.utilities.Utilities;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Optional;

public class LabelParsingTests {

    /**
     * namedLabels() method tests
     */
    @Test
    public void checkNamedLabels() {
        // accurate number of labels
       ArrayList<String> testList = new ArrayList<>();
       testList.add("labelOne"); testList.add("labelTwo"); testList.add("labelThree");
       assertEquals(true, Utilities.namedLabels(testList));
       // blank labels shouldn't be counted
        testList.clear();
        testList.add(" "); testList.add(""); testList.add("     ");
        assertEquals(false, Utilities.namedLabels(testList));
        // not enough labels
        testList.add("a"); testList.add("b");
        assertEquals(false, Utilities.namedLabels(testList));
        // valid amount of labels with blanks
        testList.add("c");
        assertEquals(true, Utilities.namedLabels(testList));
        // too many labels, blanks included
        testList.add("d");
        assertEquals(false, Utilities.namedLabels(testList));
        testList.clear();
        // too many labels no blanks
        testList.add("a"); testList.add("b"); testList.add("c"); testList.add("d");
        assertEquals(false, Utilities.namedLabels(testList));
    }

    /**
     * validLabelLengths() method tests
     */
    @Test
    public void checkLabelSizes(){
        ArrayList<String> testList = new ArrayList<>();
        // checks for valid inputs
        for(int i = 0; i < 99; i++) {
            testList.add("" + i);
            assertEquals(true, Utilities.validLabelLengths(testList));
        }
        // check for invalid input following sequence of valid inputs
        testList.add("this is way over twenty characters");
        assertEquals(false, Utilities.validLabelLengths(testList));
        // check for empty list
        testList.clear();
        assertEquals(true,Utilities.validLabelLengths(testList));
        // check for element with 19 characters
        testList.add("aaaaaaaaaaaaaaaaaaa");
        assertEquals(true, Utilities.validLabelLengths(testList));
        // check for element with 20 characters
        testList.add("aaaaaaaaaaaaaaaaaaaaa");
        assertEquals(false, Utilities.validLabelLengths(testList));
    }

    /**
     * parseCoordinate() method tests
     */
    @Test
    public void parseCoordinatesTest() {
        String validInput = "23.3456 45.6789";
        Optional<float[]> result = parseCoordinate(validInput);

        assertTrue(result.isPresent());
        float[] cordArr = result.get();
        assertEquals(23.3456f, cordArr[0], 0.0001f);
        assertEquals(45.6789f, cordArr[1], 0.0001f);

        String invalidInput = "23.3456";

        Optional<float[]> resultInvalid = parseCoordinate(invalidInput);

        assertFalse(resultInvalid.isPresent());

        invalidInput = "abc def";
        result = parseCoordinate(invalidInput);

        assertFalse(result.isPresent());

        invalidInput = "0 0 0";
        result = parseCoordinate(invalidInput);

        assertFalse(result.isPresent());
    }


    /**
     * validCoordinates() method tests
     */
    @Test
    public void validCoordinatesTest() {
        ArrayList<float[]> testList = new ArrayList<>();
        // check empty list
        assertEquals(true, Utilities.validCoordinates(testList));
        // checks one random valid value
        float[] testOne = {10, 20};
        testList.add(testOne);
        assertEquals(true, Utilities.validCoordinates(testList));
        // EDGE CASE- check if center of equator works
        float[] testTwo = {0,0};
        testList.add(testTwo);
        assertEquals(true, Utilities.validCoordinates(testList));
        //EDGE CASE- CHECK 90's + 180's
        float[] testThree = {90, 180};
        float[] testFour = {-90, 180};
        float[] testFive = {-90, -180};
        float[] testSix = {90, -180};
        testList.add(testThree); testList.add(testFour); testList.add(testFive); testList.add(testSix);
        assertEquals(true, Utilities.validCoordinates(testList));
        // check invalid positive first coordinate
        float[] testSeven = {91, 10};
        testList.add(testSeven);
        assertEquals(false, Utilities.validCoordinates(testList));
        testList.clear();
        // check invalid negative first coordinate
        float[] testEight = {-91, 10};
        testList.add(testEight);
        assertEquals(false, Utilities.validCoordinates(testList));
        testList.clear();
        // invalid positive second coordinate
        float[] testNine = {0, 181};
        testList.add(testNine);
        assertEquals(false, Utilities.validCoordinates(testList));
        testList.clear();
        //invalid negative second coordinate
        float[] testTen = {0, -181};
        testList.add(testTen);
        assertEquals(false, Utilities.validCoordinates(testList));
    }

    /**
     * radiansToDegreesDouble() method tests
     */
    public static class RelativeAngleTests {
        @Test
        public void testRadiansToDegreeFloat(){
            double offset = 0.1;
            double rad1 = 0.0;
            double expectedAngle1 = 0.0;
            double realAngle1 = Utilities.radiansToDegreesDouble(rad1);

            if(realAngle1>=expectedAngle1-offset && realAngle1<=expectedAngle1+offset){
                assertTrue(true);
            }else{
                assertTrue(false);
            }

            double rad2 = -2.324;
            double expectedAngle2 = -133.155;
            double realAngle2 = Utilities.radiansToDegreesDouble(rad2);
            if(realAngle2>=expectedAngle2-offset && realAngle2<=expectedAngle2+offset){
                assertTrue(true);
            }else{
                assertTrue(false);
            }

            double rad3 = 3.14;
            double expectedAngle3 = 179.909;
            double realAngle3 = Utilities.radiansToDegreesDouble(rad3);
            if(realAngle3>=expectedAngle3-offset && realAngle3<=expectedAngle3+offset){
                assertTrue(true);
            }else{
                assertTrue(false);
            }

            double rad4 = 10.6;
            double expectedAngle4 = 607.335;
            double realAngle4 = Utilities.radiansToDegreesDouble(rad4);
            if(realAngle4>=expectedAngle4-offset && realAngle4<=expectedAngle4+offset){
                assertTrue(true);
            }else{
                assertTrue(false);
            }
        }

        /**
         * radiansToDegreesFloat() method tests
         */
        @Test
        public void testRadiansToDegreeDouble(){
            float offset = 0.1f;
            float rad1 = 0.0f;
            float expectedAngle1 = 0.0f;
            float realAngle1 = Utilities.radiansToDegreesFloat(rad1);

            if(realAngle1>=expectedAngle1-offset && realAngle1<=expectedAngle1+offset){
                assertTrue(true);
            }else{
                assertTrue(false);
            }

            float rad2 = -2.324f;
            float expectedAngle2 = -133.155f;
            float realAngle2 = Utilities.radiansToDegreesFloat(rad2);
            if(realAngle2>=expectedAngle2-offset && realAngle2<=expectedAngle2+offset){
                assertTrue(true);
            }else{
                assertTrue(false);
            }

            float rad3 = 3.14f;
            float expectedAngle3 = 179.909f;
            float realAngle3 = Utilities.radiansToDegreesFloat(rad3);
            if(realAngle3>=expectedAngle3-offset && realAngle3<=expectedAngle3+offset){
                assertTrue(true);
            }else{
                assertTrue(false);
            }

            float rad4 = 10.6f;
            float expectedAngle4 = 607.335f;
            float realAngle4 = Utilities.radiansToDegreesFloat(rad4);
            if(realAngle4>=expectedAngle4-offset && realAngle4<=expectedAngle4+offset){
                assertTrue(true);
            }else{
                assertTrue(false);
            }
        }

        /**
         * relativeAngle() method tests
         */
        @Test
        public void testRelativeAngle(){

        }
    }
}
