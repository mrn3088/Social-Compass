/**
 * This is a MockFriendActivity class which is used to add a mocking object
 * into compass which can be used for testing or demoing purposes.
 */

package com.example.socialcompass.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.socialcompass.R;
import com.example.socialcompass.entity.SocialCompassUser;
import com.example.socialcompass.model.SocialCompassDao;
import com.example.socialcompass.model.SocialCompassDatabase;
import com.example.socialcompass.model.SocialCompassRepository;
import com.example.socialcompass.utilities.Utilities;

import java.util.Random;
import java.util.UUID;

public class MockFriendActivity extends AppCompatActivity {
    SocialCompassRepository repo;
    SocialCompassDatabase db;
    SocialCompassDao userDao;
    private static final double EARTH_RADIUS = 6371.0; // Earth's radius in kilometers
    private static final double MILE_TO_KM = 1.60934; // Conversion factor from miles to kilometers
    private double Lat, Long;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock_friend);
        db = SocialCompassDatabase.provide(getApplicationContext());
        userDao = db.getDao();
        repo = new SocialCompassRepository(userDao);
    }

    /**
     * generate a random location for mocking object within a given range.
     * @param latitude
     * @param longitude
     * @param minDistanceInMiles
     * @param maxDistanceInMiles
     * @return
     */
    public static double[] getRandomLocation(double latitude, double longitude, double minDistanceInMiles, double maxDistanceInMiles) {
        double minDistanceInKm = minDistanceInMiles * MILE_TO_KM;
        double maxDistanceInKm = maxDistanceInMiles * MILE_TO_KM;

        // Generate a random angle (in radians) and distance (in kilometers) within the specified range
        Random random = new Random();
        double angle = random.nextDouble() * 2 * Math.PI;
        double distance = minDistanceInKm + random.nextDouble() * (maxDistanceInKm - minDistanceInKm);

        // Calculate the new latitude and longitude
        double newLatitude = latitude + Math.toDegrees(distance / EARTH_RADIUS) * Math.cos(angle);
        double newLongitude = longitude + Math.toDegrees(distance / EARTH_RADIUS) / Math.cos(Math.toRadians(newLatitude)) * Math.sin(angle);

        return new double[]{newLatitude, newLongitude};
    }

    /**
     * method binded to save button
     * @return
     */
    public boolean saveLabel() {
        TextView theLat = findViewById(R.id.MockLatitude);
        TextView theLong = findViewById(R.id.MockLongitude);
        if (theLat.getText().toString().equals("") || theLong.getText().toString().equals("")) {
            Log.d("no name", "no name");

            return false;
        }
        double tmpLat, tmpLong;
        try {
            String lat = theLat.getText().toString();
            String lon = theLong.getText().toString();
            tmpLat = Double.parseDouble(lat);
            tmpLong = Double.parseDouble(lon);
        } catch (Exception e) {
            Log.d("parse error", "parse error");
            return false;
        }
        if (tmpLat > 90 || tmpLat < -90 || tmpLong < -180 || tmpLong > 180) {
            Log.d("invalid range", "invalid range");

            return false;
        }
        Lat = tmpLat;
        Long = tmpLong;
        return true;
    }

    /**
     * The following methods all specify a range to generate mocking object
     * @param view
     */

    public void onClickedOne(View view) {
        TextView myID = findViewById(R.id.mockID);

        if (myID.getText().toString().equals("")) {
            Utilities.displayAlert(this, "Friend ID not entered");
            return;
        }

        if (!saveLabel()) {
            Utilities.displayAlert(this, "Input are not valid");
            return;
        }

        var loc = getRandomLocation(this.Lat, this.Long, 0.4, 0.5);
        String uid = UUID.randomUUID().toString();
        SocialCompassUser friend = new SocialCompassUser(uid, uid, myID.getText().toString(), (float) loc[0], (float) loc[1]);
        try {
            repo.upsertSynced(friend);
        } catch (Exception e) {
            e.printStackTrace();
        }
        myID.setText("");
        Utilities.displayAlert(this, "Successfully mocked friend: " + friend.label + " within 1 miles!");
    }

    public void onClickedTwo(View view) {
        TextView myID = findViewById(R.id.mockID);

        if (myID.getText().toString().equals("")) {
            Utilities.displayAlert(this, "Friend ID not entered");
            return;
        }
        if (!saveLabel()) {
            Utilities.displayAlert(this, "Input are not valid");
            return;
        }
        var loc = getRandomLocation(this.Lat, this.Long, 5, 7);
        String uid = UUID.randomUUID().toString();
        SocialCompassUser friend = new SocialCompassUser(uid, uid, myID.getText().toString(), (float) loc[0], (float) loc[1]);
        try {
            repo.upsertSynced(friend);
        } catch (Exception e) {
            e.printStackTrace();
        }
        myID.setText("");
        Utilities.displayAlert(this, "Successfully mocked friend: " + friend.label + " between 1 to 10!");
    }

    public void onClickedThree(View view) {
        TextView myID = findViewById(R.id.mockID);

        if (myID.getText().toString().equals("")) {
            Utilities.displayAlert(this, "Friend ID not entered");
            return;
        }
        if (!saveLabel()) {
            Utilities.displayAlert(this, "Input are not valid");
            return;
        }
        var loc = getRandomLocation(this.Lat, this.Long, 350, 400);
        String uid = UUID.randomUUID().toString();
        SocialCompassUser friend = new SocialCompassUser(uid, uid, myID.getText().toString(), (float) loc[0], (float) loc[1]);
        try {
            repo.upsertSynced(friend);
        } catch (Exception e) {
            e.printStackTrace();
        }
        myID.setText("");
        Utilities.displayAlert(this, "Successfully mocked friend: " + friend.label + " between 10 to 500 miles!");
    }

    public void onClickedFour(View view) {
        TextView myID = findViewById(R.id.mockID);

        if (myID.getText().toString().equals("")) {
            Utilities.displayAlert(this, "Friend ID not entered");
            return;
        }
        if (!saveLabel()) {
            Utilities.displayAlert(this, "Input are not valid");
            return;
        }
        var loc = getRandomLocation(this.Lat, this.Long, 700, 1000);
        String uid = UUID.randomUUID().toString();
        SocialCompassUser friend = new SocialCompassUser(uid, uid, myID.getText().toString(), (float) loc[0], (float) loc[1]);

        try {
            repo.upsertSynced(friend);
        } catch (Exception e) {
            e.printStackTrace();
        }
        myID.setText("");
        Utilities.displayAlert(this, "Successfully mocked friend: " + friend.label + " further than  500!");
    }

    public void onBackClicked(View view) {
        Intent intent = new Intent(this, ShowMapActivity.class);
        startActivity(intent);
    }
}

