package com.example.socialcompass;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

@Entity(tableName = "users")
public class SocialCompassUser {
    @PrimaryKey
    @SerializedName("userID")
    @NonNull
    public String userID;

    @SerializedName("username")
    @NonNull
    public String name;

    @SerializedName("location")
    @NonNull
    public Position location;

    public SocialCompassUser(@NonNull String userID, @NonNull String name, @NonNull Position location) {
        this.userID = userID;
        this.name = name;
        this.location = location;
    }
}
