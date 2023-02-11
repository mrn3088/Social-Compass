package com.example.socialcompass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintProperties;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.widget.TextView;

public class ShowMapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);
        OrientationService orientationService = OrientationService.singleton(this);
        TextView view = (TextView) findViewById(R.id.North);
        orientationService.getOrientation().observe(this, new Observer<Float>() {
            @Override
            public void onChanged(Float aFloat) {
                float degree = (float)((float) 360.0-(aFloat*180/Math.PI));
                view.setRotation(degree);
                ConstraintLayout compass = findViewById(R.id.compass);
                ConstraintProperties cp = new ConstraintProperties(compass);
                cp.rotation(degree);
            }
        });
    }


}