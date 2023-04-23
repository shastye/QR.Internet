package com.example.qrinternet.Activities.roomsupplies;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "images", indices = {@Index(value = "imageID", unique = true), @Index(value = "userID", unique = true)})
public class Image {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "imageID")
    public int imageID;

    @ColumnInfo(name = "userID")
    public int userID;

    @ColumnInfo(name = "source")
    public String source;

    @ColumnInfo(name = "binaryData")
    public byte[] binaryData;


    public Image(int imageID, int userID, String source, byte[] binaryData) {
        this.imageID = imageID;
        this.userID = userID;
        this.source = source;
        this.binaryData = binaryData;
    }

    @Ignore
    public Image(int userID, String source, byte[] binaryData) {
        this.userID = userID;
        this.source = source;
        this.binaryData = binaryData;
    }
}
