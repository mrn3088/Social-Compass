package com.example.socialcompass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.widget.Button;
import android.widget.TextView;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;
import androidx.test.rule.GrantPermissionRule;

import com.example.socialcompass.activity.ShowMapActivity;
import com.example.socialcompass.activity.ShowMapActivityMocking;
import com.example.socialcompass.entity.Position;
import com.example.socialcompass.service.LocationService;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.util.UUID;

// Story 6: display GPS access status
@RunWith(RobolectricTestRunner.class)
public class MS2Story6Test {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public GrantPermissionRule fRuntimePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule fRuntimePermissionRuleCoarse = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_COARSE_LOCATION);

    @Test
    public void displayGPSStatusTest(){
        var scenario = ActivityScenario.launch(ShowMapActivityMocking.class);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            //var locationService = LocationService.singleton(activity);

            //MutableLiveData<Position> mockLocation = new MutableLiveData<>();
            //locationService.setMockOrientationSource(mockLocation);

            Button gpsButton  = (Button) activity.findViewById(R.id.displayGpsStatus);

            assertEquals("GPS ACTIVE", gpsButton.getText().toString());
            activity.onGpsChanged(65);
            assertEquals("1H", gpsButton.getText().toString());

        });
    }
}