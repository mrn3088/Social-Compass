/**
 * This file has tests for location service
 */

package com.example.socialcompass;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.assertEquals;

import android.widget.Button;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import androidx.test.rule.GrantPermissionRule;

import com.example.socialcompass.activity.MainActivity;
import com.example.socialcompass.activity.ShowMapActivity;
import com.example.socialcompass.entity.Position;
import com.example.socialcompass.entity.SocialCompassUser;
import com.example.socialcompass.model.SocialCompassAPI;
import com.example.socialcompass.service.LocationService;
import com.example.socialcompass.service.OrientationService;
import com.example.socialcompass.utilities.Utilities;

import java.util.UUID;

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
    public void locationTest() {
        Position testValue = new Position(35.006, 78.5546);
        var scenario = ActivityScenario.launch(ShowMapActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {

            SocialCompassAPI api = SocialCompassAPI.provide();
            SocialCompassAPI.setServerURL("https://socialcompass.goto.ucsd.edu/");
            String privateID1 = UUID.randomUUID().toString();
            String publicID1 = UUID.randomUUID().toString();

            SocialCompassUser theUser1 = new SocialCompassUser(privateID1, publicID1, "1", 50.0f, 50.0f);

            try {
                api.addUser(theUser1);
            } catch (Exception e) {
                e.printStackTrace();
            }

            activity.setUserInfo(publicID1, privateID1, "1");

            var locationService = LocationService.singleton(activity);

            MutableLiveData<Position> mockLocation = new MutableLiveData<>();
            locationService.setMockOrientationSource(mockLocation);

            mockLocation.setValue(testValue);
            activity.reobserveLocation();

            var expected = testValue;
            var observed = activity.getPreviousLocation();
            assertEquals(expected.getLatitude(), observed.getLatitude(), 0.002);
            assertEquals(expected.getLongitude(), observed.getLongitude(), 0.002);
        });
    }


}
