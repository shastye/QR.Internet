package com.example.qrinternet.Activities.utility;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Methods {
    public Boolean SaveBitmapAsPNGToDevice(Bitmap _bitmap) {
        File file = new File(Tags.IMAGE_PATH, Tags.IMAGE_NAME);
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            _bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
}
