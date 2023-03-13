/**
 * This file has ShowMapActivity class which is used to support
 * the show map page
 */

package com.example.socialcompass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintProperties;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class is ShowMapActivity class used to support show map page
 */
public class ShowMapActivity extends AppCompatActivity {
    private Service orientationService;
    private Service locationService;
    private ConstraintLayout compass;
    private ConstraintProperties cp;
    private SharedPreferences preferences;
    private Position destination1;
    private String label1;
    private Position destination2;
    private String label2;
    private Position destination3;
    private String label3;
    private Position previousLocation = new Position(0, 0);
    private int manual_rotation;
    private float orientation;
    private String uid;

    private Map<String, String> userIDs = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);
        this.loadProfile();

        /*
        Ask for user's permission for location tracking
         */
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }

        uid = getIntent().getStringExtra("uid");

        /*
        Get location service and orientation service object
         */
        locationService = LocationService.singleton(this);
        orientationService = OrientationService.singleton(this);
        compass = (ConstraintLayout) findViewById(R.id.compass);
        cp = new ConstraintProperties(compass);
        TextView north = (TextView) findViewById(R.id.North);

        /*
        Two different modes: Orientation manual setting vs orientation tracking
         */
        if (!((this.manual_rotation >= 0) && (this.manual_rotation < 360))) {
            /*
            Orientation tracking
             */
            this.reobserveOrientation();
        } else {
            /*
            Orientation manual setting
             */
            ConstraintLayout.LayoutParams northlayoutparams = (ConstraintLayout.LayoutParams) north.getLayoutParams();
            northlayoutparams.circleAngle = 360.0f - manual_rotation;
        }
        this.reobserveLocation();

    }

    /**
     * Reobserve the orientation of the device
     */
    public void reobserveLocation() {
        ((LocationService) locationService).getLocation().observe(this, new Observer<Position>() {
            @Override
            public void onChanged(Position currentLocation) {
                TextView north = (TextView) findViewById(R.id.North);

                if (currentLocation == null) {
                    currentLocation = previousLocation;
                } else {
                    previousLocation = currentLocation;
                }

                /*
                Get the view of three coordinate labels
                 */
                TextView text1 = (TextView) findViewById(R.id.label1);
                TextView text2 = (TextView) findViewById(R.id.label2);
                TextView text3 = (TextView) findViewById(R.id.label3);


                ConstraintLayout.LayoutParams northlayoutparams = (ConstraintLayout.LayoutParams) north.getLayoutParams();
                ConstraintLayout.LayoutParams label1layoutparams = (ConstraintLayout.LayoutParams) text1.getLayoutParams();
                ConstraintLayout.LayoutParams label2layoutparams = (ConstraintLayout.LayoutParams) text2.getLayoutParams();
                ConstraintLayout.LayoutParams label3layoutparams = (ConstraintLayout.LayoutParams) text3.getLayoutParams();

                float northAngle = northlayoutparams.circleAngle;

                /*
                Update three coordinate labels according to new orientation
                 */
                if (destination1.getLatitude() > -360f && destination1.getLongitude() > -360f) {
                    /*
                    Set coordinate 1
                     */
                    text1.setText(label1);
                    float angle1 = locationCalculations.relativeAngle(currentLocation, destination1);
                    label1layoutparams.circleAngle = ((northAngle + angle1) % 360);
                } else {
                    /*
                    No input of coordinate 1.
                     */
                    text1.setText("");
                }

                if (destination2.getLatitude() > -360f && destination2.getLongitude() > -360f) {
                    /*
                    Set coordinate 2
                     */
                    text2.setText(label2);
                    float angle2 = locationCalculations.relativeAngle(currentLocation, destination2);
                    label2layoutparams.circleAngle = ((northAngle + angle2) % 360);
                } else {
                    /*
                    No input of coordinate 2
                     */
                    text2.setText("");
                }

                if (destination3.getLatitude() > -360f && destination3.getLongitude() > -360f) {
                    /*
                    Set coordinate 3
                     */
                    text3.setText(label3);
                    float angle3 = locationCalculations.relativeAngle(currentLocation, destination3);
                    label3layoutparams.circleAngle = ((northAngle + angle3) % 360);
                } else {
                    /*
                    No input of coordinate 3
                     */
                    text3.setText("");
                }


            }
        });
    }

    /**
     * Reobserve the orientation of the device
     */
    public void reobserveOrientation() {
        TextView north = (TextView) findViewById(R.id.North);

        ((OrientationService) orientationService).getOrientation().observe(this, new Observer<Float>() {
            @Override
            public void onChanged(Float aFloat) {
                ConstraintLayout.LayoutParams northlayoutparams = (ConstraintLayout.LayoutParams) north.getLayoutParams();
                float degree = 360 - Utilities.radiansToDegreesFloat(aFloat);
                orientation = degree;
                northlayoutparams.circleAngle = degree;

            }
        });
    }

    /**
     * Called when the user taps the Back button
     */
    public void onBackClicked(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onAddFriendsClicked(View view) {
        Intent i = new Intent(this, AddFriendActivity.class);
        i.putExtra("uid", uid);
        startActivity(i);
    }


    @Override
    protected void onDestroy() {
        orientationService.onPause();
        locationService.onPause();
        super.onDestroy();
    }

    /**
     * load the 3 destinations and their labels from the shared preferences
     */
    public void loadProfile() {
        preferences = getSharedPreferences("com.example.socialcompass", MODE_PRIVATE);
//        destination1 = new Position((double) preferences.getFloat("label1Lat", 0f), (double) preferences.getFloat("label1Long", 0f));
//        destination2 = new Position((double) preferences.getFloat("label2Lat", 0f), (double) preferences.getFloat("label2Long", 0f));
//        destination3 = new Position((double) preferences.getFloat("label3Lat", 0f), (double) preferences.getFloat("label3Long", 0f));
//        label1 = preferences.getString("label1Name", "Label1");
//        label2 = preferences.getString("label2Name", "Label2");
//        label3 = preferences.getString("label3Name", "Label3");
        loadUsers();
        manual_rotation = -1;
        try {
            manual_rotation = Integer.parseInt(preferences.getString("manual_rotation", "-1"));
        } catch (NumberFormatException e) {
        }
    }

    /**
     * get the orientation of the device
     *
     * @return orientation
     */
    public float getOrientation() {
        return orientation;
    }

    /**
     * get the previous location of the device
     *
     * @return previousLocation
     */
    public Position getPreviousLocation() {
        return previousLocation;
    }

    /**
     * For test use only
     *
     * @param latitude, longitude
     */
    public void setDestination1(Double latitude, Double longitude) {
        destination1 = new Position(latitude, longitude);
    }

    /**
     * For test use only
     *
     * @param orientation
     */
    public void setOrientation(float orientation) {
        this.orientation = orientation;
    }

    public void addNewUserView(float angle, int radius, String str, String public_code) {
        // Get a reference to the ConstraintLayout
        ConstraintLayout constraintLayout = this.findViewById(R.id.compass);

// Inflate a new instance of the TextView using label_template as a template
        TextView newTextView = new TextView(this);
        newTextView.setText(str);
        newTextView.setRotation(0);
        var id = View.generateViewId();
        userIDs.put("" + id, public_code);
        newTextView.setId(id);
        newTextView.setTextSize(20);
        constraintLayout.addView(newTextView);
        ConstraintLayout.LayoutParams layoutparams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        newTextView.setLayoutParams(layoutparams);
        ConstraintSet cons = new ConstraintSet();
        cons.clone(constraintLayout);
        cons.constrainCircle(newTextView.getId(),
                R.id.compass,
                radius,
                angle
        );
        cons.applyTo(constraintLayout);
    }

    // return
    private Pair<Float, Integer> calculateLocation(float x, float y) {
        return new Pair<>(0.0f, 0);
    }

    private void updateUserView(TextView view, SocialCompassUser user) {
        var theLoc = calculateLocation(user.getLatitude(), user.getLongitude());
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
        layoutParams.circleAngle = theLoc.first;
        layoutParams.circleRadius = theLoc.second;
    }

    private void loadUsers() {
        var db = SocialCompassDatabase.provide(this.getApplicationContext());
        var dao = db.getDao();
        var users = dao.getAll().getValue();
        var api = SocialCompassAPI.provide();
        if (userIDs.isEmpty()) {
            for (var user : users) {
                var theLoc = calculateLocation(user.getLatitude(), user.getLongitude());
                addNewUserView(theLoc.first, theLoc.second, user.getLabel(), user.private_code);
            }
        } else {
            for (var id : userIDs.keySet()) {
                int idInt = Integer.parseInt(id);
                TextView userView = findViewById(idInt);
                String publicCode = userIDs.get(id);
                SocialCompassUser theUser;
                try {
                    theUser = api.getUser(publicCode);
                    updateUserView(userView, theUser);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}