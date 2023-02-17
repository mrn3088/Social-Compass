package com.example.socialcompass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintProperties;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

public class ShowMapActivity extends AppCompatActivity {
    private OrientationService orientationService;
    private LocationService locationService;
    private ConstraintLayout compass;
    private ConstraintProperties cp;
    private SharedPreferences preferences;
    private Pair<Double, Double> destination1;
    private String label1;
    private Pair<Double, Double> destination2;
    private String label2;
    private Pair<Double, Double> destination3;
    private String label3;
    private Pair<Double, Double> previousLocation = Pair.create(0.0, 0.0);
    private int manual_rotation;
    private float orientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);
        this.loadProfile();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }

        locationService = LocationService.singleton(this);
        orientationService = OrientationService.singleton(this);
        compass = (ConstraintLayout) findViewById(R.id.compass);
        cp = new ConstraintProperties(compass);
        TextView north = (TextView) findViewById(R.id.North);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.manual_rotation = Integer.valueOf(extras.getString("manual_rotation"));
        }

        if (!(this.manual_rotation >= 0) && (this.manual_rotation <360)) {
            this.reobserveOrientation();
        } else {
            ConstraintLayout.LayoutParams northlayoutparams = (ConstraintLayout.LayoutParams) north.getLayoutParams();
            northlayoutparams.circleAngle = manual_rotation;
        }


        locationService.getLocation().observe(this, new Observer<Pair<Double, Double>>() {
            @Override
            public void onChanged(Pair<Double, Double> currentLocation) {

                if(currentLocation == null){
                    currentLocation = previousLocation;
                }else{
                    previousLocation = currentLocation;
                }

                TextView text1 = (TextView) findViewById(R.id.label1);
                TextView text2 = (TextView) findViewById(R.id.label2);
                TextView text3 = (TextView) findViewById(R.id.label3);

                text1.setText(label1);
                text2.setText(label2);
                text3.setText(label3);

                ConstraintLayout.LayoutParams northlayoutparams = (ConstraintLayout.LayoutParams) north.getLayoutParams();
                ConstraintLayout.LayoutParams label1layoutparams = (ConstraintLayout.LayoutParams) text1.getLayoutParams();
                ConstraintLayout.LayoutParams label2layoutparams = (ConstraintLayout.LayoutParams) text2.getLayoutParams();
                ConstraintLayout.LayoutParams label3layoutparams = (ConstraintLayout.LayoutParams) text3.getLayoutParams();

                float angle1 = Utilities.relativeAngle(currentLocation, destination1);
                float angle2 = Utilities.relativeAngle(currentLocation, destination2);
                float angle3 = Utilities.relativeAngle(currentLocation, destination3);

                float northAngle = northlayoutparams.circleAngle;

                label1layoutparams.circleAngle = ((northAngle+angle1)%360);
                label2layoutparams.circleAngle = ((northAngle+angle2)%360);
                label3layoutparams.circleAngle = ((northAngle+angle3)%360);


            }
        });
    }

    public void reobserveOrientation() {
        TextView north = (TextView) findViewById(R.id.North);

        orientationService.getOrientation().observe(this, new Observer<Float>() {
            @Override
            public void onChanged(Float aFloat) {
                ConstraintLayout.LayoutParams northlayoutparams = (ConstraintLayout.LayoutParams) north.getLayoutParams();
                float degree = 360 - Utilities.radiansToDegreesFloat(aFloat);
                orientation = degree;
                northlayoutparams.circleAngle = degree;

            }
        });
    }
    public void onBackClicked(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        orientationService.onPause();
        locationService.onPause();
        super.onDestroy();
    }

    public void loadProfile(){
        preferences = getSharedPreferences("com.example.socialcompass", MODE_PRIVATE);
        destination1= Pair.create((double) preferences.getFloat("label1Lat", 0f), (double) preferences.getFloat("label1long", 0f));
        destination2 = Pair.create((double) preferences.getFloat("label2Lat", 0f), (double) preferences.getFloat("label2long", 0f));
        destination3 = Pair.create((double) preferences.getFloat("label3Lat", 0f), (double) preferences.getFloat("label3long", 0f));
        label1 = preferences.getString("label1Name", "Label1");
        label2 = preferences.getString("label2Name", "Label2");
        label3 = preferences.getString("label3Name", "Label3");
    }

    public float getOrientation() {return orientation;}


}