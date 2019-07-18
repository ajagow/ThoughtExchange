package com.mad.thoughtExchange;

import android.content.Intent;
import android.os.Bundle;


import com.luseen.spacenavigation.SpaceNavigationView;
import com.mad.thoughtExchange.utils.NavBarSetupUtil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;


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
    View navLine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        fm.beginTransaction().add(R.id.main_container, newContentFragment, "newContentFragment").hide(newContentFragment).commit();
        fm.beginTransaction().add(R.id.main_container, homeInvestFragment, "homeInvestFragment").hide(homeInvestFragment).commit();
        fm.beginTransaction().add(R.id.main_container, walletMyIdeasFragment, "walletMyIdeasFragment").hide(walletMyIdeasFragment).commit();
        fm.beginTransaction().add(R.id.main_container, walletMyInvestmentsFragment, "walletMyInvestmentsFragment").hide(walletMyInvestmentsFragment).commit();
        fm.beginTransaction().add(R.id.main_container, homeFeedFragment, "homeFeedFragment").commit();

        SpaceNavigationView spaceNavigationView = findViewById(R.id.space);
        RelativeLayout tabHeader = findViewById(R.id.tab_header_and_line);

        tab1 = findViewById(R.id.tab_feed);
        tab2 = findViewById(R.id.tab_invest);

        logout = findViewById(R.id.logout);

        navLine = findViewById(R.id.navbar_line);

        spaceNavigationView.setCentreButtonIcon(R.drawable.plus_icon);
        spaceNavigationView.setInActiveCentreButtonIconColor(ContextCompat.getColor(this,R.color.white));


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
