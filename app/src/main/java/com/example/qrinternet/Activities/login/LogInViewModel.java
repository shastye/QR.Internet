package com.example.qrinternet.Activities.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.qrinternet.Activities.roomsupplies.Image;
import com.example.qrinternet.Activities.roomsupplies.User;

import java.util.List;

public class LogInViewModel extends ViewModel {
    private final MutableLiveData<String> mText;
    private static List<Image> images;
    private static User currentUser;

    public LogInViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }


    public static void setImages(List<Image> _images) {
        LogInViewModel.images = _images;
    }

    public static void setCurrentUser(User _currentUser) {
        LogInViewModel.currentUser = _currentUser;
    }

    public static List<Image> getImages() {
        return images;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
