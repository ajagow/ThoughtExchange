package com.mad.thoughtExchange.models;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GsonRequest<T, U> {
    private RequestQueue requestQueue;

    private final Gson gson;
    private JsonStringRequest jsonStringRequest;

    private static final String PROTOCOL_CHARSET = "utf-8";

    /** Content type for request. */
    private static final String PROTOCOL_CONTENT_TYPE =
            String.format("application/json; charset=%s", PROTOCOL_CHARSET);

    /**
     * Make a POST request and return a parsed object from JSON.
     *
     * @param url URL of the request to make
     * @param clazz Relevant class object, for Gson's reflection
     * @param headers Map of request headers
     */
    public GsonRequest(int requestType, String url, T dataIn, Context context, Class<T> clazz, Class<U> responseClazz, Map<String, String> headers,
                            Response.Listener<U> listener, Response.ErrorListener errorListener) {
        this.gson = new Gson();
        if (dataIn == null) {
            jsonStringRequest = createNewGetRequest(url, responseClazz, listener, errorListener, headers);
        }
        else {
            jsonStringRequest = createNewPostJsonRequest(requestType, url, dataIn, clazz, responseClazz, listener, errorListener, headers);
        }
        this.requestQueue = Volley.newRequestQueue(context);

    }

    public GsonRequest(String url, Context context, Class<U> responseClass, Response.Listener<U> listener,
                            Response.ErrorListener errorListener, Map<String, String> headers) {
        this(Request.Method.GET, url, null, context, null, responseClass, headers, listener, errorListener);
    }

    public void volley() {
        this.requestQueue.add(jsonStringRequest);
    }

    private JsonStringRequest createNewGetRequest(String url, final Class<U> responseClazz,
                                                  final Response.Listener<U> listener,
                                                  final Response.ErrorListener errorListener,
                                                  final Map<String, String> headers) {

        JsonStringRequest jsonObjectRequest = new JsonStringRequest(url, null, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                U typedResponse = gson.fromJson(response, responseClazz);
                listener.onResponse(typedResponse);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorListener.onErrorResponse(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };

        return jsonObjectRequest;
    }


    private JsonStringRequest createNewPostJsonRequest(int requestType, String url, T requestObject, Class<T> clazz, final Class<U> responseClazz, final Response.Listener<U> listener,
                                                       final Response.ErrorListener errorListener, final Map<String, String> headers) {

        String dataIn = gson.toJson(requestObject, clazz);
        Log.d("JSON Request", dataIn); //

        JsonStringRequest jsonObjectRequest = new JsonStringRequest(requestType, url, dataIn, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                U typedResponse = gson.fromJson(response, responseClazz);
                listener.onResponse(typedResponse);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorListener.onErrorResponse(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };

        return jsonObjectRequest;
    }




}