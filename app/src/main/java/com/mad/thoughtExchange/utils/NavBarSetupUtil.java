package com.mad.thoughtExchange.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.mad.thoughtExchange.R;

import java.util.List;

public class NavBarSetupUtil {

    public void setupNavBar(Bundle savedInstanceState, SpaceNavigationView spaceNavigationView, final FragmentManager fragmentManager) {

        spaceNavigationView.showIconOnly();
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem("HOME", R.drawable.home));
        spaceNavigationView.addSpaceItem(new SpaceItem("WALLET", R.drawable.wallet));

        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                Fragment active = getVisibleFragment(fragmentManager);
                Log.d("CLICK",  "  activetag: " + active.getTag());
                Fragment centreFragment = fragmentManager.findFragmentByTag("2");
                fragmentManager.beginTransaction().hide(active).show(centreFragment).commit();
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                onAnyItemClick(fragmentManager, itemName);

            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                onAnyItemClick(fragmentManager, itemName);

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

    private void onAnyItemClick(FragmentManager fragmentManager, String itemName) {
        Fragment active = getVisibleFragment(fragmentManager);
        Fragment newFragment = null;
        if (itemName.equals("HOME")) {
            Log.d("CLICK", "SWITCH TO 1");
            newFragment = fragmentManager.findFragmentByTag("1");
        }
        else {
            Log.d("CLICK", "SWITCH TO 3");
            newFragment = fragmentManager.findFragmentByTag("3");
        }

        fragmentManager.beginTransaction().hide(active).show(newFragment).commit();

    }

}
