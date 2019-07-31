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
import com.mad.thoughtExchange.HomeFeedFragment;
import com.mad.thoughtExchange.HomeInvestFragment;
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
        Log.d("NavBarSetup","set up tab1, tab2 clicks");

        spaceNavigationView.showIconOnly();
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem("HOME", R.drawable.home));
        spaceNavigationView.addSpaceItem(new SpaceItem("WALLET", R.drawable.wallet));
        spaceNavigationView.addSpaceItem(new SpaceItem("HISTORY", R.drawable.history));
        spaceNavigationView.addSpaceItem(new SpaceItem("RANKING", R.drawable.ranking));

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
                onAnyMenuItemClick(fm, itemName, tabHeader, tab1, tab2, nav);
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                onAnyMenuItemClick(fm, itemName, tabHeader, tab1, tab2, nav);
            }
        });
    }

    // get the fragment that's currently visible
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

    // When HOME button or WALLET button are clicked in nav bar, change tab names on top
    // and which fragments are visible
    private void onAnyMenuItemClick(FragmentManager fm,
                                    String itemName, RelativeLayout tabHeader,
                                    Button tab1, Button tab2, View nav) {
        tabHeader.setVisibility(View.VISIBLE);
        Fragment active = getVisibleFragment(fm);
        Fragment newFragment = null;
        Log.d("ITEMNAME", itemName);
        if (itemName.equals("HOME")) {
            newFragment = fm.findFragmentByTag("homeFeedFragment");

            changeTabNames(tab1, tab2, true);
            click(tab1, "homeInvestFragment", "homeFeedFragment", null,fm, true, nav);
            click(tab2, "homeFeedFragment", "homeInvestFragment", null,fm, false, nav);
            Log.d("CLICK", "SWITCH TO homeFeedFragment");
        } else if (itemName.equals("HISTORY")) {
            Log.d("NAVBAR", "itemName.equals(HISTORY)");
            tabHeader.setVisibility(View.INVISIBLE);
            newFragment = fm.findFragmentByTag("historyFragment");

        } else if (itemName.equals("RANKING")) {
            tabHeader.setVisibility(View.INVISIBLE);
            newFragment = fm.findFragmentByTag("rankingFragment");
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

    // change which fragments are visible based on click
    private void click(Button button, final String current,
                       final String goTo, final View nav, final FragmentManager fm,
                       final boolean swipeLeft, final View navLine) {

        final int enter;
        final int exit;

        Log.d("CLICK HERE", "current: " + current + "    goto: " + goTo);
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
                    moveLine(navLine, 550f, 0f);
                }
                else {
                    moveLine(navLine, 0f, 550f);
                }
            }
        });
    }

    // change name of tabs on top depending on which button is clicked
    private void changeTabNames(Button tab1, Button tab2, boolean isHome) {
        if (isHome) {
            tab1.setText(R.string.vote);
            tab2.setText(R.string.investments);
        }
        else {
            tab1.setText(R.string.my_ideas);
            tab2.setText(R.string.my_investments);
        }
    }

    // move line without animating
    private void moveLineNoAnimation(View nav) {
        nav.setTranslationX(0f);
    }

    // move line with left position start and left position end
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
