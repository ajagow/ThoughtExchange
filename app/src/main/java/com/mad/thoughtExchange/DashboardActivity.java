package com.mad.thoughtExchange;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    final Fragment fragment1 = new HomeFeedFragment();
    final Fragment fragment2 = new NewContentFragment();
    final Fragment fragment3 = new HomeInvestFragment();
    final Fragment fragment4 = new WalletMyIdeasFragment();
    final Fragment fragment5 = new WalletMyInvestmentsFragment();

    final FragmentManager fm = getSupportFragmentManager();

    Button tab1;
    Button tab2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        fm.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.main_container, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.main_container, fragment5, "5").hide(fragment5).commit();
        fm.beginTransaction().add(R.id.main_container, fragment1, "1").commit();

        SpaceNavigationView spaceNavigationView = findViewById(R.id.space);
        RelativeLayout tabHeader = findViewById(R.id.tab_header_and_line);

        tab1 = findViewById(R.id.tab_feed);
        tab2 = findViewById(R.id.tab_invest);

        NavBarSetupUtil navBarSetupUtil = new NavBarSetupUtil();
        navBarSetupUtil.setupNavBar(savedInstanceState, spaceNavigationView, fm, tabHeader, tab1, tab2);




    }


//    private void click() {
//
//        investTabButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Fragment s = fm.findFragmentByTag("3");
//                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).hide().show(s).commit();
//
//                View nav = findViewById(R.id.navbar_line);
//
//                Log.d("HERE", "here");
//
//                ObjectAnimator textViewAnimator = ObjectAnimator.ofFloat(nav, "translationX",0f,550f);
//                textViewAnimator.setDuration(750);
//                textViewAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
//
//                AnimatorSet set = new AnimatorSet();
//                set.play(textViewAnimator);
//                set.start();
//
//
//            }
//        });
//    }
}
