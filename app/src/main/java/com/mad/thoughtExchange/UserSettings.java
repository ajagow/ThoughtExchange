package com.mad.thoughtExchange;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.mad.thoughtExchange.models.GsonRequest;
import com.mad.thoughtExchange.responses.FeedPostResponse;
import com.mad.thoughtExchange.responses.UserResponse;
import com.mad.thoughtExchange.utils.SharedPreferencesUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class UserSettings extends DialogFragment {
    private static final String USER_NAME = "name";
    private static final String USER_EMAIL = "email";

    private String nameVal;
    private String emailVal;
    private static String USERS_PATH = "api/v1/users/me";

    public UserSettings() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @return A new instance of fragment UserSettings.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static UserSettings newInstance() {
////        String nameVal, String emailVal, int rankVal
//
//        UserSettings fragment = new UserSettings();
//        Bundle args = new Bundle();
//
//        args.putString(USER_NAME, "mel");
//        args.putString(USER_EMAIL, "maaaa");
//        args.putInt(USER_RANK, 3);
//        fragment.setArguments(args);
//        return fragment;
//    }

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
        Button logoutButton = view.findViewById(R.id.logout);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent explicitIntent = new Intent(getActivity(), MainActivity.class);
                startActivity(explicitIntent);
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

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", "user settings error");
            }
        };

        String token = SharedPreferencesUtil.getStringFromSharedPreferences(getActivity().getSharedPreferences(SharedPreferencesUtil.myPreferences, Context.MODE_PRIVATE), SharedPreferencesUtil.token);

        Map<String, String> headers = new HashMap<>();
        headers.put("api-token", token);
        headers.put("Content-Type", "application/json");


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
