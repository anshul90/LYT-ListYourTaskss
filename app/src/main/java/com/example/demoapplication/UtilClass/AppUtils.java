package com.example.demoapplication.UtilClass;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AppUtils {
    public final static String ISLOGIN = "islogin";
    public final static String USER_KEY = "user_key";
    public final static String USER_ID = "user_id";
    public final static String FIRST_NAME = "first_name";
    public final static String LAST_NAME = "last_name";
    public final static String EMAIL = "email";
    public final static String PASSWORD = "password";
    public final static String PROFILE_IMAGE = "profile_image";
    public final static String USER_NAME = "name";

    public static boolean hasNetworkConnection(Context con) {
        boolean hasConnectedWifi = false;
        boolean hasConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    hasConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    hasConnectedMobile = true;
        }
        return hasConnectedWifi || hasConnectedMobile;
    }
}
