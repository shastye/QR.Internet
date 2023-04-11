package com.example.qrinternet.Activities.utility;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Methods {
    public static Boolean SaveBitmapAsPNGToDevice(String _filename, Bitmap _bitmap) {
        File file = new File(Tags.SAVE_PATH, _filename);
        try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
            _bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            return true;
        } catch (Exception e) {
            Log.e("Couldn't open file", e.getMessage());
            return false;
        }
    }

    public static Boolean SaveBinaryAsBINToDevice(String _filename, byte[] _binary) {
        File file = new File(Tags.SAVE_PATH, _filename);

        try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
            outputStream.write(_binary);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
}
