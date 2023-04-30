package com.example.qrinternet.Activities.utility;

import java.io.Serializable;
import java.util.HashMap;

public class User {
    private String email;

    public User(String _email) {
        email = _email;
    }
    public User(Serializable _serUser) {
        HashMap<String, String> hashUser = ((HashMap<String, String>) _serUser);

        try {
            email = hashUser.get(Tags.USER_EMAIL);
        } catch (Exception e) {
            email = "";
        }
    }

    public HashMap<String, String> getHashMap() {
        HashMap<String, String> temp = new HashMap<>();

        temp.put(Tags.USER_EMAIL, this.email);

        return temp;
    }
}
