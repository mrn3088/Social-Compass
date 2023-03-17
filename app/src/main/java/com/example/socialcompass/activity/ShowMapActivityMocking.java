/**
 * This file has ShowMapActivity class which is used to support
 * the show map page
 */

package com.example.socialcompass.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintProperties;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.socialcompass.service.LocationService;
import com.example.socialcompass.service.OrientationService;
import com.example.socialcompass.entity.Position;
import com.example.socialcompass.R;
import com.example.socialcompass.service.Service;
import com.example.socialcompass.model.SocialCompassAPI;
import com.example.socialcompass.entity.SocialCompassUser;
import com.example.socialcompass.viewmodel.SocialCompassViewModel;
import com.example.socialcompass.utilities.Utilities;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


/**
 * This class is ShowMapActivity class used to support show map page
 * @invariant user must have an active registered profile
 */

public class ShowMapActivityMocking extends AppCompatActivity {
    private Service orientationService;
    private Service locationService;
    private ConstraintLayout compass;
    private ConstraintProperties cp;
    private SharedPreferences preferences;

    public int state = 2;

    private Position current = new Position(60, -130);


    private Position previousLocation = new Position(0, 0);
    private float orientation;
    private String uid;

    private String label;

    private String private_code;

    private Map<String, String> userIDs = new HashMap<>();
    private Map<String, String> textID2imageID = new HashMap<>();

    public SocialCompassViewModel viewmodel;

    private Map<String, String> userLabels = new HashMap<>();

