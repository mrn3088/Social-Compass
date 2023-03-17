/**
 * This file has tests for User Story 5
 */

package com.example.socialcompass;

import static org.junit.Assert.assertEquals;

import android.util.Pair;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import androidx.test.rule.GrantPermissionRule;

import com.example.socialcompass.activity.ShowMapActivity;
import com.example.socialcompass.entity.Position;
import com.example.socialcompass.service.LocationService;
import com.example.socialcompass.service.OrientationService;
import com.example.socialcompass.utilities.Utilities;

//Story 6: showing distance between users
@RunWith(RobolectricTestRunner.class)
public class MS2Story5Test {

    @Test
    public void calculateDistanceTest(){

        double lat1 = 0.0;
        double long1 = 0.0;
        double lat2 = 90.0;
        double long2 = 90.0;
        double calculatedDistance = 0;
        double expectedDistance = 6218.47;

        calculatedDistance = Utilities.calculateDistance(lat1, long1,lat2,long2);

        assertEquals(expectedDistance, calculatedDistance, 0.02);
    }

    @Test
    public void calculateDistanceTest2(){

        double lat1 = 100.0;
        double long1 = 100.0;
        double lat2 = 90.0;
        double long2 = 90.0;
        double calculatedDistance = 0;
        double expectedDistance = 690.94;

        calculatedDistance = Utilities.calculateDistance(lat1, long1,lat2,long2);

        assertEquals(expectedDistance, calculatedDistance, 0.02);
    }
}
