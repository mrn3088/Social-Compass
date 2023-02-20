/**
 * This file has tests for User Story 4
 */

package com.example.socialcompass;

import static org.junit.Assert.assertEquals;

import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import androidx.test.rule.GrantPermissionRule;
@RunWith(RobolectricTestRunner.class)
public class Story4Test {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public GrantPermissionRule fRuntimePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule fRuntimePermissionRuleCoarse = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_COARSE_LOCATION);

    /**
     * The test for user turning the phone
     */
    @Test
    public void orientationSet(){
        float testValueOne = (float) Math.PI;
        float testValueTwo = (float) 0;
        float testValueThree = (float) (Math.PI / 2);

        var scenario = ActivityScenario.launch(ShowMapActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            var orientationService = OrientationService.singleton(activity);

            var mockOrientation = new MutableLiveData<Float>();
            orientationService.setMockOrientationData(mockOrientation);
            // We don't want to have to do this! It's not our job to tell the activity!
            activity.reobserveOrientation();

            mockOrientation.setValue(testValueOne);
            activity.reobserveOrientation();

            TextView north = (TextView) activity.findViewById(R.id.North);
            ConstraintLayout.LayoutParams northlayoutparams = (ConstraintLayout.LayoutParams) north.getLayoutParams();
            float northAngle1 = northlayoutparams.circleAngle;


            mockOrientation.setValue(testValueTwo);
            activity.reobserveOrientation();
            north = (TextView) activity.findViewById(R.id.North);
            northlayoutparams = (ConstraintLayout.LayoutParams) north.getLayoutParams();
            float northAngle2 = northlayoutparams.circleAngle;

            mockOrientation.setValue(testValueThree);
            activity.reobserveOrientation();
            north = (TextView) activity.findViewById(R.id.North);
            northlayoutparams = (ConstraintLayout.LayoutParams) north.getLayoutParams();
            float northAngle3 = northlayoutparams.circleAngle;

            var expected1 = 180.0;
            var expected2 = 360.0;
            var expected3 = 270.0;

            assertEquals(expected1, northAngle1, 0.1);
            assertEquals(expected2, northAngle2, 0.1);
            assertEquals(expected3, northAngle3, 0.1);
        });
    }

}
