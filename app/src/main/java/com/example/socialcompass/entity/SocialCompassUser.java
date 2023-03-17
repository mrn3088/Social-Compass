/**
 * A SocialCompassUser class as the object for database query
 */

package com.example.socialcompass.entity;

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

    @SerializedName("private_code")
    @NonNull
    public String private_code;

    public SocialCompassUser(@NonNull String private_code, @NonNull String public_code, @NonNull String label, @NonNull float latitude,@NonNull float longitude) {
        this.private_code = private_code;
        this.public_code = public_code;
        this.label = label;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public String getLabel() {
        return this.label;
    }

    public float getLatitude() {
        return this.latitude;
    }

    public float getLongitude() {
        return this.longitude;
    }

    public static SocialCompassUser fromJSON(String json) {
        return new Gson().fromJson(json, SocialCompassUser.class);
    }
}
