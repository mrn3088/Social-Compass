package com.example.socialcompass;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "users")
public class SocialCompassUser {
    @PrimaryKey
    @SerializedName("public_code")
    @NonNull
    public String public_code;

    @SerializedName("label")
    @NonNull
    public String label;

    @SerializedName("latitude")
    @NonNull
    public float latitude;

    @SerializedName("longitude")
    @NonNull
    public float longitude;

    public SocialCompassUser(@NonNull String public_code, @NonNull String label, @NonNull float latitude, float longitude) {
        this.public_code = public_code;
        this.label = label;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static SocialCompassUser fromJSON(String json) {
        return new Gson().fromJson(json, SocialCompassUser.class);
    }
}
