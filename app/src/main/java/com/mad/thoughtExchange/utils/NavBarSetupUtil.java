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
                            final Button tab1, final Button tab2) {

        click(tab1, "3", "1", null,fragmentManager, true);
        click(tab2, "1", "3", null,fragmentManager, false);

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
                onAnyItemClick(fragmentManager, itemName, tabHeader, tab1, tab2);

            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                onAnyItemClick(fragmentManager, itemName, tabHeader, tab1, tab2);

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
                                Button tab1, Button tab2) {
        Fragment active = getVisibleFragment(fragmentManager);
        tabHeader.setVisibility(View.VISIBLE);
        Fragment newFragment = null;
        if (itemName.equals("HOME")) {
            Log.d("CLICK", "SWITCH TO 1");
            newFragment = fragmentManager.findFragmentByTag("1");
            click(tab1, "3", "1", null,fragmentManager, true);
            click(tab2, "1", "3", null,fragmentManager, false);

        }
        else {
            Log.d("CLICK", "SWITCH TO 4");
            newFragment = fragmentManager.findFragmentByTag("4");
            click(tab1, "5", "4", null,fragmentManager, true);
            click(tab2, "4", "5", null,fragmentManager, false);
        }

        fragmentManager.beginTransaction().hide(active).show(newFragment).commit();

    }

    private void click(Button button, final String current, final String goTo, final View nav, final FragmentManager fm,
                       boolean swipeLeft) {

        final int enter;
        final int exit;

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


            }
        });
    }

}
