package com.example.socialcompass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

public class AddFriendActivity extends AppCompatActivity {
    TextView friendsID;
    SocialCompassRepository repo;
    SocialCompassDatabase db;
    SocialCompassDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        db = SocialCompassDatabase.provide(getApplicationContext());
        userDao = db.getDao();
        repo = new SocialCompassRepository(userDao);

        TextView usersID = findViewById(R.id.usersID);
        friendsID = findViewById(R.id.friendID);

        String uid = getIntent().getStringExtra("uid");


        usersID.setText(uid);
    }

    /*
    @requires user with inputed id to exist
    @ensures user with inpputed id is added to dao
     */
    public void onSaveClicked(View view) {

        if (userDao.exists(friendsID.getText().toString())) {
            Utilities.displayAlert(this, "Already added this friend");
            return;
        }
        try {
            SocialCompassUser friend = repo.getSynced(friendsID.getText().toString()).getValue();
            friend.private_code = friend.public_code;
            Log.d(friend.label, friend.label);
            userDao.upsert(friend);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void onBackClicked(View view) {
        Intent intent = new Intent(this, ShowMapActivity.class);
        startActivity(intent);
    }
}
