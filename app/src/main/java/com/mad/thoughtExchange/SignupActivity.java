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
    private static String USERS_PATH = "api/v1/users/";

    private TextView email;
    private TextView password;
    private Button login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        login_btn = findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                this.onClick(view);
            }
        });
    }

    public void onClick(View view) throws JSONException {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject jsonBody = new JSONObject();

        String emailVal = email.getText().toString();
        String passwordVal = password.getText().toString();

        SignupModel signupModel = new SignupModel();
        signupModel.setEmail(emailVal);
        signupModel.setPassword(passwordVal);

        Response.Listener<SignupResponse> responseListener = new Response.Listener<SignupResponse>() {
            @Override
            public void onResponse(SignupResponse response) {
                onSuccessfulLogin(response);
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
    private void onSuccessfulLogin(SignupResponse loginResponse) {
        Intent explicitIntent = new Intent(SignupActivity.this, HomeActivity.class);
        explicitIntent.putExtra("jwtToken", loginResponse.getToken());
        startActivity(explicitIntent);
    }
}
