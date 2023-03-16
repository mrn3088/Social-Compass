/**
 * This file has MainActivity class used to support main page
 */

package com.example.socialcompass.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.socialcompass.R;
import com.example.socialcompass.model.SocialCompassAPI;
import com.example.socialcompass.model.SocialCompassDatabase;
import com.example.socialcompass.model.SocialCompassRepository;
import com.example.socialcompass.entity.SocialCompassUser;
import com.example.socialcompass.utilities.Utilities;

import java.util.UUID;

/**
 * This class is MainActivity class used to support main page
 */
public class MainActivity extends AppCompatActivity {
    // IF SHARED PREFERENCES DON'T EXIST STAY ON PAGE
    // ELSE IMMEDIATELY LEAVE MAIN ACTIVITY AND LOAD MAP!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // check if there is an active user
        // if no active user exists, prompt registration
        // else move immediatly to map
        SharedPreferences preferences = getSharedPreferences("com.example.socialcompass", MODE_PRIVATE);
        String uid = preferences.getString("uid", "No value");




        if (!uid.equals("No value")) {
            Intent intent = new Intent(this, ShowMapActivity.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
        }


    }

    public void onSubmitLabelsClicked(View view) {
        String url = ((TextView) findViewById(R.id.orientation_input)).getText().toString();

        if (url.equals("")) {
            url = Utilities.DEFAULT_URL;
        }

        SocialCompassAPI.provide().useURL(url);

        SharedPreferences preferences = getSharedPreferences("com.example.socialcompass", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();


        String publicID = UUID.randomUUID().toString();
        String privateID = UUID.randomUUID().toString();

        TextView tv = findViewById(R.id.displayName);

        if (tv.getText().toString().equals("")) {
            Utilities.displayAlert(this, "Displayed name cannot be empty!");
            return;
        }

        editor.putString("name", tv.getText().toString());
        editor.putString("uid", publicID);
        editor.putString("server_url", url);
        Log.d("added uid", publicID);
        editor.putString("private_code", privateID);
        Log.d("added private_code", privateID);
        editor.apply();
        SocialCompassUser theUser = new SocialCompassUser(privateID, publicID, tv.getText().toString(), 0.0f, 0.0f);
        var db = SocialCompassDatabase.provide(getApplicationContext()); //fix this later lmao
        var dao = db.getDao();
        var repo = new SocialCompassRepository(dao);
        try {
            repo.upsertRemote(theUser);
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView setPublicCode = (TextView) findViewById(R.id.publicCode);
        setPublicCode.setText(publicID);

        Intent intent = new Intent(this, ShowMapActivity.class);
        intent.putExtra("uid", publicID);
        intent.putExtra("private_code", privateID);
        intent.putExtra("name", tv.getText().toString());
        startActivity(intent);

    }

}