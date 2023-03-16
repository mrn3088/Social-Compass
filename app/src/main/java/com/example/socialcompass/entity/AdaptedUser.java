package com.example.socialcompass.entity;

import com.google.gson.Gson;

public class AdaptedUser {
    String private_code;
    String label;
    float latitude;
    float longitude;

    /* Class solely used for adapting a user's JSON file so that public code is not uploaded to PUT */

    /*
    PRE: socialCompassUser must not be null
    POST: adaptedUser can be uploaded to remote repository
     */
    public AdaptedUser(SocialCompassUser user) {
        this.private_code = user.private_code;
        this.label = user.label;
        this.latitude = user.latitude;
        this.longitude = user.longitude;
    }

    public static AdaptedUser fromJSON(String json) {
        return new Gson().fromJson(json, AdaptedUser.class);
    }
}
