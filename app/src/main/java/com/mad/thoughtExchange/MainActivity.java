package com.mad.thoughtExchange;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mad.thoughtExchange.models.GsonRequest;
import com.mad.thoughtExchange.models.LoginModel;
import com.mad.thoughtExchange.responses.LoginResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static String URL = "https://blog-api-tutorial1.herokuapp.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void onClick(View view) throws JSONException {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("email", "myname2@email.com");
            jsonBody.put("password", "password");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        


        JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, URL + "api/v1/users/login" , jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                String stringResponse = response.toString();
                Gson gson = new Gson();
                LoginResponse loginResponse = gson.fromJson(stringResponse, LoginResponse.class);

                Toast.makeText(getApplicationContext(), "Response:  " + loginResponse.getToken(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Error:  " + error.networkResponse.toString() + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();


            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Basic " + "c2FnYXJAa2FydHBheS5jb206cnMwM2UxQUp5RnQzNkQ5NDBxbjNmUDgzNVE3STAyNzI=");//put your token here
                return headers;
            }
        };
        requestQueue.add(jsonOblect);
    }

    private Response.Listener<LoginModel> createMyReqSuccessListener() {
        return new Response.Listener<LoginModel>() {
            @Override
            public void onResponse(LoginModel response) {
                // Do whatever you want to do with response;
                // Like response.tags.getListing_count(); etc. etc.
                Toast.makeText(getApplicationContext(), "Response:  " + response.toString(), Toast.LENGTH_SHORT).show();

            }
        };
    }

    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Do whatever you want to do with error.getMessage();
                Toast.makeText(getApplicationContext(), "Error:  " + error.networkResponse.toString() + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        };
    }
}
