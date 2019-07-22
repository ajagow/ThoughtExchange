package com.mad.thoughtExchange.models;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.cache.NoCache;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.Map;

/**
 * Class for handling volley and Gson POST and GET calls.  Used when only expect one object back
 * @param <T> request class
 * @param <U> response class
 */

public class GsonRequest<T, U> {
    private RequestQueue requestQueue;

    private final Gson gson;
    private JsonStringRequest jsonStringRequest;

    private static final String PROTOCOL_CHARSET = "utf-8";

    /** Content type for request. */
    private static final String PROTOCOL_CONTENT_TYPE =
            String.format("application/json; charset=%s", PROTOCOL_CHARSET);

    /**
     * Create a GsonRequest, determine whether it's a post or get request
     * @param requestType type of request
     * @param url string url to make the api call to
     * @param dataIn Object to make the request
     * @param context context of request
     * @param clazz
     * @param responseClazz Class object of what the response is going to be like
     * @param headers additional headers to send
     * @param listener what do on when request is successful
     * @param errorListener what to do when request returns an error
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
        requestQueue.getCache().clear();

    }


    /**
     * Make a GET request and return a parsed object from JSON.
     */

    public GsonRequest(String url, Context context, Class<U> responseClass, Response.Listener<U> listener,
                            Response.ErrorListener errorListener, Map<String, String> headers) {
        this(Request.Method.GET, url, null, context, null, responseClass, headers, listener, errorListener);
    }

    public void volley() {
        this.requestQueue.add(jsonStringRequest);
    }

    // helper for creating a new volley GET request. Converts U class to JSON string
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

        jsonObjectRequest.setShouldCache(false);
        return jsonObjectRequest;
    }


    // helper to create a new volley POST request. Convert T object to JSON string and
    // response JSON string to object of type U
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

        jsonObjectRequest.setShouldCache(false);

        return jsonObjectRequest;
    }




}