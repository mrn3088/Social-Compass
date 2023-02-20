/**
 * This file has tests for User Story 2
 */

package com.example.socialcompass;

import static org.junit.Assert.assertEquals;

import android.util.Pair;
import android.widget.TextView;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;
import androidx.test.rule.GrantPermissionRule;

import org.bouncycastle.pqc.jcajce.provider.qtesla.SignatureSpi;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

// story 2: check parent direction
@RunWith(RobolectricTestRunner.class)
public class Story2Test {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public GrantPermissionRule fRuntimePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule fRuntimePermissionRuleCoarse = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_COARSE_LOCATION);

    @Test
    public void testParentDirection(){

        Pair<Double, Double> myPosition = Pair.create(0.0, 0.0);
        float myOrientation = (float)Math.PI;
        double parentLongitude = 45.0;
        double parentLatitude = 45.0;
        var scenario = ActivityScenario.launch(ShowMapActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            var locationService = LocationService.singleton(activity);
            var mockLocation = new MutableLiveData<Pair<Double, Double>>();
            locationService.setMockOrientationSource(mockLocation);
            mockLocation.setValue(myPosition);

            var orientationService = OrientationService.singleton(activity);
            var mockOrientation = new MutableLiveData<Float>();
            mockOrientation.setValue(myOrientation);
            orientationService.setMockOrientationData(mockOrientation);

            activity.setDestination1(parentLatitude, parentLongitude);

            activity.reobserveOrientation();
            activity.reobserveLocation();

            TextView text1 = (TextView) activity.findViewById(R.id.label1);
            ConstraintLayout.LayoutParams label1layoutparams = (ConstraintLayout.LayoutParams) text1.getLayoutParams();

            var expected = 225;
            var observed = label1layoutparams.circleAngle;
            assertEquals(expected, observed, 0.002);

        });
    }

}
