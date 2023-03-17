package com.example.socialcompass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;
import androidx.test.rule.GrantPermissionRule;

import com.example.socialcompass.activity.AddFriendActivity;
import com.example.socialcompass.activity.MainActivity;
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

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

// A test for MS2 that check all the activities we have
@RunWith(RobolectricTestRunner.class)
public class milestone2Test {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public GrantPermissionRule fRuntimePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule fRuntimePermissionRuleCoarse = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_COARSE_LOCATION);

    @Test
    public void ms2Test(){

        AtomicReference<String> publicID1 = new AtomicReference<String>("");
        AtomicReference<String> publicID2 = new AtomicReference<String>("");


        var scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {

            EditText nameEntry = (EditText) activity.findViewById(R.id.displayName);
            nameEntry.setText("user name");
            Button submit = activity.findViewById(R.id.submit_labels);
            submit.performClick();
            TextView publicCode = (TextView) activity.findViewById(R.id.publicCode);
            publicID1.set(publicCode.getText().toString());

            assertNotEquals("", publicID1.get());

            EditText nameEntry2 = (EditText) activity.findViewById(R.id.displayName);
            nameEntry.setText("friend name");
            activity.onSubmitLabelsClicked(submit);
            publicID2.set(publicCode.getText().toString());

            assertNotEquals("", publicID2.get());

            assertNotEquals(publicID1.get(), publicID2.get());

        });

        /*
        var scenario2 = ActivityScenario.launch(AddFriendActivity.class);
        scenario2.moveToState(Lifecycle.State.STARTED);
        scenario2.onActivity(activity -> {
            TextView myId = activity.findViewById(R.id.usersID);
            myId.setText(publicID1.get());
            TextView friendId = activity.findViewById(R.id.friendID);
            friendId.setText(publicID2.get());

            try{
                Button saveB = activity.findViewById(R.id.saveB);
                activity.onSaveClicked(saveB);
            }catch(Exception e){
                assertTrue(true);
            }

            assert(activity.getUserDao().exists(publicID2.get()));


        });
         */

        /*

        var scenario3 = ActivityScenario.launch(ShowMapActivityMocking.class);
        scenario3.moveToState(Lifecycle.State.STARTED);
        scenario3.onActivity(activity -> {

            //1st part: test location
            SocialCompassAPI api = SocialCompassAPI.provide();
            SocialCompassAPI.setServerURL("https://socialcompass.goto.ucsd.edu/");
            SocialCompassUser user1;
            SocialCompassUser user2;
            try {
                user1 = api.getUser(publicID1.get());
                user2 = api.getUser(publicID1.get());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            activity.setUserInfo(user1.public_code, user1.private_code, user1.label);

            Position testValue = new Position(20.0, 20.0);
            var locationService = LocationService.singleton(activity);
            activity.getOrientation();
            var mockLocation = new MutableLiveData<Position>();
            locationService.setMockOrientationSource(mockLocation);
            mockLocation.setValue(testValue);
            activity.reobserveLocation();

            var observed = activity.getPreviousLocation(); //remember to update previousLocation in showMapActivity
            //assertEquals(testValue.getLongitude(), observed.getLongitude(), 0.002);
            //assertEquals(testValue.getLatitude(), observed.getLatitude(), 0.002);
            //activity.setUpViewModel();
            //2nd part: test user and friend distance


            try {
                activity.viewmodel.upsertRemote(new SocialCompassUser(user1.private_code, user1.public_code, "user1", 20.0f, 20.0f));
                activity.viewmodel.upsertRemote(new SocialCompassUser(user2.private_code, user2.public_code, "user2", 25.0f, 25.0f));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


            double distanceWithFriend = Utilities.calculateDistance(user1.latitude, user1.longitude, user2.latitude, user2.longitude);
            //assertEquals(33, distanceWithFriend, 0.02);


        });
         */

    }
}