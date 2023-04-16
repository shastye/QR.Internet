package com.example.qrinternet.Activities.notifications;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.qrinternet.Activities.utility.ImageDetails;

import java.util.Vector;

public class NotificationsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    private static Vector<ImageDetails> imagesFromAPI;
    private static Vector<Bitmap> bitmapsOfQRCodes;
    private static int positionOfGrid;

    public NotificationsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public Vector<ImageDetails> getImagesFromAPI() {
        return imagesFromAPI;
    }
    public Vector<Bitmap> getBitmapsOfQRCodes() {
        return bitmapsOfQRCodes;
    }
    public int getPositionOfGrid() {
        return positionOfGrid;
    }

    public void setImagesFromAPI(Vector<ImageDetails> _imagesFromAPI){
        imagesFromAPI = _imagesFromAPI;
    }
    public void setBitmapsOfQRCodes(Vector<Bitmap> _bitmapsOfQRCodes) {
        bitmapsOfQRCodes = _bitmapsOfQRCodes;
    }
    public void setPositionOfGrid(int _positionOfGrid) {
        positionOfGrid = _positionOfGrid;
    }
}