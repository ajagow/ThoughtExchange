package com.mad.thoughtExchange.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.List;

public class FragmentUtil {

    public static void hideAllFragmentsBesidesGiven(FragmentManager fragmentManager, String name) {
        for (Fragment fragment : fragmentManager.getFragments()) {
            String fragmentName = fragment.getTag();
            if (!fragmentName.equals(name)) {
                fragmentManager.beginTransaction().hide(fragment).commit();
            }

        }
    }

    // get the fragment that's currently visible
    static Fragment getVisibleFragment(FragmentManager fragmentManager){
        List<Fragment> fragments = fragmentManager.getFragments();

        if(fragments != null){
            for(Fragment fragment : fragments){
                if(fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }
}
