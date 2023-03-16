/**
 * This file has tests for location service
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
import com.example.socialcompass.entity.Position;
import com.example.socialcompass.service.LocationService;

@RunWith(RobolectricTestRunner.class)
public class LocationTests {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public GrantPermissionRule fRuntimePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule fRuntimePermissionRuleCoarse = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_COARSE_LOCATION);

    /**
     * Test that location can be set correctly
     */
    @Test
    public void locationSet() {
        Position testValue = new Position(35.006, 78.5546);
        var scenario = ActivityScenario.launch(ShowMapActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            var locationService = LocationService.singleton(activity);

            MutableLiveData<Position> mockLocation = new MutableLiveData<>();
            locationService.setMockOrientationSource(mockLocation);
            activity.reobserveLocation();
            mockLocation.setValue(testValue);

            var expected = testValue;
            var observed = activity.getPreviousLocation();
            assertEquals(expected.getLatitude(), observed.getLatitude(), 0.002);
            assertEquals(expected.getLongitude(), observed.getLongitude(), 0.002);
        });
    }

    /**
     * Test that location can be updated correctly
     */
    @Test
    public void locationUpdatesWithChange() {
        Position testValue = new Position(35.006, 78.5546);
        Position testValueTwo = new Position(56.9987, 10.002);
        Position testValueThree = new Position(34.555, 45.6676);
        var scenario = ActivityScenario.launch(ShowMapActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            var locationService = LocationService.singleton(activity);

            var mockLocation = new MutableLiveData<Position>();
            locationService.setMockOrientationSource(mockLocation);
            activity.reobserveLocation();
            mockLocation.setValue(testValue);
            mockLocation.setValue(testValueTwo);
            mockLocation.setValue(testValueThree);

            var expected = testValueThree;
            var observed = activity.getPreviousLocation();
            assertEquals(expected.getLongitude(), observed.getLongitude(), 0.002);
            assertEquals(expected.getLatitude(), observed.getLatitude(), 0.002);
        });
    }
}
