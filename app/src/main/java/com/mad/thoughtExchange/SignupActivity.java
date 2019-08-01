package com.mad.thoughtExchange;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.mad.thoughtExchange.utils.SharedPreferencesUtil;

import org.json.JSONObject;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    private static String USERS_PATH = "api/v1/users/";

    private TextView name;
    private TextView email;
    private TextView password;
    private int passwordRequiredLength;
    private boolean passwordIsValidLength;
    private TextView passwordValidLength;
    private Button signup_btn;
    private Button back_to_login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        back_to_login_btn = findViewById(R.id.back_to_login_btn);
        signup_btn = findViewById(R.id.signup_btn);
        passwordValidLength = findViewById(R.id.pass_validation);
        passwordIsValidLength = false;
        passwordRequiredLength = 8;

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwordIsValidLength && name.length() > 0 && email.length() > 0 && email.length() < 26) {
                    onSignup(view);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Check fields again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back_to_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent explicitIntent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(explicitIntent);
            }
        });

        setTextWatcherForPasswordCounter();
    }

    public void onSignup(View view) {
//        SignupResponse response = new SignupResponse();
//        response.setToken("lkdsjf");
//        onSuccessfulSignup(response);

        String nameVal = name.getText().toString();
        String emailVal = email.getText().toString();
        String passwordVal = password.getText().toString();

        SignupModel signupModel = new SignupModel();
        signupModel.setName(nameVal);
        signupModel.setEmail(emailVal);
        signupModel.setPassword(passwordVal);

        Response.Listener<SignupResponse> responseListener = new Response.Listener<SignupResponse>() {
            @Override
            public void onResponse(SignupResponse response) {
                onSuccessfulSignup(response);
                Log.d("OnResponse", response.getToken()); ///
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", error.networkResponse.toString());
                Toast.makeText(getApplicationContext(), "Error:  " + error.networkResponse.toString() + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        GsonRequest<SignupModel, SignupResponse> gsonRequest = new GsonRequest<>(Request.Method.POST, MainActivity.URL + USERS_PATH, signupModel, this,
                SignupModel.class, SignupResponse.class, new HashMap<String, String>(), responseListener, errorListener);

        gsonRequest.volley();
    }

    private void setTextWatcherForPasswordCounter() {
        TextWatcher mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // if password is longer than required length
                if (s.length() >= passwordRequiredLength) {
                    passwordValidLength.setText("Password meets requirements");
                    passwordIsValidLength = true;
                }
                else {
                    String passValidationText = "Your password needs "
                            + (passwordRequiredLength - s.length())
                            + " more characters";
                    passwordValidLength.setText(passValidationText);
                    passwordIsValidLength = false;
                }
            }

            public void afterTextChanged(Editable s) {
            }
        };
        password.addTextChangedListener(mTextEditorWatcher);
    }

    // on successful sign up attempt, go to HomeActivity
    private void onSuccessfulSignup(SignupResponse signupResponse) {
        Intent explicitIntent = new Intent(SignupActivity.this, DashboardActivity.class);
        explicitIntent.putExtra("jwtToken", signupResponse.getToken());

        String token = signupResponse.getToken();
        Log.d("loginResponseToken",token);

        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferencesUtil.myPreferences, MODE_PRIVATE);
        SharedPreferencesUtil.saveToSharedPreferences(sharedPreferences, SharedPreferencesUtil.token, token);
        Log.d("sharedPreferences","saved token to shared preferences");

        SharedPreferencesUtil.saveToSharedPreferences(sharedPreferences, SharedPreferencesUtil.networth, 6000);
        startActivity(explicitIntent);
    }
}
