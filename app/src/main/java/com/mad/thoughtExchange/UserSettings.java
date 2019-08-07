package com.mad.thoughtExchange;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.android.volley.Response;
import com.mad.thoughtExchange.models.GsonRequest;
import com.mad.thoughtExchange.responses.UserResponse;
import com.mad.thoughtExchange.utils.VolleyUtils;

import java.util.Map;


/**
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class UserSettings extends DialogFragment {
    private static final String USER_NAME = "name";
    private static final String USER_EMAIL = "email";

    private String nameVal;
    private String emailVal;
    private static String USERS_PATH = "api/v1/users/me";

    private Button logoutButton;
    private Button closeButton;

    public UserSettings() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nameVal = getArguments().getString(USER_NAME);
            emailVal = getArguments().getString(USER_EMAIL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user_settings, container, false);
        View headerView = inflater.inflate(R.layout.activity_header, container, false);

        final TextView uName = view.findViewById(R.id.user_popup_name);
        final TextView uEmail = view.findViewById(R.id.user_popup_email);
        final TextView uWorth = headerView.findViewById(R.id.worth);
        logoutButton = view.findViewById(R.id.logout);
        closeButton = view.findViewById(R.id.settingsClose);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent explicitIntent = new Intent(getActivity(), MainActivity.class);
                startActivity(explicitIntent);
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        Response.Listener<UserResponse> responseListener = new Response.Listener<UserResponse>() {
            @Override
            public void onResponse(UserResponse response) {
                nameVal = response.getName();
                emailVal = response.getEmail();

                uName.setText(nameVal);
                uEmail.setText(emailVal);

            }
        };

        Response.ErrorListener errorListener = VolleyUtils.logError("USER_SETTINGS");

        Map<String, String> headers = VolleyUtils.getAuthenticationHeader(getActivity());


        GsonRequest<String, UserResponse> gsonRequest = new GsonRequest<String, UserResponse>(
                MainActivity.URL + USERS_PATH,
                getContext(),
                UserResponse.class,
                responseListener,
                errorListener,
                headers
        );

        gsonRequest.volley();

        return view;
    }
}
