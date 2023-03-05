package com.example.socialcompass;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Upsert;

import java.util.List;

@Dao
public abstract class SocialCompassDao {
    @Upsert
    public abstract long upsert(String userID);

    //TODO: What to replace for @Query??
//    @Query("SELECT EXISTS(SELECT 1 FROM notes WHERE title = :title)")
//    public abstract boolean exists(String userID);
//
//
//    @Query("SELECT * FROM notes WHERE title = :title")
//    public abstract  get(String userID);
//
//    @Query("SELECT * FROM notes ORDER BY title")
//    public abstract  getAll();
}
