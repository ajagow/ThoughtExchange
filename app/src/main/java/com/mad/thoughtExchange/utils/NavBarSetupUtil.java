package com.mad.thoughtExchange.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.mad.thoughtExchange.R;

import java.util.List;

public class NavBarSetupUtil {

    public void setupNavBar(Bundle savedInstanceState, final SpaceNavigationView spaceNavigationView,
                            final FragmentManager fragmentManager, final RelativeLayout tabHeader,
                            final Button tab1, final Button tab2, final View nav) {

        click(tab1, "3", "1", null,fragmentManager, true, nav);
        click(tab2, "1", "3", null,fragmentManager, false, nav);

        spaceNavigationView.showIconOnly();
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem("HOME", R.drawable.home));
        spaceNavigationView.addSpaceItem(new SpaceItem("WALLET", R.drawable.wallet));

        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                Fragment active = getVisibleFragment(fragmentManager);
                Log.d("CLICK",  "  activetag: " + active.getTag());
                tabHeader.setVisibility(View.INVISIBLE);
                Fragment centreFragment = fragmentManager.findFragmentByTag("2");
                fragmentManager.beginTransaction().hide(active).show(centreFragment).commit();
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                onAnyItemClick(fragmentManager, itemName, tabHeader, tab1, tab2, nav);

            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                onAnyItemClick(fragmentManager, itemName, tabHeader, tab1, tab2, nav);

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

    private void onAnyItemClick(FragmentManager fragmentManager, String itemName, RelativeLayout tabHeader,
                                Button tab1, Button tab2, View nav) {
        Fragment active = getVisibleFragment(fragmentManager);
        tabHeader.setVisibility(View.VISIBLE);
        Fragment newFragment = null;
        if (itemName.equals("HOME")) {
            Log.d("CLICK", "SWITCH TO 1");
            newFragment = fragmentManager.findFragmentByTag("1");
            click(tab1, "3", "1", null,fragmentManager, true, nav);
            click(tab2, "1", "3", null,fragmentManager, false, nav);

        }
        else {
            Log.d("CLICK", "SWITCH TO 4");
            newFragment = fragmentManager.findFragmentByTag("4");
            click(tab1, "5", "4", null,fragmentManager, true, nav);
            click(tab2, "4", "5", null,fragmentManager, false, nav);
        }

        fragmentManager.beginTransaction().hide(active).show(newFragment).commit();

    }

    private void click(Button button, final String current, final String goTo, final View nav, final FragmentManager fm,
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

    private void moveLineNoAnimation(View nav) {
        nav.setTranslationX(0f);
    }

    private void moveLine(View nav, float start, float end) {

        ObjectAnimator textViewAnimator = ObjectAnimator.ofFloat(nav, "translationX",start, end);
        textViewAnimator.setDuration(300);

        AnimatorSet set = new AnimatorSet();
        set.play(textViewAnimator);
        set.start();
    }

}
