package com.example.qrinternet.Activities.utility;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Methods {
    public Boolean SaveBitmapAsPNGToDevice(String _filename, Bitmap _bitmap) {
        File file = new File(Tags.IMAGE_PATH, _filename);
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            _bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public Boolean SaveBinaryAsBINToDevice(String _filename, byte[] _binary) {
        File file = new File(Tags.IMAGE_PATH, _filename);

        try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
            outputStream.write(_binary);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
}
