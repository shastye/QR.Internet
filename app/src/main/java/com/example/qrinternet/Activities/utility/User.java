package com.example.qrinternet.Activities.utility;

import java.io.Serializable;
import java.util.HashMap;

public class User {
    private String email;
    private String password;

    public User(String _email, String _password) {
        email = _email;
        password = _password;
    }
    public User(Serializable _serUser) {
        HashMap<String, String> hashUser = ((HashMap<String, String>) _serUser);

        try {
            email = hashUser.get(Tags.USER_EMAIL);
        } catch (Exception e) {
            email = "";
        }

        try {
            password = hashUser.get(Tags.USER_PASSWORD);
        } catch (Exception e) {
            password = "";
        }
    }

    public HashMap<String, String> getHashMap() {
        HashMap<String, String> temp = new HashMap<>();

        temp.put(Tags.USER_EMAIL, this.email);
        temp.put(Tags.USER_PASSWORD, this.password);

        return temp;
    }
}
