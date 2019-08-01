package com.mad.thoughtExchange;

import android.content.Context;
import android.content.SharedPreferences;
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

    final FragmentManager fm = getSupportFragmentManager();

    final Fragment homeFeedFragment = new HomeFeedSwipeFragment();
    final Fragment newContentFragment = new NewContentFragment();
    final Fragment homeInvestFragment = new HomeInvestFragment();
    final Fragment walletMyIdeasFragment = new WalletMyIdeasFragment();
    final Fragment historyFragment = new VotesFragment();
    final Fragment rankingFragment = new RankingFragment();
    final Fragment walletMyInvestmentsFragment = new WalletMyInvestmentsFragment();

    Button tab1;
    Button tab2;
    //ImageView logout;
    LinearLayout userDetail;
    View navLine;

    private int worthVal;
    private TextView uWorth;

    private static String USERS_PATH = "api/v1/users/me";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        setViews();
        RelativeLayout tabHeader = findViewById(R.id.tab_header_and_line);

        SpaceNavigationView spaceNavigationView = findViewById(R.id.space);
        spaceNavigationView.setCentreButtonIcon(R.drawable.plus_icon);
        spaceNavigationView.setInActiveCentreButtonIconColor(ContextCompat.getColor(this,R.color.white));

        // uncomment only if starting app from dashboard activity
//        String token1 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1NjkyNTIzNDYsImlhdCI6MTU2NDA2ODM0Niwic3ViIjoxfQ.hF3_Iyq9wxiA5kvpZkiuZCzzhCzld0keORhvtN7yNSM";
//        int netWorth = SharedPreferencesUtil.getIntFromSharedPreferences(getSharedPreferences(SharedPreferencesUtil.myPreferences, Context.MODE_PRIVATE), SharedPreferencesUtil.networth);
//        coins.setText(Integer.toString(netWorth));

        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferencesUtil.myPreferences, MODE_PRIVATE);

        setFragmentManager();

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

        Response.Listener<UserResponse> responseListener = new Response.Listener<UserResponse>() {
            @Override
            public void onResponse(UserResponse response) {
                worthVal = response.getNetWorth();
                Log.d("WORTHVAL", String.valueOf(worthVal));
                SharedPreferencesUtil.saveToSharedPreferences(sharedPreferences, SharedPreferencesUtil.networth, worthVal);
                uWorth.setText(String.valueOf(SharedPreferencesUtil.getIntFromSharedPreferences(sharedPreferences, SharedPreferencesUtil.networth)));
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", "user settings error");
            }
        };

        Map<String, String> headers = new HashMap<>();
        String token = SharedPreferencesUtil.getStringFromSharedPreferences(getSharedPreferences(SharedPreferencesUtil.myPreferences, Context.MODE_PRIVATE), SharedPreferencesUtil.token);
        headers.put("api-token", token);

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

    private void setFragmentManager() {
        fm.beginTransaction().add(R.id.main_container, newContentFragment, "newContentFragment").hide(newContentFragment).commit();
        fm.beginTransaction().add(R.id.main_container, homeInvestFragment, "homeInvestFragment").hide(homeInvestFragment).commit();
        fm.beginTransaction().add(R.id.main_container, historyFragment, "historyFragment").hide(historyFragment).commit();
        fm.beginTransaction().add(R.id.main_container, rankingFragment, "rankingFragment").hide(rankingFragment).commit();
        fm.beginTransaction().add(R.id.main_container, walletMyIdeasFragment, "walletMyIdeasFragment").hide(walletMyIdeasFragment).commit();
        fm.beginTransaction().add(R.id.main_container, walletMyInvestmentsFragment, "walletMyInvestmentsFragment").hide(walletMyInvestmentsFragment).commit();
        fm.beginTransaction().add(R.id.main_container, homeFeedFragment, "homeFeedFragment").commit();
    }

    private void setViews() {
        tab1 = findViewById(R.id.tab_feed);
        tab2 = findViewById(R.id.tab_invest);
//        logout = findViewById(R.id.logout);
        navLine = findViewById(R.id.navbar_line);
        tab1 = findViewById(R.id.tab_feed);
        tab2 = findViewById(R.id.tab_invest);
        userDetail = findViewById(R.id.userDetailButton);
        navLine = findViewById(R.id.navbar_line);
        uWorth = findViewById(R.id.worth);
    }
}
