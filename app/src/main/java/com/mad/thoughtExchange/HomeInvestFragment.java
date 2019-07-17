package com.mad.thoughtExchange;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;


public class HomeInvestFragment extends Fragment {
    private Button investTab;

    public HomeInvestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("INVEST", "log");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_invest, container, false);


        investTab = getActivity().findViewById(R.id.tab_feed);

        final HomeFeedFragment homeFeedFragment = new HomeFeedFragment();


        investTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment s = getFragmentManager().findFragmentByTag("1");
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right).hide(HomeInvestFragment.this).show(s).commit();

                View nav = getActivity().findViewById(R.id.navbar_line);

                ObjectAnimator textViewAnimator = ObjectAnimator.ofFloat(nav, "translationX",550f,0f);
                textViewAnimator.setDuration(750);
                textViewAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

                AnimatorSet set = new AnimatorSet();
                set.play(textViewAnimator);
                set.start();}
        });

        return view;
    }


}
