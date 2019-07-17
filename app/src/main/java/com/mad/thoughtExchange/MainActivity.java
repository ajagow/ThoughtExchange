package com.mad.thoughtExchange;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    static String URL = "https://blog-api-tutorial1.herokuapp.com/";
    private static String USERS_PATH = "api/v1/users/login";

    private TextView email;
    private TextView password;
    private Button submitButton;
    private Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                Toast.makeText(getApplicationContext(), "Error:  " + error.networkResponse.toString() + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        GsonRequest<LoginModel, LoginResponse> gsonRequest = new GsonRequest<>(Request.Method.POST, URL + USERS_PATH, loginModel, this,
                LoginModel.class, LoginResponse.class, new HashMap<String, String>(), responseListener, errorListener);

        gsonRequest.volley();

}

    // on successful login attempt, go to HomeActivity
    private void onSuccessfulLogin(LoginResponse loginResponse) {
        Intent explicitIntent = new Intent(MainActivity.this, DashboardActivity.class);
        explicitIntent.putExtra("jwtToken", loginResponse.getToken());
        startActivity(explicitIntent);
    }

}
