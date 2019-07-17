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


        final HomeFeedFragment homeFeedFragment = new HomeFeedFragment();


        return view;
    }


}
