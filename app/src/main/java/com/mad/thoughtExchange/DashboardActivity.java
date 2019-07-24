package com.mad.thoughtExchange;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.mad.thoughtExchange.models.GsonRequest;
import com.mad.thoughtExchange.responses.UserResponse;
import com.mad.thoughtExchange.utils.NavBarSetupUtil;
import com.mad.thoughtExchange.utils.SharedPreferencesUtil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;


public class DashboardActivity extends AppCompatActivity {

    //final Fragment homeFeedFragment = new HomeFeedFragment();
    final Fragment homeFeedFragment = new HomeFeedSwipeFragment();
    final Fragment newContentFragment = new NewContentFragment();
    final Fragment homeInvestFragment = new HomeInvestFragment();
    final Fragment walletMyIdeasFragment = new WalletMyIdeasFragment();
    final Fragment walletMyInvestmentsFragment = new WalletMyInvestmentsFragment();


    final FragmentManager fm = getSupportFragmentManager();

    Button tab1;
    Button tab2;
    ImageView logout;
    LinearLayout userDetail;
    TextView coins;
    View navLine;


    private int worthVal;
    private TextView uWorth;

    private static String USERS_PATH = "api/v1/users/me";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        tab1 = findViewById(R.id.tab_feed);
        tab2 = findViewById(R.id.tab_invest);
//        logout = findViewById(R.id.logout);
        coins = findViewById(R.id.coins);
        navLine = findViewById(R.id.navbar_line);
        RelativeLayout tabHeader = findViewById(R.id.tab_header_and_line);
        SpaceNavigationView spaceNavigationView = findViewById(R.id.space);

        // uncomment only if starting app from dashboard activity
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1Njg4MjU3MTksImlhdCI6MTU2MzY0MTcxOSwic3ViIjoyfQ.8yMMptQRI9w6ltgOmBM0827b4trzQ16WavXfB_aHKuQ";
        //SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferencesUtil.myPreferences, MODE_PRIVATE);
        //SharedPreferencesUtil.saveToSharedPreferences(sharedPreferences, SharedPreferencesUtil.token, token);
        //SharedPreferencesUtil.saveToSharedPreferences(sharedPreferences, SharedPreferencesUtil.networth, 5000);

        fm.beginTransaction().add(R.id.main_container, newContentFragment, "newContentFragment").hide(newContentFragment).commit();
        fm.beginTransaction().add(R.id.main_container, homeInvestFragment, "homeInvestFragment").hide(homeInvestFragment).commit();
        fm.beginTransaction().add(R.id.main_container, walletMyIdeasFragment, "walletMyIdeasFragment").hide(walletMyIdeasFragment).commit();
        fm.beginTransaction().add(R.id.main_container, walletMyInvestmentsFragment, "walletMyInvestmentsFragment").hide(walletMyInvestmentsFragment).commit();
        fm.beginTransaction().add(R.id.main_container, homeFeedFragment, "homeFeedFragment").commit();

        tab1 = findViewById(R.id.tab_feed);
        tab2 = findViewById(R.id.tab_invest);

//        logout = findViewById(R.id.logout);
        userDetail = findViewById(R.id.userDetailButton);

        navLine = findViewById(R.id.navbar_line);

        spaceNavigationView.setCentreButtonIcon(R.drawable.plus_icon);
        spaceNavigationView.setInActiveCentreButtonIconColor(ContextCompat.getColor(this,R.color.white));

//        int netWorth = SharedPreferencesUtil.getIntFromSharedPreferences(getSharedPreferences(SharedPreferencesUtil.myPreferences, Context.MODE_PRIVATE), SharedPreferencesUtil.networth);
//        coins.setText(Integer.toString(netWorth));

        NavBarSetupUtil navBarSetupUtil = new NavBarSetupUtil();
        navBarSetupUtil.setupNavBar(savedInstanceState, spaceNavigationView, fm, tabHeader, tab1, tab2, navLine);

        userDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserSettings userSettings = new UserSettings();
                userSettings.show(fm, "fragment_user_settings");
            }
        });

        /*logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent explicitIntent = new Intent(DashboardActivity.this, MainActivity.class);
                startActivity(explicitIntent);
            }
        });*/

        uWorth = findViewById(R.id.worth);

        Response.Listener<UserResponse> responseListener = new Response.Listener<UserResponse>() {
            @Override
            public void onResponse(UserResponse response) {
                worthVal = response.getNetWorth();
                Log.d("WORTHVAL", String.valueOf(worthVal));

                uWorth.setText(String.valueOf(worthVal));

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", "user settings error");
            }
        };

        Map<String, String> headers = new HashMap<>();
        headers.put("api-token", token);
        headers.put("Content-Type", "application/json");

        GsonRequest<String, UserResponse> gsonRequest = new GsonRequest<String, UserResponse>(
                MainActivity.URL + USERS_PATH,
                this,
                UserResponse.class,
                responseListener,
                errorListener,
                headers
        );

        gsonRequest.volley();


    }
}
