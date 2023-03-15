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
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.internal.Util;

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

    private int state = 2;

    private int dpscale = 450;

    private int distanceScale = 500;

    private Position current = new Position(60, -130);


    private Position previousLocation = new Position(0, 0);
    private int manual_rotation;
    private float orientation;
    private String uid;

    private Map<String, String> userIDs = new HashMap<>();
    private Map<String, String> textID2imageID = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);
        uid = getIntent().getStringExtra("uid");
        var api = SocialCompassAPI.provide();
        SocialCompassUser selfUser = null;
        try {
            selfUser = api.getUser(uid);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (selfUser != null) {

        }
        var db = SocialCompassDatabase.provide(this.getApplicationContext());
        var dao = db.getDao();
//        dao.upsert(new SocialCompassUser("3117", "3117", "Ruinan—Ma", 60.5f, -130.5f));
//        try {
//            api.addUser(new SocialCompassUser("3117", "3117", "Ruinan—Ma", 60.5f, -130.5f));
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        this.loadProfile();

        /*
        Ask for user's permission for location tracking
         */
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }

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

                current = currentLocation;

                Log.d("observeLocations", "entered this");
                var api = SocialCompassAPI.provide();

                updateCircles();
                for (var id : userIDs.keySet()) {
                    int idInt = Integer.parseInt(id);
                    TextView userView = findViewById(idInt);
                    String publicCode = userIDs.get(id);
                    SocialCompassUser theUser;
                    try {
                        theUser = api.getUser(publicCode);
                        updateUserView(id, theUser);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    private void updateCircles() {
        ImageView circle1 = findViewById(R.id.circle1);
        ImageView circle2 = findViewById(R.id.circle2);
        ImageView circle3 = findViewById(R.id.circle3);
        ImageView circle4 = findViewById(R.id.circle4);

        if(state==1){
            circle1.getLayoutParams().width = 900;
            circle1.getLayoutParams().height = 900;
            circle2.getLayoutParams().width = Utilities.INVISIBLE_CIRCLE;
            circle2.getLayoutParams().height = Utilities.INVISIBLE_CIRCLE;
            circle3.getLayoutParams().width = Utilities.INVISIBLE_CIRCLE;
            circle3.getLayoutParams().height = Utilities.INVISIBLE_CIRCLE;
            circle4.getLayoutParams().width = Utilities.INVISIBLE_CIRCLE;
            circle4.getLayoutParams().height = Utilities.INVISIBLE_CIRCLE;
        }else if(state==2){
            circle1.getLayoutParams().width = 900/2;
            circle1.getLayoutParams().height = 900/2;
            circle2.getLayoutParams().width = 900;
            circle2.getLayoutParams().height = 900;
            circle3.getLayoutParams().width = Utilities.INVISIBLE_CIRCLE;
            circle3.getLayoutParams().height = Utilities.INVISIBLE_CIRCLE;
            circle4.getLayoutParams().width = Utilities.INVISIBLE_CIRCLE;
            circle4.getLayoutParams().height = Utilities.INVISIBLE_CIRCLE;

        }else if(state==3){
            circle1.getLayoutParams().width = 300;
            circle1.getLayoutParams().height = 300;
            circle2.getLayoutParams().width = 600;
            circle2.getLayoutParams().height = 600;
            circle3.getLayoutParams().width = 900;
            circle3.getLayoutParams().height = 900;
            circle4.getLayoutParams().width = Utilities.INVISIBLE_CIRCLE;
            circle4.getLayoutParams().height = Utilities.INVISIBLE_CIRCLE;
        }else{
            circle1.getLayoutParams().width = 300;
            circle1.getLayoutParams().height = 300;
            circle2.getLayoutParams().width = 500;
            circle2.getLayoutParams().height = 500;
            circle3.getLayoutParams().width = 700;
            circle3.getLayoutParams().height = 700;
            circle4.getLayoutParams().width = 900;
            circle4.getLayoutParams().height = 900;
        }
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
        var db = SocialCompassDatabase.provide(this.getApplicationContext());
        var dao = db.getDao();
        var userList = dao.getAll();
        userList.observe(this, this::loadUsers);

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
        ImageView newImageView = new ImageView(this);
        newTextView.setText(str);
        newTextView.setRotation(0);
        newImageView.setImageResource(R.drawable.user_icon);
        newImageView.setRotation(0);
        var textViewID = View.generateViewId();
        var imageViewID = View.generateViewId();
        userIDs.put(Integer.toString(textViewID), public_code);
        textID2imageID.put(Integer.toString(textViewID), Integer.toString(imageViewID));
        newTextView.setId(textViewID);
        newImageView.setId(imageViewID);
        newTextView.setTextSize(20);
        constraintLayout.addView(newTextView);
        constraintLayout.addView(newImageView);
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
        cons.constrainCircle(newImageView.getId(),
                R.id.compass,
                radius,
                angle
        );
        cons.applyTo(constraintLayout);
    }

    // return
    private Pair<Float, Integer> calculateLocation(float x, float y) {
        double distance = Utilities.calculateDistance(current.getLatitude(), current.getLongitude(), x, y);
//        Integer radius = (int) (dpscale * distance / distanceScale);
        Integer radius = (int) Utilities.calculateUserViewRadius(distance, this.state);
        Float relativeAngle = Utilities.relativeAngleUtils(current.getLatitude(), current.getLongitude(), (double) x, (double) y);
        TextView north = (TextView) findViewById(R.id.North);
        ConstraintLayout.LayoutParams northlayoutparams = (ConstraintLayout.LayoutParams) north.getLayoutParams();

        Float northAngle = northlayoutparams.circleAngle;

        if (radius > 450) {
            radius = 450;
        }

        Log.d("Distance", Double.toString(distance));
        Log.d("current lat", Double.toString(current.getLatitude()));
        Log.d("current long", Double.toString(current.getLongitude()));
        Log.d("x", Float.toString(x));
        Log.d("y", Float.toString(y));
        Log.d("dp", Integer.toString(radius));

        return new Pair<>((relativeAngle + northAngle) % 360, radius);
    }

    private void updateUserView(String textViewID, SocialCompassUser user) {
        TextView textView = findViewById(Integer.parseInt(textViewID));
        ImageView imageView = findViewById(Integer.parseInt(textID2imageID.get(textViewID)));
        ConstraintLayout constraintLayout = this.findViewById(R.id.compass);
        var theLoc = calculateLocation(user.getLatitude(), user.getLongitude());
        if (theLoc.second == Utilities.DISPLAY_MARGIN) {
            textView.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.INVISIBLE);
        }
        ConstraintSet cons = new ConstraintSet();
        cons.clone(constraintLayout);
        cons.constrainCircle(textView.getId(),
                R.id.compass,
                theLoc.second,
                theLoc.first
        );
        cons.constrainCircle(imageView.getId(),
                R.id.compass,
                theLoc.second,
                theLoc.first
        );
        cons.applyTo(constraintLayout);

        Log.d("updated", Integer.toString(theLoc.second));
    }

    public void onZoomInClicked(View view) {
        if (state > 1 && state <= 4) {
            state--;
            if (state == 1) {
                this.distanceScale = 1;
            } else if (state == 2) {
                this.distanceScale = 10;
            } else if (state == 3) {
                this.distanceScale = 100;
            } else {
                this.distanceScale = 500;
            }
            this.reobserveLocation();
        } else {
            Utilities.displayAlert(this, "Cannot zoom in more!");
        }
    }

    public void onZoomOutClicked(View view) {
        if (state >= 1 && state < 4) {
            state++;
            if (state == 1) {
                this.distanceScale = 1;
            } else if (state == 2) {
                this.distanceScale = 10;
            } else if (state == 3) {
                this.distanceScale = 100;
            } else {
                this.distanceScale = 500;
            }
            this.reobserveLocation();
        } else {
            Utilities.displayAlert(this, "Cannot zoom out more!");
        }
    }

    private void loadUsers(List<SocialCompassUser> users) {
        var api = SocialCompassAPI.provide();
        if (userIDs.isEmpty()) {
            for (var user : users) {
                Log.d("code", user.public_code);
                var theLoc = calculateLocation(user.getLatitude(), user.getLongitude());
                addNewUserView(theLoc.first, theLoc.second, user.getLabel(), user.private_code);
            }
        } else {
            for (var id : userIDs.keySet()) {
                int idInt = Integer.parseInt(id);
                String publicCode = userIDs.get(id);
                SocialCompassUser theUser;
                try {
                    theUser = api.getUser(publicCode);
                    updateUserView(id, theUser);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}