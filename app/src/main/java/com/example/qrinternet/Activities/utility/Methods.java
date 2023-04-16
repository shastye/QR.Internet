package com.example.qrinternet.Activities.utility;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

public class Methods {
    public static Boolean SaveBitmapAsPNGToDevice(String _filename, Bitmap _bitmap) {
        File file = new File(Tags.SAVE_PATH, _filename);
        try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
            _bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            outputStream.close();
            return true;
        } catch (Exception e) {
            Log.e("Couldn't open file", e.getMessage());
            return false;
        }
    }

    public static int CountNumberOfSavedImages(String _filepath) {
        File directory = new File(_filepath);
        if (directory.exists()) {
            File[] files = directory.listFiles();
            return files.length;
        }
        else {
            return -1;
        }
    }
}
