package com.example.qrinternet.Activities.roomsupplies;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "users", indices = {@Index(value = "userID", unique = true), @Index(value = "username", unique = true)})
public class User {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "userID")
    public int userID;

    @ColumnInfo(name = "username")
    public String username;

    @ColumnInfo(name = "password")
    public String password;

    public User(int userID, String username, String password) {
        this.userID = userID;
        this.username = username;
        this.password = password;
    }

    @Ignore
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
