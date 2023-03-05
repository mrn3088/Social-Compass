/**
 * This file has MainActivity class used to support main page
 */

package com.example.socialcompass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *  This class is MainActivity class used to support main page
 */
public class MainActivity extends AppCompatActivity {
    // IF SHARED PREFERENCES DON'T EXIST STAY ON PAGE
    // ELSE IMMEDIATELY LEAVE MAIN ACTIVITY AND LOAD MAP!
    private int manual_rotation;
    private String uniqueID;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onSubmitLabelsClicked(View view) {
        // if no labels have been added display error

        String manualRotationStr = ((TextView)findViewById(R.id.orientation_input)).getText().toString();
        String manualRotationOpt = Utilities.USE_PHONE_ORIENTATION;
        if (!manualRotationStr.equals("") && !Utilities.validOrientation(manualRotationStr)) {
            Utilities.displayAlert(this, "Orientation needs to be a integer between 0 and 359!");
            return;
        } else {
            manualRotationOpt = manualRotationStr;
        }



        // if all checks pass open map
        SharedPreferences preferences = getSharedPreferences("com.example.socialcompass", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("manual_rotation", manualRotationOpt);
        editor.apply();

        uniqueID = UUID.randomUUID().toString();
        TextView tv = findViewById(R.id.displayName);
        editor.putString("name",tv.getText().toString());
        editor.putString("uid",uniqueID);
        editor.apply();
        SocialCompassUser theUser = new SocialCompassUser(uniqueID, tv.getText().toString(), 0, 0);
        SocialCompassAPI api = new SocialCompassAPI();
        api = api.provide();
        try {
            api.addUser(theUser);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, ShowMapActivity.class);

        startActivity(intent);

    }

}