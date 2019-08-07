package com.mad.thoughtExchange.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class VolleyUtils {

    public static Response.ErrorListener logError(String logName) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String body = new String(error.networkResponse.data,"UTF-8");
                    Log.d("FETCH_VOTES", body);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public static Map<String, String> getAuthenticationHeader(Activity activity) {
        String token = SharedPreferencesUtil.getStringFromSharedPreferences(activity.getSharedPreferences(SharedPreferencesUtil.myPreferences, Context.MODE_PRIVATE), SharedPreferencesUtil.token);
        Map<String, String> headers = new HashMap<>();
        headers.put("api-token", token);

        return headers;
    }


}
