package com.example.socialcompass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import java.util.ArrayList;

public class LabelParsingTests {

    @Test
    public void checkNumLabels() {
        // accurate number of labels
       ArrayList<String> testList = new ArrayList<>();
       testList.add("labelOne"); testList.add("labelTwo"); testList.add("labelThree");
       assertEquals(true,Utilities.namedLabels(testList));
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

    @Test
    public void parseCoordinatesTest() {
        // HOW TO TEST PARSE COORDINATES METHOD????
    }

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


}
