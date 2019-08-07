package com.mad.thoughtExchange.utils;

import android.content.SharedPreferences;

/**
 * Util for helping to get and set things to SharedPreferences
 */

public class SharedPreferencesUtil {

    public static String token = "token_name";
    public static String networth = "networth_of_user";
    public static String myPreferences = "my_preferences";
    public static String userName = "user_name";

    public static void saveToSharedPreferences(SharedPreferences sharedPreferences, String name, String data) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(name, data);
        editor.commit();
    }

    public static void saveToSharedPreferences(SharedPreferences sharedPreferences, String name, int number) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(name, number);
        editor.commit();
    }

    public static String getStringFromSharedPreferences(SharedPreferences sharedPreferences, String name) {
        return sharedPreferences.getString(name, null);
    }

    public static int getIntFromSharedPreferences(SharedPreferences sharedPreferences, String name) {
        return sharedPreferences.getInt(name, 0);
    }
}
