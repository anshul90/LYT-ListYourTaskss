package com.example.demoapplication.UtilClass;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppPreferences {
    private static String DEMO_APP_ANDROID = "demo_app_preference";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(DEMO_APP_ANDROID,
                Context.MODE_PRIVATE);
    }

    public static Boolean getIsLogin(Context ctx, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return preferences.getBoolean(key, false);
    }

    public static void setIsLogin(Context ctx, String key, Boolean value) {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor edit = preference.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public static String getUserName(Context ctx, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return preferences.getString(key, "null");
    }

    public static void setUserName(Context ctx, String key, String value) {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor edit = preference.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static String getPassword(Context ctx, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return preferences.getString(key, "null");
    }

    public static void setPassword(Context ctx, String key, String value) {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor edit = preference.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static String getUserFirstName(Context ctx, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return preferences.getString(key, "null");
    }

    public static void setUserFirstName(Context ctx, String key, String value) {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor edit = preference.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static String getUserLastName(Context ctx, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return preferences.getString(key, "null");
    }

    public static void setUserLastName(Context ctx, String key, String value) {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor edit = preference.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static String getUserEmail(Context ctx, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return preferences.getString(key, "null");
    }

    public static void setUserEmail(Context ctx, String key, String value) {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor edit = preference.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static String getUserGender(Context ctx, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return preferences.getString(key, "null");
    }

    public static void setUserGender(Context ctx, String key, String value) {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor edit = preference.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static String getUserProfilePic(Context ctx, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return preferences.getString(key, "null");
    }

    public static void setUserProfilePic(Context ctx, String key, String value) {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor edit = preference.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static String getUserId(Context ctx, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return preferences.getString(key, "null");
    }

    public static void setUserId(Context ctx, String key, String value) {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor edit = preference.edit();
        edit.putString(key, value);
        edit.commit();
    }
}
