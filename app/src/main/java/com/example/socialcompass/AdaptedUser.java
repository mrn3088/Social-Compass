package com.example.socialcompass;

import com.google.gson.Gson;

public class AdaptedUser {
    String private_code;
    String label;
    float latitude;
    float longitude;

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
