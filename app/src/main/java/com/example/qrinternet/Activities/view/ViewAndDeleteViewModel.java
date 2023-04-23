package com.example.qrinternet.Activities.view;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.qrinternet.Activities.utility.ImageDetails;

import java.util.Vector;

public class ViewAndDeleteViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    private static Vector<ImageDetails> imagesFromAPI;
    private static Vector<Bitmap> bitmapsOfQRCodes;
    private static int positionOfGrid;

    public ViewAndDeleteViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public static Vector<ImageDetails> getImagesFromAPI() {
        return imagesFromAPI;
    }
    public static Vector<Bitmap> getBitmapsOfQRCodes() {
        return bitmapsOfQRCodes;
    }
    public static int getPositionOfGrid() {
        return positionOfGrid;
    }

    public static void setImagesFromAPI(Vector<ImageDetails> _imagesFromAPI){
        imagesFromAPI = _imagesFromAPI;
    }
    public static void setBitmapsOfQRCodes(Vector<Bitmap> _bitmapsOfQRCodes) {
        bitmapsOfQRCodes = _bitmapsOfQRCodes;
    }
    public static void setPositionOfGrid(int _positionOfGrid) {
        positionOfGrid = _positionOfGrid;
    }
}