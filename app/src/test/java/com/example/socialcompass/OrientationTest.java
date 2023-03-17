/**
 * This file has tests for orientation service
 */

package com.example.socialcompass;

import static org.junit.Assert.assertEquals;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import androidx.test.rule.GrantPermissionRule;

import com.example.socialcompass.activity.ShowMapActivity;
import com.example.socialcompass.activity.ShowMapActivityMocking;
import com.example.socialcompass.service.OrientationService;
import com.example.socialcompass.utilities.Utilities;

@RunWith(RobolectricTestRunner.class)
public class OrientationTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public GrantPermissionRule fRuntimePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule fRuntimePermissionRuleCoarse = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_COARSE_LOCATION);

    /**
     * Test that orientation can be set correctly
     */
    @Test
    public void orientation_service() {
        float testValue = (float) Math.PI;

        var scenario = ActivityScenario.launch(ShowMapActivityMocking.class);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            var orientationService = OrientationService.singleton(activity);

            var mockOrientation = new MutableLiveData<Float>();
            orientationService.setMockOrientationData(mockOrientation);
            // We don't want to have to do this! It's not our job to tell the activity!
            activity.reobserveOrientation();

            mockOrientation.setValue(testValue);
            //TextView textView = activity.findViewById(R.id.orientationText);

            var expected = 360 - Utilities.radiansToDegreesFloat(testValue);
            var observed = activity.getOrientation();
            assertEquals(expected, observed, 0.002);
        });
    }

    /**
     * Test that orientation can be updated correctly
     */
    @Test
    public void orientation_service_multiple_changes() {
        float testValueOne = (float) Math.PI;
        float testValueTwo = (float) 0;
        float testValueThree = (float) (Math.PI / 2);

        var scenario = ActivityScenario.launch(ShowMapActivityMocking.class);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            var orientationService = OrientationService.singleton(activity);

            var mockOrientation = new MutableLiveData<Float>();
            orientationService.setMockOrientationData(mockOrientation);
            // We don't want to have to do this! It's not our job to tell the activity!
            activity.reobserveOrientation();

            mockOrientation.setValue(testValueOne);
            mockOrientation.setValue(testValueTwo);
            mockOrientation.setValue(testValueThree);
            //TextView textView = activity.findViewById(R.id.orientationText);

            var expected = 360 - Utilities.radiansToDegreesFloat(testValueThree);
            var observed = activity.getOrientation();
            assertEquals(expected, observed, 0.002);
        });
    }
}
