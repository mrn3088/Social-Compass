package com.example.socialcompass;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class SocialCompassAPI {
    private volatile static SocialCompassAPI instance = null;

    private OkHttpClient client;

    public SocialCompassAPI() {
        this.client = new OkHttpClient();
    }

    public static SocialCompassAPI provide() {
        if(instance == null) {
            instance = new SocialCompassAPI();
        }
        return instance;
    }
}
