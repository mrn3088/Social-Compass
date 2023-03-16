/**
 * This file has tests for User Story 5
 */

package com.example.socialcompass;

import static org.junit.Assert.assertEquals;

import android.util.Pair;
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
public class MS2Story5Test {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public GrantPermissionRule fRuntimePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule fRuntimePermissionRuleCoarse = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_COARSE_LOCATION);

    /**
     * This is the test for user changing location
     */
    @Test
    public void locationSet() {
        /*
        Position testValue = new Position(35.006, 78.5546);
        var scenario = ActivityScenario.launch(ShowMapActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            var locationService = LocationService.singleton(activity);
            var orientationService = OrientationService.singleton(activity);
            var mockOrientation = new MutableLiveData<Float>();
            mockOrientation.setValue(0f);
            orientationService.setMockOrientationData(mockOrientation);



            activity.setOrientation(0f);
            activity.reobserveOrientation();
            var mockLocation = new MutableLiveData<Position>();

            locationService.setMockOrientationSource(mockLocation);
            activity.reobserveLocation();
            mockLocation.setValue(testValue);

            Pair<Double, Double> destination = Pair.create(20.0, 30.0);
            activity.setDestination1(20.0, 30.0);
            activity.reobserveLocation();

            TextView north = (TextView) activity.findViewById(R.id.North);
            ConstraintLayout.LayoutParams northlayoutparams = (ConstraintLayout.LayoutParams) north.getLayoutParams();
            float northAngle = northlayoutparams.circleAngle;

            TextView text1 = (TextView) activity.findViewById(R.id.label1);
            ConstraintLayout.LayoutParams text1layoutparams =  (ConstraintLayout.LayoutParams) text1.getLayoutParams();
            float text1Angle = text1layoutparams.circleAngle;

            var expected = 252.826f;
            assertEquals(252.826f, text1Angle, 0.1f);

            Pair<Double, Double> destination2 = Pair.create(21.0,31.0);
            activity.setDestination1(21.0, 31.0);
            activity.reobserveLocation();

            north = (TextView) activity.findViewById(R.id.North);
            northlayoutparams = (ConstraintLayout.LayoutParams) north.getLayoutParams();
            text1 = (TextView) activity.findViewById(R.id.label1);
            text1layoutparams =  (ConstraintLayout.LayoutParams) text1.getLayoutParams();
            text1Angle = text1layoutparams.circleAngle;
            expected = 253.589f;
            assertEquals(expected, text1Angle, 0.1);

        });

         */
    }
}
