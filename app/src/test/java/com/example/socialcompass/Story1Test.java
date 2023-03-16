/**
 * This file has tests for User Story 1
 */

package com.example.socialcompass;

import static org.junit.Assert.assertEquals;

import android.widget.TextView;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;
import androidx.test.rule.GrantPermissionRule;

import com.example.socialcompass.activity.ShowMapActivity;
import com.example.socialcompass.service.OrientationService;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

// story 1: see which way is north
@RunWith(RobolectricTestRunner.class)
public class Story1Test {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public GrantPermissionRule fRuntimePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule fRuntimePermissionRuleCoarse = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_COARSE_LOCATION);

    @Test
    public void testNorth(){

        var scenario = ActivityScenario.launch(ShowMapActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            var orientationService = OrientationService.singleton(activity);

            var mockOrientation = new MutableLiveData<Float>();
            mockOrientation.setValue((float)Math.PI);
            orientationService.setMockOrientationData(mockOrientation);
            // We don't want to have to do this! It's not our job to tell the activity!
            activity.reobserveOrientation();

            TextView north = (TextView) activity.findViewById(R.id.North);
            ConstraintLayout.LayoutParams northlayoutparams = (ConstraintLayout.LayoutParams) north.getLayoutParams();
            float northValue = northlayoutparams.circleAngle;
            float expected = 180;

            assertEquals(expected, northValue, 0.002);
        });
    }

}
