package com.example.socialcompass;

import android.provider.MediaStore;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class SocialCompassRepository {
    private final SocialCompassDao dao;
    private final OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON
            = MediaType.get("applicaion/json; charset=utf-8");

    public SocialCompassRepository(SocialCompassDao dao) {
        this.dao = dao;
    }
}
