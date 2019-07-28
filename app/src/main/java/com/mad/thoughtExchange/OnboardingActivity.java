package com.mad.thoughtExchange;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

public class OnboardingActivity extends AppIntro {
    private final String TITLE = "title";
    private final String DESCRIPTION = "description";
    private final String IMAGE = "image";
    private final String COLOR = "background_color";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SliderPage page1 = new SliderPage();
        page1.setTitle("welcome!");
        page1.setDescription("thought exchange is an application...");
        page1.setImageDrawable(R.drawable.google_logo);
        page1.setBgColor(Color.parseColor("#4284F5"));
        page1.setTitleTypefaceFontRes(R.font.lato_bold);
        page1.setDescTypefaceFontRes(R.font.roboto_light);
        addSlide(AppIntroFragment.newInstance(page1));

        SliderPage page2 = new SliderPage();
        page2.setTitle("Application to post and invest in thoughts");
        page2.setDescription("Once posted, a thought goes into the investment stage for 1 hour, then moves to the market to stay for another 24 hours");
        page2.setImageDrawable(R.drawable.random1);
        page2.setBgColor(Color.parseColor("#E37542"));
        page1.setTitleTypefaceFontRes(R.font.lato_bold);
        page1.setDescTypefaceFontRes(R.font.roboto_light);
        addSlide(AppIntroFragment.newInstance(page2));

        SliderPage page3 = new SliderPage();
        page3.setTitle("Application to post and invest in thoughts");
        page3.setDescription("Once posted, a thought goes into the investment stage for 1 hour, then moves to the market to stay for another 24 hours");
        page3.setImageDrawable(R.drawable.random1);
        page3.setBgColor(Color.parseColor("#fc033d"));
        page1.setTitleTypefaceFontRes(R.font.lato_bold);
        page1.setDescTypefaceFontRes(R.font.roboto_light);
        addSlide(AppIntroFragment.newInstance(page3));

        setBarColor(Color.parseColor("#3F51B5"));
        //setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);

        setFadeAnimation();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        Log.d("onboarding","onSkipPressed");
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
        // Update the shared preferences
        onOnboardingCompletion();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        Log.d("onboarding","onDonePressed");
        super.onDonePressed(currentFragment);
        onOnboardingCompletion();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        Log.d("onboarding","onSlideChanged");
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }

    private void onOnboardingCompletion() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        SharedPreferences.Editor e = preferences.edit();
        e.putBoolean("need_onboarding", false);

        //  Apply changes
        e.apply();
        // go back to the MainActivity
        Intent toMain = new Intent(this, MainActivity.class);
        startActivity(toMain);
    }
}