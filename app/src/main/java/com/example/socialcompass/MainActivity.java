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
 * This class is MainActivity class used to support main page
 */
public class MainActivity extends AppCompatActivity {
    // IF SHARED PREFERENCES DON'T EXIST STAY ON PAGE
    // ELSE IMMEDIATELY LEAVE MAIN ACTIVITY AND LOAD MAP!
    private int manual_rotation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // check if there is an active user
        // if no active user exists, prompt registration
        // else move immediatly to map
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        String uid = preferences.getString("uid", "No value");
        if (uid != "No value") {
            Intent intent = new Intent(this, ShowMapActivity.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
        }


    }

    /*
    @ensures new user is created and sent to server
     */
    public void onSubmitLabelsClicked(View view) {

        String manualRotationStr = ((TextView) findViewById(R.id.orientation_input)).getText().toString();
        String manualRotationOpt = Utilities.USE_PHONE_ORIENTATION;
        if (!manualRotationStr.equals("") && !Utilities.validOrientation(manualRotationStr)) {
            Utilities.displayAlert(this, "Orientation needs to be a integer between 0 and 359!");
            return;
        } else {
            manualRotationOpt = manualRotationStr;
        }

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("manual_rotation", manualRotationOpt);

        String publicID = UUID.randomUUID().toString();
        String privateID = UUID.randomUUID().toString();

        TextView tv = findViewById(R.id.displayName);
        editor.putString("name", tv.getText().toString());
        editor.putString("uid", publicID);
        editor.apply();
        SocialCompassUser theUser = new SocialCompassUser(privateID, publicID, tv.getText().toString(), 0.0f, 0.0f);
        var db = SocialCompassDatabase.provide(getApplicationContext()); //fix this later lmao
        var dao = db.getDao();
        var repo = new SocialCompassRepository(dao);
        try {
            repo.upsertSynced(theUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // FOR TESTING PURPOSES ONLY
        TextView setPublicCode = (TextView) findViewById(R.id.publicCode);
        setPublicCode.setText(publicID);


        Intent intent = new Intent(this, ShowMapActivity.class);
        intent.putExtra("uid", publicID);
        startActivity(intent);

    }

}