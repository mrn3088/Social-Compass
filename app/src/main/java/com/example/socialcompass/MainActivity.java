package com.example.socialcompass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    // IF SHARED PREFERENCES DON'T EXIST STAY ON PAGE
    // ELSE IMMEDAITLY LEAVE MAIN ACTIVITY AND LOAD MAP!


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, ShowMapActivity.class);
        if(false) {
            startActivity(intent);
        }

    }

    public void onSubmitLabelsClicked(View view) {
        // if no labels have been added display error
        if(false) {
            Utilities.displayAlert(this, "you must enter at least one label before proceeding!");
        // if label strings are too long display error
        } else if(false) {
            Utilities.displayAlert(this, "your label strings must be less then 12 characters!");
        // if coordinates are innacurate display error
        } else if (false) {
            Utilities.displayAlert(this, "your coordinates are innacurate!");
        } else {
            // if all checks pass open map
            Intent intent = new Intent(this, ShowMapActivity.class);
            startActivity(intent);
        }
    }
}