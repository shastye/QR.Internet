package com.example.qrinternet.Activities.create;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CreateAndSaveViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    private static Bitmap bitmap;
    private static byte[] binaryData;

    public CreateAndSaveViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public static Bitmap getBitmap() {
        return bitmap;
    }
    public static byte[] getBinaryData() {
        return binaryData;
    }

    public static void setBitmap(Bitmap _bitmap) {
        bitmap = _bitmap;
    }
    public static void setBinaryData(byte[] _binaryData) {
        binaryData = _binaryData;
    }
}