package com.example.socialcompass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

public class AddFriendActivity extends AppCompatActivity {
    TextView friendsID;
    SocialCompassAPI api;
    SocialCompassDatabase db;
    SocialCompassDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        db = SocialCompassDatabase.provide(getApplicationContext());
        userDao = db.getDao();
        api = new SocialCompassAPI();

        TextView usersID = findViewById(R.id.usersID);
        friendsID = findViewById(R.id.friendID);

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        String uid  = preferences.getString("uid", "");

        usersID.setText(uid);
    }

    public void onSaveClicked(View view) {
        api = api.provide();
        try {
            SocialCompassUser friend = api.getUser(friendsID.getText().toString());
            userDao.upsert(friend);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}