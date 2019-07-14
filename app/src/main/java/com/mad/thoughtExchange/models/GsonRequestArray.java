package com.mad.thoughtExchange.models;

import android.content.Context;

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

public class GsonRequestArray<T, U> {
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
    public GsonRequestArray(int requestType, String url, T dataIn, Context context, Class<T> clazz, Class<U> responseClazz, Map<String, String> headers,
                       Response.Listener<List<U>> listener, Response.ErrorListener errorListener) {
        this.gson = new Gson();
        if (dataIn == null) {
            jsonStringRequest = createNewGetRequest(url, responseClazz, listener, errorListener, headers);
        }
        else {
            jsonStringRequest = createNewPostJsonRequest(requestType, url, dataIn, clazz, responseClazz, listener, errorListener, headers);
        }
        this.requestQueue = Volley.newRequestQueue(context);

    }

    public GsonRequestArray(String url, Context context, Class<U> responseClass, Response.Listener<List<U>> listener,
                       Response.ErrorListener errorListener, Map<String, String> headers) {
        this(Request.Method.GET, url, null, context, null, responseClass, headers, listener, errorListener);
    }




    public void volley() {
        this.requestQueue.add(jsonStringRequest);
    }

    private JsonStringRequest createNewGetRequest(String url, final Class<U> responseClazz,
                                                  final Response.Listener<List<U>> listener,
                                                  final Response.ErrorListener errorListener,
                                                  final Map<String, String> headers) {

        JsonStringRequest jsonObjectRequest = new JsonStringRequest(url, null, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Type listOfMyClassObject = TypeToken.getParameterized(List.class, responseClazz).getType();
                List<U> responses = gson.fromJson(response, listOfMyClassObject);

                listener.onResponse(responses);

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


    private JsonStringRequest createNewPostJsonRequest(int requestType, String url, T requestObject,
                                                       Class<T> clazz, final Class<U> responseClazz,
                                                       final Response.Listener<List<U>> listener,
                                                       final Response.ErrorListener errorListener,
                                                       final Map<String, String> headers) {

        String dataIn = gson.toJson(requestObject, clazz);

        JsonStringRequest jsonObjectRequest = new JsonStringRequest(requestType, url, dataIn, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Type founderListType = new TypeToken<ArrayList<U>>(){}.getType();
                List<U> responses = gson.fromJson(response, founderListType);
                listener.onResponse(responses);
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
