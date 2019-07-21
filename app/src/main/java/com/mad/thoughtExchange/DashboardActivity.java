package com.mad.thoughtExchange;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.luseen.spacenavigation.SpaceNavigationView;
import com.mad.thoughtExchange.utils.NavBarSetupUtil;
import com.mad.thoughtExchange.utils.SharedPreferencesUtil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class DashboardActivity extends AppCompatActivity {

    final Fragment homeFeedFragment = new HomeFeedFragment();
    final Fragment newContentFragment = new NewContentFragment();
    final Fragment homeInvestFragment = new HomeInvestFragment();
    final Fragment walletMyIdeasFragment = new WalletMyIdeasFragment();
    final Fragment walletMyInvestmentsFragment = new WalletMyInvestmentsFragment();

    final FragmentManager fm = getSupportFragmentManager();

    Button tab1;
    Button tab2;
    ImageView logout;
    TextView coins;
    View navLine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        tab1 = findViewById(R.id.tab_feed);
        tab2 = findViewById(R.id.tab_invest);
        logout = findViewById(R.id.logout);
        coins = findViewById(R.id.coins);
        navLine = findViewById(R.id.navbar_line);
        RelativeLayout tabHeader = findViewById(R.id.tab_header_and_line);
        SpaceNavigationView spaceNavigationView = findViewById(R.id.space);

        // uncomment only if starting app from dashboard activity
        //String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1Njg4MjU3MTksImlhdCI6MTU2MzY0MTcxOSwic3ViIjoyfQ.8yMMptQRI9w6ltgOmBM0827b4trzQ16WavXfB_aHKuQ";
        //SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferencesUtil.myPreferences, MODE_PRIVATE);
        //SharedPreferencesUtil.saveToSharedPreferences(sharedPreferences, SharedPreferencesUtil.token, token);
        //SharedPreferencesUtil.saveToSharedPreferences(sharedPreferences, SharedPreferencesUtil.networth, 5000);

        fm.beginTransaction().add(R.id.main_container, newContentFragment, "newContentFragment").hide(newContentFragment).commit();
        fm.beginTransaction().add(R.id.main_container, homeInvestFragment, "homeInvestFragment").hide(homeInvestFragment).commit();
        fm.beginTransaction().add(R.id.main_container, walletMyIdeasFragment, "walletMyIdeasFragment").hide(walletMyIdeasFragment).commit();
        fm.beginTransaction().add(R.id.main_container, walletMyInvestmentsFragment, "walletMyInvestmentsFragment").hide(walletMyInvestmentsFragment).commit();
        fm.beginTransaction().add(R.id.main_container, homeFeedFragment, "homeFeedFragment").commit();

        spaceNavigationView.setCentreButtonIcon(R.drawable.plus_icon);
        spaceNavigationView.setInActiveCentreButtonIconColor(ContextCompat.getColor(this,R.color.white));

        int netWorth = SharedPreferencesUtil.getIntFromSharedPreferences(getSharedPreferences(SharedPreferencesUtil.myPreferences, Context.MODE_PRIVATE), SharedPreferencesUtil.networth);
        coins.setText(Integer.toString(netWorth));

        NavBarSetupUtil navBarSetupUtil = new NavBarSetupUtil();
        navBarSetupUtil.setupNavBar(savedInstanceState, spaceNavigationView, fm, tabHeader, tab1, tab2, navLine);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent explicitIntent = new Intent(DashboardActivity.this, MainActivity.class);
                startActivity(explicitIntent);
            }
        });
    }
}
