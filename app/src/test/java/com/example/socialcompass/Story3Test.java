/**
 * This file has tests for User Story 3
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

//test persistence of house locations
@RunWith(RobolectricTestRunner.class)
public class Story3Test {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public GrantPermissionRule fRuntimePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule fRuntimePermissionRuleCoarse = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_COARSE_LOCATION);

    @Test
    public void testLocationPersistence(){

        var scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            TextView parentCoordinate = (TextView)activity.findViewById(R.id.label_1_coordinates);
            parentCoordinate.setText("45.0 45.0");
            View submit_labels= activity.findViewById(R.id.submit_labels);
            submit_labels.callOnClick();

            //kill the activity
            activity.finish();

        });

        ///restart activity
        scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {

            activity.loadProfile();
            TextView parentCoordinate = (TextView)activity.findViewById(R.id.label_1_coordinates);

            String expected = new String("45.0 45.0");
            String observed = parentCoordinate.getText().toString();
            assert (expected.equals(observed));


        });
    }
}