    private static final int MAX_TRUNCATED_LENGTH = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);
        //setUpViewModel();
        preferences = getSharedPreferences("com.example.socialcompass", MODE_PRIVATE);
        uid = preferences.getString("uid", "");
        private_code = preferences.getString("private_code", "");
        label = preferences.getString("name", "");
        var serverURL = preferences.getString("server_url", "");
        SocialCompassAPI.provide().useURL(serverURL);
        Log.d("get public", uid);
        Log.d("get private", private_code);

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
        updateCircles();
        trackGps();

        //this.reobserveLocation();
        this.reobserveOrientation();
        try {
            refreshPositions();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Method to set up view model
     */
    public void setUpViewModel(){
        this.viewmodel = new ViewModelProvider(this).get(SocialCompassViewModel.class);
    }

    /**
     * method to check for collision for a given user ID
     * @param labelID
     */
    private void checkCollisions(int labelID) {
        Rect rect1 = new Rect();
        Rect rect2 = new Rect();
        boolean changedLabel = false;
        TextView labelOne = findViewById(labelID);
        // look through all labels currently on screen
        for (String label2ID : textID2imageID.keySet()) {
            // makes sure we don't track a label as colliding with itself
            if (!label2ID.equals(Integer.toString(labelID))) {
                // get textview of labels we are currently looking at

                TextView labelTwo = findViewById(Integer.parseInt(label2ID));

                // set rect1, rect2, to point at the rectangles created by the textviews
                labelOne.getGlobalVisibleRect(rect1);
                labelTwo.getGlobalVisibleRect(rect2);

                // must update imageView's associated with labels as well as labels themselves
                // checks if rectangles created by labels collide with each other
                if (Rect.intersects(rect1, rect2)) {
                    changedLabel = true;
                    Log.d("COLLISION", "labels DO collide");
                    Log.d("VISIBILITY", "set labels to invisible");
                    Log.d("ID", Integer.toString(labelID));
                    Log.d("ID2", label2ID);
                    // if labels collide wipe text off screen
                    float angle1 = ((ConstraintLayout.LayoutParams)labelOne.getLayoutParams()).circleAngle;
                    float angle2 = ((ConstraintLayout.LayoutParams)labelTwo.getLayoutParams()).circleAngle;
                    if (Math.abs(angle1-angle2)> 5){

                        useTruncateLabel(labelOne);
                    } else {

                        useTruncateLabel(labelOne);
//                        stackLabel(labelOne, labelTwo);

                    }
                    break;
                }
            }
        }
        if (!changedLabel) {
            useFullLabel(labelOne);
        }
    }

    /**
     * Method to truncate labels
     * @param view
     */
    private void useTruncateLabel(TextView view) {
        String originalText = view.getText().toString();
        view.setText(originalText.substring(0, Math.min(MAX_TRUNCATED_LENGTH, originalText.length())));
    }

    /**
     * Method to untruncate labels
     * @param view
     */
    private void useFullLabel(TextView view) {
        String originalText = userLabels.get(String.valueOf(view.getId()));
        view.setText(originalText);
    }


    /**
     * Method to stack close labels
     * @param label1
     * @param label2
     */
    public void stackLabel(TextView label1, TextView label2){
        float angle1 = ((ConstraintLayout.LayoutParams)label1.getLayoutParams()).circleAngle;
        float angle2 = ((ConstraintLayout.LayoutParams)label2.getLayoutParams()).circleAngle;
        float y_coordinate1 = ((ConstraintLayout.LayoutParams)label1.getLayoutParams()).circleRadius;
        float y_coordinate2 = ((ConstraintLayout.LayoutParams)label2.getLayoutParams()).circleRadius;
        TextView upper = null;
        TextView lower = null;
        if(y_coordinate1>y_coordinate2){
            upper = label1;
            lower = label2;
        }else{
            lower = label1;
            upper = label2;
        }
        ((ConstraintLayout.LayoutParams)upper.getLayoutParams()).circleRadius+=40;
        ((ConstraintLayout.LayoutParams)upper.getLayoutParams()).circleAngle+=20;
        ((ConstraintLayout.LayoutParams)lower.getLayoutParams()).circleRadius-=40;
        ((ConstraintLayout.LayoutParams)lower.getLayoutParams()).circleAngle-=20;

    }

    /**
     * Method to refresh users in local database from remote server
     * @throws IOException
     * @throws InterruptedException
     */
    private void refreshPositions() throws IOException, InterruptedException {
        ScheduledFuture<?> poller;
        ScheduledExecutorService schedular = Executors.newScheduledThreadPool(1);
        poller = schedular.scheduleAtFixedRate(() -> {

            LiveData<List<SocialCompassUser>> allFriends = viewmodel.getAllUserLocal();
            List<SocialCompassUser> friendList = allFriends.getValue();
            for (SocialCompassUser friend : friendList) {
                String currID = friend.public_code;
                try {
                    //dao.upsert(repo.getRemote(currID).getValue());
                    viewmodel.getUserRemote(currID).observe(this, theUser->{
                        viewmodel.upsert(theUser);
                    });
                } catch (Exception e) {
                    Log.d("EXC", e.toString());
                }
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

    /**
     * Method to track GPS signal
     */
    private void trackGps() {
        // create a poller that will every minute see if we still have gps access
        // if the poller returns that we do not have gps access, increment secondsNoGps by 60
        // call on method to display secondsnoGps to user
        ScheduledFuture<?> poller;
        ScheduledExecutorService schedular = Executors.newScheduledThreadPool(1);
        poller = schedular.scheduleAtFixedRate(() -> {
            LocationService newService = (LocationService) locationService;
            long timeSinceUpdateMillis = newService.timeSinceGpsUpdate();
            int timeSinceUpdateMinutes = (int) timeSinceUpdateMillis / (1000 * 60);
            runOnUiThread(() -> {
                onGpsChanged(timeSinceUpdateMinutes);
            });
        }, 0, 3, TimeUnit.SECONDS);
    }

    /**
     * Method to display GPS status
     * @param minutesNoGps
     */
    public void onGpsChanged(int minutesNoGps) {
        Button gpsButton  = (Button) findViewById(R.id.displayGpsStatus);
        if ((minutesNoGps > 0) && (minutesNoGps < 60)) {
            gpsButton.setText("" + minutesNoGps + "M");
        } else if (minutesNoGps > 60) {
            int hoursSince = minutesNoGps/60;
            gpsButton.setText("" + hoursSince + "H");
        } else {
            gpsButton.setText("GPS ACTIVE");
        }
    }

    /**
     * Reobserve the orientation of the device
     */
    public void reobserveLocation() {
        ((LocationService) locationService).getLocation().observe(this, new Observer<Position>() {
            @Override
            public void onChanged(Position currentLocation) {

                current = currentLocation;

                previousLocation = currentLocation;

                try {
                    viewmodel.upsertRemote(new SocialCompassUser(private_code, uid, label, (float) currentLocation.getLatitude(), (float) currentLocation.getLongitude()));
                    Log.d("Public code", uid);
                    Log.d("Private code", private_code);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("observeLocations", "entered this");

                updateCircles();

                for (var id : userIDs.keySet()) {
                    int idInt = Integer.parseInt(id);
                    String publicCode = userIDs.get(id);
                    try {
                        viewmodel.getUserSynced(publicCode).observeForever(theUsers->{
                            updateUserView(id, theUsers);
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }


    /**
     * Method to update circles according to state
     */
    public void updateCircles() {
        ImageView circle1 = findViewById(R.id.circle1);
        ImageView circle2 = findViewById(R.id.circle2);
        ImageView circle3 = findViewById(R.id.circle3);
        ImageView circle4 = findViewById(R.id.circle4);

        if(state==1){
            // state 1
            circle1.getLayoutParams().width = 900;
            circle1.getLayoutParams().height = 900;
            circle2.getLayoutParams().width = Utilities.INVISIBLE_CIRCLE;
            circle2.getLayoutParams().height = Utilities.INVISIBLE_CIRCLE;
            circle3.getLayoutParams().width = Utilities.INVISIBLE_CIRCLE;
            circle3.getLayoutParams().height = Utilities.INVISIBLE_CIRCLE;
            circle4.getLayoutParams().width = Utilities.INVISIBLE_CIRCLE;
            circle4.getLayoutParams().height = Utilities.INVISIBLE_CIRCLE;
        }else if(state==2){
            //state 2
            circle1.getLayoutParams().width = 900/2;
            circle1.getLayoutParams().height = 900/2;
            circle2.getLayoutParams().width = 900;
            circle2.getLayoutParams().height = 900;
            circle3.getLayoutParams().width = Utilities.INVISIBLE_CIRCLE;
            circle3.getLayoutParams().height = Utilities.INVISIBLE_CIRCLE;
            circle4.getLayoutParams().width = Utilities.INVISIBLE_CIRCLE;
            circle4.getLayoutParams().height = Utilities.INVISIBLE_CIRCLE;

        }else if(state==3){
            //state 3
            circle1.getLayoutParams().width = 300;
            circle1.getLayoutParams().height = 300;
            circle2.getLayoutParams().width = 600;
            circle2.getLayoutParams().height = 600;
            circle3.getLayoutParams().width = 900;
            circle3.getLayoutParams().height = 900;
            circle4.getLayoutParams().width = Utilities.INVISIBLE_CIRCLE;
            circle4.getLayoutParams().height = Utilities.INVISIBLE_CIRCLE;
        }else{
            //state 4
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
     * binded to add friend button, go to add friend activity
     * @param view
     */
    public void onAddFriendsClicked(View view) {
        Intent i = new Intent(this, AddFriendActivity.class);
        i.putExtra("uid", uid);
        startActivity(i);
    }

    /**
     * binded to add mock friend button, go to mock friend activity
     * @param view
     */
    public void onMockFriendsClicked(View view) {
        Intent i = new Intent(this, MockFriendActivity.class);
        startActivity(i);
    }


    @Override
    protected void onDestroy() {
        orientationService.onPause();
        locationService.onPause();
        super.onDestroy();
    }

    /**
     * load user's friends and their labels from the shared preferences
     *
     *
     */
    public void loadProfile() {
        preferences = getSharedPreferences("com.example.socialcompass", MODE_PRIVATE);
        //var userList = viewmodel.getAllUserLocal();
        //userList.observe(this, this::loadUsers);
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
        userLabels.put(Integer.toString(textViewID), str);
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
        checkCollisions(textViewID);
    }

    /**
     * calculate a user's location on the screen, radius and orientation angle
     * @param x
     * @param y
     * @return
     */
    // return
    private Pair<Float, Integer> calculateLocation(float x, float y) {
        double distance = Utilities.calculateDistance(current.getLatitude(), current.getLongitude(), x, y);
        int radius = (int) Utilities.calculateUserViewRadius(distance, this.state);
        Float relativeAngle = Utilities.relativeAngleUtils(current.getLatitude(), current.getLongitude(), (double) x, (double) y);
        TextView north = (TextView) findViewById(R.id.North);
        ConstraintLayout.LayoutParams northlayoutparams = (ConstraintLayout.LayoutParams) north.getLayoutParams();

        Float northAngle = northlayoutparams.circleAngle;

        return new Pair<>((relativeAngle + northAngle) % 360, radius);
    }


    /**
     * update user's text view according to remote server data
     * @param textViewID
     * @param user
     */
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
            checkCollisions(Integer.parseInt(textViewID));
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
    }

    /**
     * Method binded to zoom in button
     * @param view
     */
    public void onZoomInClicked(View view) {
        if (state > 1 && state <= 4) {
            state--;
            updateCircles();
            this.reobserveLocation();
        } else {
            Utilities.displayAlert(this, "Cannot zoom in more!");
        }
    }

    /**
     * Method binded to zoom out button
     * @param view
     */
    public void onZoomOutClicked(View view) {
        if (state >= 1 && state < 4) {
            state++;
            updateCircles();
            this.reobserveLocation();
        } else {
            Utilities.displayAlert(this, "Cannot zoom out more!");
        }
    }

    /**
     * Method to load all friends onto the screen
     * @param users
     */
    private void loadUsers(List<SocialCompassUser> users) {
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
                try {
                    viewmodel.getUserSynced(publicCode).observeForever(theUsers->{
                        updateUserView(id, theUsers);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //For testing purpose
    public void setUserInfo(String uid, String private_code, String label){
        this.uid = uid;
        this.private_code = private_code;
        this.label = label;
    }

    public void reobserveLocationMocking() {
        ((LocationService) locationService).getLocation().observe(this, new Observer<Position>() {
            @Override
            public void onChanged(Position currentLocation) {

                current = currentLocation;

                previousLocation = currentLocation;

                try {
                    viewmodel.upsertRemote(new SocialCompassUser(private_code, uid, label, (float) currentLocation.getLatitude(), (float) currentLocation.getLongitude()));
                    Log.d("Public code", uid);
                    Log.d("Private code", private_code);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("observeLocations", "entered this");

                updateCircles();

                for (var id : userIDs.keySet()) {
                    int idInt = Integer.parseInt(id);
                    String publicCode = userIDs.get(id);
                    try {
                        viewmodel.getUserSynced(publicCode).observeForever(theUsers->{
                            updateUserView(id, theUsers);
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }
}