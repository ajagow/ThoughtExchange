package com.mad.thoughtExchange;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.luseen.spacenavigation.SpaceOnLongClickListener;
import com.mad.thoughtExchange.utils.NavBarSetupUtil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    final Fragment fragment1 = new HomeFeedFragment();
    final Fragment fragment2 = new NewContentFragment();
    final Fragment fragment3 = new HomeInvestFragment();
    final FragmentManager fm = getSupportFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        fm.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.main_container, fragment1, "1").commit();

        SpaceNavigationView spaceNavigationView = findViewById(R.id.space);

        NavBarSetupUtil navBarSetupUtil = new NavBarSetupUtil();
        navBarSetupUtil.setupNavBar(savedInstanceState, spaceNavigationView, fm);

    }
}
