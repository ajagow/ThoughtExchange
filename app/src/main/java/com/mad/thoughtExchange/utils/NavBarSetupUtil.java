package com.mad.thoughtExchange.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.mad.thoughtExchange.R;

/**
 * Utility for setting up the bottom navbar navigation and all fragment changing logic.
 */
public class NavBarSetupUtil {

    private static final String HOME_ITEM = "HOME";
    private static final String WALLET_ITEM = "WALLET";
    private static final String HISTORY_ITEM = "HISTORY";
    private static final String RANKING_ITEM = "RANKING";

    public static void setupNavBar(Bundle savedInstanceState,
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
        spaceNavigationView.addSpaceItem(new SpaceItem(HOME_ITEM, R.drawable.navigation_icon_home));
        spaceNavigationView.addSpaceItem(new SpaceItem(WALLET_ITEM, R.drawable.navigation_icon_history));
        spaceNavigationView.addSpaceItem(new SpaceItem(HISTORY_ITEM, R.drawable.navigation_icon_likehistory));
        spaceNavigationView.addSpaceItem(new SpaceItem(RANKING_ITEM, R.drawable.navigation_icon_ranking));

        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                tabHeader.setVisibility(View.INVISIBLE);
                Fragment active = FragmentUtil.getVisibleFragment(fm);
                Fragment centerFragment = fm.findFragmentByTag("newContentFragment");
                fm.beginTransaction().setCustomAnimations(R.anim.slide_up, R.anim.fade_out).hide(active).show(centerFragment).commit();
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

    // When HOME button or WALLET button are clicked in nav bar, change tab names on top
    // and which fragments are visible
    public static void onAnyMenuItemClick(FragmentManager fm,
        String itemName, RelativeLayout tabHeader,
        Button tab1, Button tab2, View nav) {

        tabHeader.setVisibility(View.VISIBLE);
        Fragment active = FragmentUtil.getVisibleFragment(fm);
        Fragment newFragment = null;

        switch (itemName) {
            case HISTORY_ITEM:
                tabHeader.setVisibility(View.INVISIBLE);
                newFragment = fm.findFragmentByTag("historyFragment");
                break;
            case RANKING_ITEM:
                tabHeader.setVisibility(View.INVISIBLE);
                newFragment = fm.findFragmentByTag("rankingFragment");
                break;
            case HOME_ITEM:
                newFragment = fm.findFragmentByTag("homeFeedFragment");
                changeTabNames(tab1, tab2, true);
                click(tab1, "homeInvestFragment", "homeFeedFragment", null, fm, true, nav);
                click(tab2, "homeFeedFragment", "homeInvestFragment", null, fm, false, nav);
                break;
            default:
                changeTabNames(tab1, tab2, false);
                newFragment = fm.findFragmentByTag("walletMyIdeasFragment");
                click(tab1, "walletMyInvestmentsFragment", "walletMyIdeasFragment", null, fm, true, nav);
                click(tab2, "walletMyIdeasFragment", "walletMyInvestmentsFragment", null, fm, false, nav);
                break;
        }

        fm.beginTransaction()
                .hide(active)
                .show(newFragment)
                .addToBackStack(null)
                .commit();
    }

    // change which fragments are visible based on click
    private static void click(Button button, final String current,
        final String goTo, final View nav,
        final FragmentManager fm, final boolean swipeLeft,
        final View navLine) {

        final int enter;
        final int exit;

        moveLineNoAnimation(navLine);

        if (!swipeLeft) {
            enter = R.anim.enter_from_right;
            exit = R.anim.exit_to_left;
        }
        else {
            enter = R.anim.enter_from_left;
            exit = R.anim.exit_to_right;
        }

        // animate changing fragments
        button.setOnClickListener(view -> {
            Fragment currentFrag = fm.findFragmentByTag(current);
            Fragment goToFragment = fm.findFragmentByTag(goTo);
            fm.beginTransaction()
                    .setCustomAnimations(enter, exit)
                    .hide(currentFrag)
                    .show(goToFragment)
                    .commit();

            if (swipeLeft) {
                moveLine(navLine, 550f, 0f);
            }
            else {
                moveLine(navLine, 0f, 550f);
            }
        });
    }

    // change name of tabs on top depending on which button is clicked
    private static void changeTabNames(Button tab1, Button tab2, boolean isHome) {
        if (isHome) {
            tab1.setText(R.string.vote);
            tab2.setText(R.string.investments);
        }
        else {
            tab1.setText(R.string.my_ideas);
            tab2.setText(R.string.my_investments);
        }
    }

    // move line with left position start and left position end
    private static void moveLine(View nav, float start, float end) {
        if (Float.compare(nav.getTranslationX(), start) == 0 ) {

            ObjectAnimator textViewAnimator = ObjectAnimator.ofFloat(nav, "translationX",start, end);
            textViewAnimator.setDuration(300);

            AnimatorSet set = new AnimatorSet();
            set.play(textViewAnimator);
            set.start();
        }
    }

    // move line without animating
    private static void moveLineNoAnimation(View nav) {
        nav.setTranslationX(0f);
    }
}
