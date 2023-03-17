package com.example.socialcompass;

import static org.junit.Assert.assertEquals;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;
import androidx.test.rule.GrantPermissionRule;

import com.example.socialcompass.activity.ShowMapActivity;
import com.example.socialcompass.activity.ShowMapActivityMocking;
import com.example.socialcompass.entity.Position;
import com.example.socialcompass.entity.SocialCompassUser;
import com.example.socialcompass.model.SocialCompassAPI;
import com.example.socialcompass.service.LocationService;
import com.example.socialcompass.utilities.Utilities;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.UUID;

//Story 2: track user locations
@RunWith(RobolectricTestRunner.class)
public class MS2Story2Test {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public GrantPermissionRule fRuntimePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule fRuntimePermissionRuleCoarse = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_COARSE_LOCATION);

    /**
     * Test that location can be updated correctly
     */
    @Test
    public void locationUpdatesWithChange() {
        Position testValue = new Position(35.006, 78.5546);
        Position testValueTwo = new Position(56.9987, 10.002);
        Position testValueThree = new Position(34.555, 45.6676);

        var scenario = ActivityScenario.launch(ShowMapActivityMocking.class);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {

            SocialCompassAPI api = SocialCompassAPI.provide();
            SocialCompassAPI.setServerURL("https://socialcompass.goto.ucsd.edu/");
            String privateID1 = UUID.randomUUID().toString();
            String publicID1 = UUID.randomUUID().toString();

            SocialCompassUser theUser1 = new SocialCompassUser(privateID1, publicID1, "1", 0.0f, 0.0f);

            try {
                api.addUser(theUser1);
            } catch (Exception e) {
                e.printStackTrace();
            }

            activity.setUserInfo(publicID1, privateID1, "1");
            var locationService = LocationService.singleton(activity);
            activity.getOrientation();
            var mockLocation = new MutableLiveData<Position>();
            locationService.setMockOrientationSource(mockLocation);

            mockLocation.setValue(testValue);
            mockLocation.setValue(testValueTwo);
            mockLocation.setValue(testValueThree);
            activity.reobserveLocationMocking();

            var expected = testValueThree;
            var observed = activity.getPreviousLocation(); //remember to update previousLocation in showMapActivity
            assertEquals(expected.getLongitude(), observed.getLongitude(), 0.002);
            assertEquals(expected.getLatitude(), observed.getLatitude(), 0.002);


        });
    }

}
