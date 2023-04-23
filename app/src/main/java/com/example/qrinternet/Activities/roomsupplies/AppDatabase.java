package com.example.qrinternet.Activities.roomsupplies;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.Database;

@Database(entities = {User.class, Image.class}, exportSchema = false, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static final String dbName = "UserImages";
    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context _context) {
        if (instance == null) {
            instance = Room.databaseBuilder(_context.getApplicationContext(),
                    AppDatabase.class, dbName)
                    .build();
        }

        return instance;
    }

    public abstract UserDao userDao();
    public abstract ImageDao imageDao();
}
