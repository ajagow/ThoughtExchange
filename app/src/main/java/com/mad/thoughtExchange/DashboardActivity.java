package com.mad.thoughtExchange;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Response;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.mad.thoughtExchange.models.GsonRequest;
import com.mad.thoughtExchange.responses.UserResponse;
import com.mad.thoughtExchange.utils.NavBarSetupUtil;
import com.mad.thoughtExchange.utils.SharedPreferencesUtil;
import com.mad.thoughtExchange.utils.VolleyUtils;

import java.util.Map;

/**
 * Activity that is in charge of changing out which fragments are active.
 */
public class DashboardActivity extends AppCompatActivity {

    final FragmentManager fm = getSupportFragmentManager();

    final Fragment homeFeedFragment = new HomeFeedSwipeFragment();
    final Fragment newContentFragment = new NewContentFragment();
    final Fragment homeInvestFragment = new HomeInvestFragment();
    final Fragment walletMyIdeasFragment = new WalletMyIdeasFragment();
    final Fragment historyFragment = new VotesFragment();
    final Fragment rankingFragment = new RankingFragment();
    final Fragment walletMyInvestmentsFragment = new WalletMyInvestmentsFragment();

    static String NEW_CONTENT_FRAGMENT_NAME = "newContentFragment";
    static String HOME_INVEST_FRAGMENT_NAME = "homeInvestFragment";
    static String HISTORY_FRAGMENT_NAME = "historyFragment";
    static String RANKING_FRAGMENT_NAME = "rankingFragment";
    static String WALLET_MY_IDEAS_FRAGMENT_NAME = "walletMyIdeasFragment";
    static String WALLET_MY_INVESTMENTS_FRAGMENT_NAME = "walletMyInvestmentsFragment";
    static String HOME_FEED_FRAGMENT_NAME = "homeFeedFragment";

    private Button tab1;
    private Button tab2;
    private LinearLayout userDetail;
    private View navLine;

    private int worthVal;
    private String userName;
    private TextView uWorth;
    private TextView uName;

    private static String USERS_PATH = "api/v1/users/me";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        initViews();
        RelativeLayout tabHeader = findViewById(R.id.tab_header_and_line);

        SpaceNavigationView spaceNavigationView = findViewById(R.id.space);
        spaceNavigationView.setCentreButtonIcon(R.drawable.plus_icon);
        spaceNavigationView.setInActiveCentreButtonIconColor(ContextCompat.getColor(this,R.color.white));

        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferencesUtil.myPreferences, MODE_PRIVATE);

        setFragmentManager();

        NavBarSetupUtil navBarSetupUtil = new NavBarSetupUtil();
        navBarSetupUtil.setupNavBar(savedInstanceState, spaceNavigationView, fm, tabHeader, tab1, tab2, navLine);

        userDetail.setOnClickListener(view -> {
            UserSettings userSettings = new UserSettings();
            userSettings.show(fm, "fragment_user_settings");
        });

        getUserSettings(sharedPreferences);

    }

    // get user settings from api
    private void getUserSettings(SharedPreferences sharedPreferences) {
        Response.Listener<UserResponse> responseListener = response -> {
            worthVal = response.getNetWorth();
            userName = response.getName();
            String headerDisplayName = userName.substring(0, 1).toUpperCase()
                    + userName.substring(1);

            if (userName.length() >= 10) {
                headerDisplayName = userName.substring(0, 10);
                headerDisplayName += "...";
            }

            SharedPreferencesUtil.saveToSharedPreferences(sharedPreferences, SharedPreferencesUtil.networth, worthVal);
            SharedPreferencesUtil.saveToSharedPreferences(sharedPreferences, SharedPreferencesUtil.userName, userName);
            uWorth.setText(String.valueOf(SharedPreferencesUtil.getIntFromSharedPreferences(sharedPreferences, SharedPreferencesUtil.networth)));
            uName.setText(headerDisplayName);
        };

        Response.ErrorListener errorListener = error -> Log.d("ERROR", "user settings error");

        Map<String, String> headers = VolleyUtils.getAuthenticationHeader(this);

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

    // set fragment manager by adding all the fragments
    private void setFragmentManager() {
        fm.beginTransaction().add(R.id.main_container, newContentFragment, NEW_CONTENT_FRAGMENT_NAME).hide(newContentFragment).commit();
        fm.beginTransaction().add(R.id.main_container, homeInvestFragment, HOME_INVEST_FRAGMENT_NAME).hide(homeInvestFragment).commit();
        fm.beginTransaction().add(R.id.main_container, historyFragment, HISTORY_FRAGMENT_NAME).hide(historyFragment).commit();
        fm.beginTransaction().add(R.id.main_container, rankingFragment, RANKING_FRAGMENT_NAME).hide(rankingFragment).commit();
        fm.beginTransaction().add(R.id.main_container, walletMyIdeasFragment, WALLET_MY_IDEAS_FRAGMENT_NAME).hide(walletMyIdeasFragment).commit();
        fm.beginTransaction().add(R.id.main_container, walletMyInvestmentsFragment, WALLET_MY_INVESTMENTS_FRAGMENT_NAME).hide(walletMyInvestmentsFragment).commit();
        fm.beginTransaction().add(R.id.main_container, homeFeedFragment, HOME_FEED_FRAGMENT_NAME).commit();
    }

    // init all view objects
    private void initViews() {
        tab1 = findViewById(R.id.tab_feed);
        tab2 = findViewById(R.id.tab_invest);
        navLine = findViewById(R.id.navbar_line);
        tab1 = findViewById(R.id.tab_feed);
        tab2 = findViewById(R.id.tab_invest);
        userDetail = findViewById(R.id.userDetailButton);
        navLine = findViewById(R.id.navbar_line);
        uWorth = findViewById(R.id.worth);
        uName = findViewById(R.id.headerUserName);
    }
}
