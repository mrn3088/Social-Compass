/**
 * This file has tests for relative angles
 */


package com.example.socialcompass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.example.socialcompass.utilities.Utilities;

import org.junit.Test;

public class RelativeAngleTests {
    /**
     * Test radiansToDegreesDouble() method
     */
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
     * Test radiansToDegreesFloat() method
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
     * Test relativeAngleUtils() method
     */
    @Test
    public void testRelativeAngle(){
        float offset = 0.2f;
        float expectedAngle1 = 0.0f;
        float realAngle1 = Utilities.relativeAngleUtils(32.0, -117.0, 90.0, -117.0);
        System.out.println(realAngle1);
        if(realAngle1>=expectedAngle1-offset && realAngle1<=expectedAngle1+offset){
            assertTrue(true);
        }else{
            assertTrue(false);
        }

        float expectedAngle2 = 0.0f;
        float realAngle2 = Utilities.relativeAngleUtils(32.0, -117.0, 35.0, -117.0);
        System.out.println(realAngle1);
        if(realAngle2>=expectedAngle2-offset && realAngle2<=expectedAngle2+offset){
            assertTrue(true);
        }else{
            assertTrue(false);
        }

        float expectedAngle3 = 90.0f;
        float realAngle3 = Utilities.relativeAngleUtils(32.0, -117.0, 32.01, 0.0);
        System.out.println(realAngle3);
        if(realAngle3>=expectedAngle3-offset && realAngle3<=expectedAngle3+offset){
            assertTrue(true);
        }else{
            assertTrue(false);
        }

        float expectedAngle4 = 270.0f;
        float realAngle4 = Utilities.relativeAngleUtils(32.0, -117.0, 32.01, -120.0);
        System.out.println(realAngle4);
        if(realAngle4>=expectedAngle4-offset && realAngle4<=expectedAngle4+offset){
            assertTrue(true);
        }else{
            assertTrue(false);
        }

        float expectedAngle5 = 45.0f;
        float realAngle5 = Utilities.relativeAngleUtils(20.0, 30.0, 40.0, 50.0);
        System.out.println(realAngle5);
        if(realAngle5>=expectedAngle5-offset && realAngle5<=expectedAngle5+offset){
            assertTrue(true);
        }else{
            assertTrue(false);
        }
    }
}
