package com.example.socialcompass;

import static org.junit.Assert.assertEquals;

import android.util.Pair;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import androidx.test.rule.GrantPermissionRule;

import java.util.List;
import java.util.Optional;

@RunWith(RobolectricTestRunner.class)
public class LocationTests {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public GrantPermissionRule fRuntimePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule fRuntimePermissionRuleCoarse = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_COARSE_LOCATION);

    @Test
    public void locationSet() {
        Pair<Double, Double> testValue = Pair.create(35.006, 78.5546);
        var scenario = ActivityScenario.launch(ShowMapActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            var locationService = LocationService.singleton(activity);

            var mockLocation = new MutableLiveData<Pair<Double, Double>>();
            locationService.setMockOrientationSource(mockLocation);
            activity.reobserveLocation();
            mockLocation.setValue(testValue);

            var expected = testValue;
            var observed = activity.getPreviousLocation();
            assertEquals(expected.first, observed.first, 0.002);
            assertEquals(expected.second, observed.second, 0.002);
        });
    }

    @Test
    public void locationUpdatesWithChange() {
        Pair<Double, Double> testValue = Pair.create(35.006, 78.5546);
        Pair<Double, Double> testValueTwo = Pair.create(56.9987, 10.002);
        Pair<Double, Double> testValueThree = Pair.create(34.555, 45.6676);
        var scenario = ActivityScenario.launch(ShowMapActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            var locationService = LocationService.singleton(activity);

            var mockLocation = new MutableLiveData<Pair<Double, Double>>();
            locationService.setMockOrientationSource(mockLocation);
            activity.reobserveLocation();
            mockLocation.setValue(testValue);
            mockLocation.setValue(testValueTwo);
            mockLocation.setValue(testValueThree);

            var expected = testValueThree;
            var observed = activity.getPreviousLocation();
            assertEquals(expected.first, observed.first, 0.002);
            assertEquals(expected.second, observed.second, 0.002);
        });
    }
}
