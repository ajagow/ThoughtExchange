package com.mad.thoughtExchange;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroBaseFragment;
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

        addSlide(createBasePage(
                "Welcome",
                "Thought Exchange is a stock market for your thoughts…",
                R.drawable.ic_onboarding_img_1)
        );

        addSlide(createBasePage(
                "Share your ideas",
                "Have a really good thought? Write it out and set an initial investment on it",
                R.drawable.ic_onboarding_img_1)
        );

        addSlide(createBasePage(
                "Invest",
                "Don\'t know what to post? Invest in other people\'s thoughts and get a"
                +" return if it gets more likes than dislikes",
                R.drawable.ic_onboarding_img_1)
        );

        addSlide(createBasePage(
                "Watch out!",
                "If you invest in a thought that gets more dislikes than likes, you\'ll "
                + "lose your investment",
                R.drawable.ic_onboarding_img_1)
        );

        setBarColor(Color.parseColor("#3F51B5"));
        //setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);

        setFadeAnimation();
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    // Private method to set background color, text color, fonts, and base sizing for all pages
    private Fragment createBasePage(String titleText, String descText, int image) {
        SliderPage page = new SliderPage();

        page.setTitle(titleText);
        page.setDescription(descText);
        page.setImageDrawable(image);
        page.setBgColor(Color.parseColor("#FFFFFF"));
        page.setDescColor(Color.parseColor("#2978A0"));
        page.setTitleColor(Color.parseColor("#2978A0"));
        page.setTitleTypefaceFontRes(R.font.roboto_light);
        page.setDescTypefaceFontRes(R.font.roboto_light);


        return AppIntroFragment.newInstance(page);
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

        boolean isFirstStart = preferences.getBoolean("need_onboarding", true); //
        Log.d("isFirstStart", Boolean.toString(isFirstStart)); //

        //  Apply changes
        e.apply();
        // go back to the MainActivity TODO: move from signup to Dashboard
        Intent toMain = new Intent(this, MainActivity.class);
        startActivity(toMain);
    }
}