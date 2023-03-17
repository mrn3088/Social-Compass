package com.example.socialcompass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.widget.Button;
import android.widget.TextView;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.rule.GrantPermissionRule;

import com.example.socialcompass.entity.SocialCompassUser;
import com.example.socialcompass.model.SocialCompassAPI;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.util.UUID;

// Story 1: exchanging User IDs
@RunWith(RobolectricTestRunner.class)
public class MS2Story1Test {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public GrantPermissionRule fRuntimePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule fRuntimePermissionRuleCoarse = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_COARSE_LOCATION);

    @Test
    public void userIdTest(){
        SocialCompassAPI api = SocialCompassAPI.provide();
        SocialCompassAPI.setServerURL("https://socialcompass.goto.ucsd.edu/");
        String privateID1 = UUID.randomUUID().toString();
        String privateID2 = UUID.randomUUID().toString();
        String publicID1 = UUID.randomUUID().toString();
        String publicID2 = UUID.randomUUID().toString();

        SocialCompassUser theUser1 = new SocialCompassUser(privateID1, publicID1, "1", 0.0f, 0.0f);
        SocialCompassUser theUser2 = new SocialCompassUser(privateID2, publicID2, "2",0.0f, 0.0f);

        try {
            api.addUser(theUser1);
            api.addUser(theUser2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SocialCompassUser newUser1 = null;
        SocialCompassUser newUser2 = null;
        try {
            newUser1 = api.getUser(publicID1);
            newUser2 = api.getUser(publicID2);

        }catch (IOException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        assertEquals(newUser1.label, theUser1.label);
        assertEquals(newUser2.label, theUser2.label);


    }
}