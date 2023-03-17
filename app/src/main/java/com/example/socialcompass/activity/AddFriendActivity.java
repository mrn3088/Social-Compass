/**
 * This file is an AddFriendActivity class which provides the page
 * for the users to input other users' id and add them as friends.
 */

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

    /**
     * The method binded to save button, which is used to get user from
     * remote server and insert it into database.
     * @param view
     */
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
            // get from remote and insert into local database
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

    /**
     * Method binded to back button which is used to go back to show map activity.
     * @param view
     */
    public void onBackClicked(View view) {
        Intent intent = new Intent(this, ShowMapActivity.class);
        startActivity(intent);
    }

    //for testing purpose
    public SocialCompassDao getUserDao(){
        return userDao;
    }
}
