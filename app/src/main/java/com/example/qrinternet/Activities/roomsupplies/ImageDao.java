package com.example.qrinternet.Activities.roomsupplies;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ImageDao {
    @Query("SELECT * FROM images")
    List<Image> getAllImages();

    @Query("SELECT * FROM images " +
            "INNER JOIN users ON users.userID = images.userID " +
            "WHERE users.username LIKE :username")
    List<Image> findImagesFromUser(String username);

    @Insert
    void insertImage(Image image);

    @Update
    void updateImage(Image image);

    @Delete
    void deleteImage(Image image);
}
