package com.example.qrinternet.Activities.utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class Methods {
    public static Bitmap convertToBitmap(byte[] _binaryData) {
        InputStream inputStream = new ByteArrayInputStream(_binaryData);
        return BitmapFactory.decodeStream(inputStream);
    }
    public static Bitmap convertToBitmap(String _filename) {
        return BitmapFactory.decodeFile(_filename);
    }
}
