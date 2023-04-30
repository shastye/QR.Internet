package com.example.qrinternet.Activities.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LogInViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    private static String username;
    private static String password;

    public LogInViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public static String getUsername() {
        return username;
    }
    public static String getPassword() {
        return password;
    }

    public static void setPassword(String _password) {
        password = _password;
    }
    public static void setUsername(String _username) {
        username = _username;
    }
}
