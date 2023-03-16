package com.example.socialcompass.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.socialcompass.R;
import com.example.socialcompass.model.SocialCompassDao;
import com.example.socialcompass.model.SocialCompassDatabase;
import com.example.socialcompass.model.SocialCompassRepository;
import com.example.socialcompass.entity.SocialCompassUser;
import com.example.socialcompass.utilities.Utilities;

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

    public void onSaveClicked(View view) {
        TextView myID = findViewById(R.id.usersID);
        if (myID.getText().toString().equals(friendsID.getText().toString())) {
            Utilities.displayAlert(this, "Cannot add yourself");
            return;
        }
        if (userDao.exists(friendsID.getText().toString())) {
            Utilities.displayAlert(this, "Already added this friend");
            return;
        }
        if (friendsID.getText().toString().equals("")) {
            Utilities.displayAlert(this, "Friend ID not entered");
            return;
        }
        try {
            //SocialCompassUser friend = repo.getSynced(friendsID.getText().toString()).getValue();
            SocialCompassUser friend = repo.getRemoteWithoutLiveData(friendsID.getText().toString());
            friend.private_code = friend.public_code;
            Log.d(friend.label, friend.label);
            userDao.upsert(friend);
            friendsID.setText("");
            Utilities.displayAlert(this,"Successfully added friend: " + friend.label);
        } catch (Exception e) {
            Utilities.displayAlert(this,"User not found");
        }

    }

    public void onBackClicked(View view) {
        Intent intent = new Intent(this, ShowMapActivity.class);
        startActivity(intent);
    }
}