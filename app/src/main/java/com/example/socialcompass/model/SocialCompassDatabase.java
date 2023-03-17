/**
 * This class sets up local database
 */

package com.example.socialcompass.model;

import android.content.Context;

import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.socialcompass.entity.SocialCompassUser;

@Database(entities = {SocialCompassUser.class}, version = 1, exportSchema = false)
public abstract class SocialCompassDatabase extends RoomDatabase {
    private volatile static SocialCompassDatabase instance = null;

    public abstract SocialCompassDao getDao();

    public synchronized static SocialCompassDatabase provide(Context context) {
        if (instance == null) {
            instance = SocialCompassDatabase.make(context);
        }
        return instance;
    }

    private static SocialCompassDatabase make(Context context) {
        return Room.databaseBuilder(context, SocialCompassDatabase.class, "social_compass.db")
                .allowMainThreadQueries()
                .build();
    }

    @VisibleForTesting
    public static void inject(SocialCompassDatabase testDatabase) {
        if (instance != null) {
            instance.close();
        }
        instance = testDatabase;
    }
}
