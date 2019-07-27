package com.mad.thoughtExchange;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.leanback.app.OnboardingSupportFragment;

import java.util.ArrayList;

public class OnboardingFragment extends OnboardingSupportFragment {

    public static final String COMPLETED_ONBOARDING = "completed_onboarding";
    //private static final long ANIMATION_DURATION = 500;

    ImageView contentView;
    Animator animator;

    private static final int[] titles = {
            R.string.onboarding_title_welcome,
            R.string.onboarding_title_app_intro,
            R.string.onboarding_title_stages,
            R.string.onboarding_title_descrip
    };

    private static final int[] descriptions = {
            R.string.onboarding_welcome,
            R.string.onboarding_app_intro,
            R.string.onboarding_stages,
            R.string.onboarding_descrip
    };

    private final int[] images = {
            R.drawable.random1,
            R.drawable.random2,
            R.drawable.google_logo,
            R.drawable.like_button
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("onboarding","onCreateView");
        setLogoResourceId(R.drawable.google_logo);
        Log.d("onboarding", "set logo resource id");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void onFinishFragment() {
        super.onFinishFragment();
        // Our onboarding is done
        // Update the shared preferences
        SharedPreferences.Editor sharedPreferencesEditor =
                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
        sharedPreferencesEditor.putBoolean(COMPLETED_ONBOARDING, true);
        sharedPreferencesEditor.apply();
        // go back to the MainActivity
        getActivity().finish();
    }

    @Nullable
    @Override
    protected View onCreateBackgroundView(LayoutInflater inflater, ViewGroup container) {
        Log.d("onboarding","onCreateBackgroundView");
        View backgroundView = new View(getActivity());
        backgroundView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.backgroundColor));
        return backgroundView;
    }

    @Nullable
    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container) {
        Log.d("onboarding","onCreateContextView");
        contentView = new ImageView(getActivity());
        contentView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(60,60);
        contentView.setLayoutParams(parms);
        //contentView.setPadding(0, 50, 0, 50);
        return contentView;
    }

    @Nullable
    @Override
    protected View onCreateForegroundView(LayoutInflater inflater, ViewGroup container) {
        Log.d("onboarding","onCreateForegroundView");
        View view = inflater.inflate(R.layout.fragment_onboarding, container, false);
        return view;
        //return null;
    }

    @Override
    protected int getPageCount() {
        return titles.length;
    }

    @Override
    protected CharSequence getPageTitle(int pageIndex) {
        return getString(titles[pageIndex]);
    }

    @Override
    protected CharSequence getPageDescription(int pageIndex) {
        return getString(descriptions[pageIndex]);
    }
}
