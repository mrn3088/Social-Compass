/**
 * This file has tests for milestone 1
 */

package com.example.socialcompass;

import static org.junit.Assert.assertEquals;

import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

//test that check mainActivity and showMapActivity in sequence
@RunWith(RobolectricTestRunner.class)
public class MS1Test {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public GrantPermissionRule fRuntimePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule fRuntimePermissionRuleCoarse = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_COARSE_LOCATION);

    @Test
    public void testMS1(){

        float myOrientation = (float)Math.PI;
        Pair<Double, Double> myPosition = Pair.create(0.0, 0.0);

        //start MainActivity
        var scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            TextView parentCoordinate = (TextView)activity.findViewById(R.id.label_1_coordinates);
            parentCoordinate.setText("45.0 45.0");
            View submit_labels= activity.findViewById(R.id.submit_labels);
            submit_labels.callOnClick();


        });

        //start ShowMapActivity
        var scenario2 = ActivityScenario.launch(ShowMapActivity.class);
        scenario2.moveToState(Lifecycle.State.STARTED);
        scenario2.onActivity(activity -> {
            var orientationService = OrientationService.singleton(activity);
            var mockOrientation = new MutableLiveData<Float>();
            mockOrientation.setValue(myOrientation);
            orientationService.setMockOrientationData(mockOrientation);
            activity.reobserveOrientation();

            var locationService = LocationService.singleton(activity);
            var mockLocation = new MutableLiveData<Pair<Double, Double>>();
            mockLocation.setValue(myPosition);
            locationService.setMockOrientationSource(mockLocation);
            activity.reobserveLocation();

            activity.loadProfile();

            TextView parentHome = (TextView) activity.findViewById(R.id.label1);
            ConstraintLayout.LayoutParams parentHomeParams = (ConstraintLayout.LayoutParams) parentHome.getLayoutParams();

            var expected = 225;
            var observed = parentHomeParams.circleAngle;
            assertEquals(expected, observed, 0.002);

        });
    }
}
