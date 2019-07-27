package com.mad.thoughtExchange;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.mad.thoughtExchange.models.GsonRequest;
import com.mad.thoughtExchange.models.LoginModel;
import com.mad.thoughtExchange.responses.LoginResponse;
import com.mad.thoughtExchange.utils.SharedPreferencesUtil;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public static final String URL = "https://thought-exchange-api.herokuapp.com/";
    private static String USERS_PATH = "api/v1/users/login";

    private TextView email;
    private TextView password;
    private Button submitButton;
    private Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TESTING ONBOARDING TODO: remove to after signup
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(!sharedPreferences.getBoolean(OnboardingFragment.COMPLETED_ONBOARDING, false)) {
            // This is the first time running the app, let's go to onboarding
            startActivity(new Intent(this, OnboardingActivity.class));
        }
        /////////////////////

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        submitButton = findViewById(R.id.login_btn);
        signupButton = findViewById(R.id.signup_btn);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmit(view);
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSignup(view);
            }
        });

    }

    public void onSignup(View view) {
        Intent intentToSignup = new Intent(MainActivity.this, SignupActivity.class);
        startActivity(intentToSignup);
    }

    public void onSubmit(View view) {

        String emailVal = email.getText().toString();
        String passwordVal = password.getText().toString();

        LoginModel loginModel = new LoginModel();
        loginModel.setEmail(emailVal);
        loginModel.setPassword(passwordVal);

        Response.Listener<LoginResponse> responseListener = new Response.Listener<LoginResponse>() {
            @Override
            public void onResponse(LoginResponse response) {
                onSuccessfulLogin(response);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String body = new String(error.networkResponse.data,"UTF-8");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_SHORT).show();
            }
        };

        GsonRequest<LoginModel, LoginResponse> gsonRequest = new GsonRequest<>(Request.Method.POST, URL + USERS_PATH, loginModel, this,
                LoginModel.class, LoginResponse.class, new HashMap<String, String>(), responseListener, errorListener);

        gsonRequest.volley();

}

    // on successful login attempt, go to HomeActivity
    private void onSuccessfulLogin(LoginResponse loginResponse) {
        String token = loginResponse.getToken();
        Log.d("loginResponseToken",token);
        Intent explicitIntent = new Intent(MainActivity.this, DashboardActivity.class);

        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferencesUtil.myPreferences, MODE_PRIVATE);
        SharedPreferencesUtil.saveToSharedPreferences(sharedPreferences, SharedPreferencesUtil.token, token);
        Log.d("sharedPreferences","saved token to shared preferences");

        // todo: replace with real call to api to get a user's value
        //int netWorth = getNetWorth();
        SharedPreferencesUtil.saveToSharedPreferences(sharedPreferences, SharedPreferencesUtil.networth, 6000);
        startActivity(explicitIntent);
    }

    private int getNetWorth() {
        //int token = SharedPreferencesUtil.getIntFromSharedPreferences(getSharedPreferences(SharedPreferencesUtil.myPreferences, Context.MODE_PRIVATE), SharedPreferencesUtil.token);

        return 6000;
    }
}
