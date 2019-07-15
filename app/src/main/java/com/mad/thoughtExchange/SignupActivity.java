package com.mad.thoughtExchange;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.Volley;
import com.mad.thoughtExchange.models.GsonRequest;
import com.mad.thoughtExchange.models.SignupModel;
import com.mad.thoughtExchange.responses.SignupResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    private static String URL = "https://blog-api-tutorial1.herokuapp.com/";
    private static String USERS_PATH = "api/v1/users";

    private TextView name;
    private TextView email;
    private TextView password;
    private Button signup_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signup_btn = findViewById(R.id.signup_btn);
        System.out.println("reached here");
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSignup(view);
            }
        });
    }

    public void onSignup(View view) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject jsonBody = new JSONObject();

        String nameVal = name.getText().toString();
        String emailVal = email.getText().toString();
        String passwordVal = password.getText().toString();

        SignupModel signupModel = new SignupModel();
        signupModel.setEmail(nameVal);
        signupModel.setEmail(emailVal);
        signupModel.setPassword(passwordVal);

        System.out.println(nameVal); ///
        System.out.println(emailVal); ///
        System.out.println(passwordVal); ///

        Response.Listener<SignupResponse> responseListener = new Response.Listener<SignupResponse>() {
            @Override
            public void onResponse(SignupResponse response) {
                onSuccessfulSignup(response);
                System.out.println(response.getToken()); ///
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error:  " + error.networkResponse.toString() + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        GsonRequest<SignupModel, SignupResponse> gsonRequest = new GsonRequest<>(Request.Method.POST, URL + USERS_PATH, signupModel, this,
                SignupModel.class, SignupResponse.class, new HashMap<String, String>(), responseListener, errorListener);

        gsonRequest.volley();

    }

    // on successful login attempt, go to HomeActivity
    private void onSuccessfulSignup(SignupResponse signupResponse) {
        Intent explicitIntent = new Intent(SignupActivity.this, HomeActivity.class);
        explicitIntent.putExtra("jwtToken", signupResponse.getToken());
        startActivity(explicitIntent);
    }
}
