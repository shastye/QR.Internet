package com.example.qrinternet.Activities.utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

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

    public static Bitmap convertToBitmap(byte[] _binaryData) {
        InputStream inputStream = new ByteArrayInputStream(_binaryData);
        return BitmapFactory.decodeStream(inputStream);
    }
    public static Bitmap convertToBitmap(String _filename) {
        return BitmapFactory.decodeFile(_filename);
    }
}
