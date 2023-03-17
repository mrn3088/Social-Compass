package com.example.socialcompass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;
import androidx.test.rule.GrantPermissionRule;

import com.example.socialcompass.activity.ShowMapActivity;
import com.example.socialcompass.activity.ShowMapActivityMocking;
import com.example.socialcompass.utilities.Utilities;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.util.UUID;

// Story 3: test zoom in and zoom out
@RunWith(RobolectricTestRunner.class)
public class MS2Story3Test {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public GrantPermissionRule fRuntimePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule fRuntimePermissionRuleCoarse = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_COARSE_LOCATION);

    @Test
    public void zoomInOutTest(){
        var scenario = ActivityScenario.launch(ShowMapActivityMocking.class);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            activity.updateCircles();
            var zoomInButton = (Button) activity.findViewById(R.id.zoom_in);
            var zoomOutButton = (Button) activity.findViewById(R.id.zoom_out);
            ImageView circle1 = activity.findViewById(R.id.circle1);
            ImageView circle2 = activity.findViewById(R.id.circle2);
            ImageView circle3 = activity.findViewById(R.id.circle3);
            ImageView circle4 = activity.findViewById(R.id.circle4);


            assertEquals(900/2, circle1.getLayoutParams().width);
            assertEquals(900/2, circle1.getLayoutParams().height);
            assertEquals(900, circle2.getLayoutParams().width);
            assertEquals(900, circle2.getLayoutParams().height);
            assertEquals(Utilities.INVISIBLE_CIRCLE, circle3.getLayoutParams().width);
            assertEquals(Utilities.INVISIBLE_CIRCLE, circle3.getLayoutParams().height);
            assertEquals(Utilities.INVISIBLE_CIRCLE, circle4.getLayoutParams().width);
            assertEquals(Utilities.INVISIBLE_CIRCLE, circle4.getLayoutParams().height);


            //activity.onZoomInClicked(zoomInButton);
            activity.state = activity.state-1;
            activity.updateCircles();

            assertEquals(900, circle1.getLayoutParams().width);
            assertEquals(900, circle1.getLayoutParams().height);
            assertEquals(Utilities.INVISIBLE_CIRCLE, circle2.getLayoutParams().width);
            assertEquals(Utilities.INVISIBLE_CIRCLE, circle2.getLayoutParams().height);
            assertEquals(Utilities.INVISIBLE_CIRCLE, circle3.getLayoutParams().width);
            assertEquals(Utilities.INVISIBLE_CIRCLE, circle3.getLayoutParams().height);
            assertEquals(Utilities.INVISIBLE_CIRCLE, circle4.getLayoutParams().width);
            assertEquals(Utilities.INVISIBLE_CIRCLE, circle4.getLayoutParams().height);

            //activity.onZoomOutClicked(zoomOutButton);
            //activity.onZoomOutClicked(zoomOutButton);
            activity.state = activity.state+1;
            activity.state = activity.state+1;
            activity.updateCircles();

            assertEquals(300, circle1.getLayoutParams().width);
            assertEquals(300, circle1.getLayoutParams().height);
            assertEquals(600, circle2.getLayoutParams().width);
            assertEquals(600, circle2.getLayoutParams().height);
            assertEquals(900, circle3.getLayoutParams().width);
            assertEquals(900, circle3.getLayoutParams().height);
            assertEquals(Utilities.INVISIBLE_CIRCLE, circle4.getLayoutParams().width);
            assertEquals(Utilities.INVISIBLE_CIRCLE, circle4.getLayoutParams().height);

        });
    }
}