package com.example.socialcompass;

import static org.junit.Assert.assertEquals;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.rule.GrantPermissionRule;

import com.example.socialcompass.activity.MainActivity;
import com.example.socialcompass.model.SocialCompassAPI;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

//Story 4: Setting up and Display user name
@RunWith(RobolectricTestRunner.class)
public class MS2Story4Test {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public GrantPermissionRule fRuntimePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule fRuntimePermissionRuleCoarse = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_COARSE_LOCATION);

    @Test
    public void userNameDisplayTest() throws Exception {
        var scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        AtomicReference<String> ID = new AtomicReference<>("");
        String name = "Robbisaurus Rex";

        scenario.onActivity(activity -> {
            EditText nameEntry = (EditText) activity.findViewById(R.id.displayName);
            nameEntry.setText(name);
            Button submission = (Button) activity.findViewById(R.id.submit_labels);
            submission.performClick();
            TextView publicCode = (TextView) activity.findViewById(R.id.publicCode);
            ID.set(publicCode.getText().toString());
        });
        scenario.close();
        var api = new SocialCompassAPI();
        api = api.provide();
        var user = api.getUser(ID.get());

        assertEquals(name, user.label);
    }
}
