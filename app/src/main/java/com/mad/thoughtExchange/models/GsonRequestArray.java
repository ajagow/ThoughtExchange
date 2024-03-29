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

/**
 * Class for handling volley and Gson POST and GET calls.  Used when expecting a list is type U
 * objects from api.
 * @param <T> request class
 * @param <U> response class
 */

public class GsonRequestArray<T, U> {
    private RequestQueue requestQueue;

    private final Gson gson;
    private JsonStringRequest jsonStringRequest;

    /**
     * Create a GsonRequestArray, determine whether it's a post or get request
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
    public GsonRequestArray(int requestType, String url, T dataIn, Context context, Class<T> clazz,
                            Class<U> responseClazz, Map<String, String> headers,
                       Response.Listener<List<U>> listener, Response.ErrorListener errorListener) {
        this.gson = new Gson();
        if (dataIn == null) {
            jsonStringRequest = createNewGetRequest(url, responseClazz, listener, errorListener,
                    headers);
        }
        else {
            jsonStringRequest = createNewPostJsonRequest(requestType, url, dataIn, clazz,
                    responseClazz, listener, errorListener, headers);
        }
        this.requestQueue = Volley.newRequestQueue(context);
        requestQueue.getCache().clear();

    }

    public GsonRequestArray(String url, Context context, Class<U> responseClass,
                            Response.Listener<List<U>> listener,
                       Response.ErrorListener errorListener, Map<String, String> headers) {
        this(Request.Method.GET, url, null, context, null, responseClass, headers,
                listener, errorListener);
    }

    /**
     * Send out the volley request to the api.
     */
    public void volley() {
        this.requestQueue.add(jsonStringRequest);
    }

    /**
     * Create a new JSON POST request through volley.
     * @param url string url to make the api call to
     * @param responseClazz Class object of what the response is going to be like
     * @param listener what do on when request is successful
     * @param errorListener what to do when request returns an error
     * @param headers additional headers to send
     * @return Json response converted to object of type U
     */

    private JsonStringRequest createNewGetRequest(String url, final Class<U> responseClazz,
                                                  final Response.Listener<List<U>> listener,
                                                  final Response.ErrorListener errorListener,
                                                  final Map<String, String> headers) {

        JsonStringRequest jsonObjectRequest = new JsonStringRequest(url, null,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Type listOfMyClassObject = TypeToken.getParameterized(List.class,
                        responseClazz).getType();
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

        jsonObjectRequest.setShouldCache(false);

        return jsonObjectRequest;
    }


    /**
     * Create a new JSON POST request through volley.
     * @param requestType type of request
     * @param url string url to make the api call to
     * @param requestObject Object to make the request
     * @param clazz Class object of type request
     * @param responseClazz Class object of what the response is going to be like
     * @param listener what do on when request is successful
     * @param errorListener what to do when request returns an error
     * @param headers additional headers to send
     * @return Json response converted to object of type U
     */
    private JsonStringRequest createNewPostJsonRequest(int requestType, String url, T requestObject,
                                                       Class<T> clazz, final Class<U> responseClazz,
                                                       final Response.Listener<List<U>> listener,
                                                       final Response.ErrorListener errorListener,
                                                       final Map<String, String> headers) {

        String dataIn = gson.toJson(requestObject, clazz);

        JsonStringRequest jsonObjectRequest = new JsonStringRequest(requestType, url, dataIn,
                new Response.Listener<String>() {
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

        jsonObjectRequest.setShouldCache(false);

        return jsonObjectRequest;
    }
}
