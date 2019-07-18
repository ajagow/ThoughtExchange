package com.mad.thoughtExchange.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.mad.thoughtExchange.R;

import java.util.List;

public class NavBarSetupUtil {

    public void setupNavBar(Bundle savedInstanceState,
                            final SpaceNavigationView spaceNavigationView,
                            final FragmentManager fm,
                            final RelativeLayout tabHeader,
                            final Button tab1,
                            final Button tab2,
                            final View nav) {

        click(tab1, "homeInvestFragment", "homeFeedFragment", null,fm, true, nav);
        click(tab2, "homeFeedFragment", "homeInvestFragment", null,fm, false, nav);

        spaceNavigationView.showIconOnly();
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem("HOME", R.drawable.home));
        spaceNavigationView.addSpaceItem(new SpaceItem("WALLET", R.drawable.wallet));

        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                tabHeader.setVisibility(View.INVISIBLE);
                Fragment active = getVisibleFragment(fm);
                Fragment centerFragment = fm.findFragmentByTag("newContentFragment");
                fm.beginTransaction().setCustomAnimations(R.anim.slide_up, R.anim.fade_out).hide(active).show(centerFragment).commit();
                Log.d("CLICK",  "  activetag: " + active.getTag());
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                onAnyItemClick(fm, itemName, tabHeader, tab1, tab2, nav);
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                onAnyItemClick(fm, itemName, tabHeader, tab1, tab2, nav);
            }
        });
    }

    private Fragment getVisibleFragment(FragmentManager fragmentManager){
        List<Fragment> fragments = fragmentManager.getFragments();

        if(fragments != null){
            for(Fragment fragment : fragments){
                if(fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    private void onAnyItemClick(FragmentManager fm,
                                String itemName, RelativeLayout tabHeader,
                                Button tab1, Button tab2, View nav) {
        tabHeader.setVisibility(View.VISIBLE);
        Fragment active = getVisibleFragment(fm);
        Fragment newFragment = null;
        if (itemName.equals("HOME")) {
            newFragment = fm.findFragmentByTag("homeFeedFragment");
            changeTabNames(tab1, tab2, true);
            click(tab1, "homeInvestFragment", "homeFeedFragment", null,fm, true, nav);
            click(tab2, "homeFeedFragment", "homeInvestFragment", null,fm, false, nav);
            Log.d("CLICK", "SWITCH TO homeFeedFragment");
        }
        else {
            changeTabNames(tab1, tab2, false);
            newFragment = fm.findFragmentByTag("walletMyIdeasFragment");
            click(tab1, "walletMyInvestmentsFragment", "walletMyIdeasFragment", null,fm, true, nav);
            click(tab2, "walletMyIdeasFragment", "walletMyInvestmentsFragment", null,fm, false, nav);
            Log.d("CLICK", "SWITCH TO walletMyIdeasFragment");
        }
        fm.beginTransaction().hide(active).show(newFragment).commit();
    }

    private void click(Button button, final String current,
                       final String goTo, final View nav, final FragmentManager fm,
                       final boolean swipeLeft, final View navLine) {

        final int enter;
        final int exit;

        Log.d("CLICK HERE", "kdfls");
        moveLineNoAnimation(navLine);

        if (!swipeLeft) {
            enter = R.anim.enter_from_right;
            exit = R.anim.exit_to_left;
        }
        else {
            enter = R.anim.enter_from_left;
            exit = R.anim.exit_to_right;
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment currentFrag = fm.findFragmentByTag(current);
                Fragment goToFragment = fm.findFragmentByTag(goTo);
                fm.beginTransaction().setCustomAnimations(enter, exit).hide(currentFrag).show(goToFragment).commit();

                if (swipeLeft) {
                    moveLine(navLine, 500f, 0f);
                }
                else {
                    moveLine(navLine, 0f, 500f);
                }
            }
        });
    }

    private void changeTabNames(Button tab1, Button tab2, boolean isHome) {
        if (isHome) {
            tab1.setText(R.string.feed);
            tab2.setText(R.string.investments);
        }
        else {
            tab1.setText(R.string.my_ideas);
            tab2.setText(R.string.my_investments);
        }
    }

    private void moveLineNoAnimation(View nav) {
        nav.setTranslationX(0f);
    }

    private void moveLine(View nav, float start, float end) {
        if (Float.compare(nav.getTranslationX(), start) == 0 ) {

            ObjectAnimator textViewAnimator = ObjectAnimator.ofFloat(nav, "translationX",start, end);
            textViewAnimator.setDuration(300);

            AnimatorSet set = new AnimatorSet();
            set.play(textViewAnimator);
            set.start();
        }

    }

}
