package com.example.qrinternet.Activities.utility;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Tags {
    public static final String API_KEY = "ed8459dfefmsh79582b49cfeb426p19613djsn9c590ae302ce";
    public static int NUM_SAVED_QRCODES = 0;
    public static String EMAIL = "shastye.7x@gmail.com";

    public static String SAVE_PATH = "/storage/self/primary/Pictures/QRInternet/";

    public static FirebaseAuth AUTH;
    public static FirebaseUser USER;

    // DATABASE KEYS
    public static final String USER_EMAIL = "email";
    public static final String USER_PASSWORD = "password";
}
