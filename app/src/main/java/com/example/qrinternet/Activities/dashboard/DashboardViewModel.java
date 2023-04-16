package com.example.qrinternet.Activities.dashboard;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DashboardViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    private static Bitmap bitmap;
    private static byte[] binaryData;

    public DashboardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
    public byte[] getBinaryData() {
        return binaryData;
    }

    public void setBitmap(Bitmap _bitmap) {
        bitmap = _bitmap;
    }
    public void setBinaryData(byte[] _binaryData) {
        binaryData = _binaryData;
    }
}