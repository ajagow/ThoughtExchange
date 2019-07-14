package com.mad.thoughtExchange;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class HomeInvestActivity extends AppCompatActivity {

    private Button investTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_invest);

        investTab = findViewById(R.id.tab_feed);

        investTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent explicitIntent = new Intent(HomeInvestActivity.this, HomeActivity.class);
                startActivity(explicitIntent);
            }
        });
    }

    /**
     * Change background image of specified component on click
     * @param clickedComponent component being listened to
     * @param clickedBackground the new background when clicked on
     * @param releaseInitBackground the initial background to revert to after click
     */
    private void changeBackgroundOnClick(final View clickedComponent,
                                         final int clickedBackground,
                                         final int releaseInitBackground) {
        clickedComponent.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    clickedComponent.setBackgroundResource(clickedBackground);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    clickedComponent.setBackgroundResource(releaseInitBackground);
                    return true;
                }
                return false;
            }
        });
    }
}
