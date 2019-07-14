package com.mad.thoughtExchange;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class HomeActivity extends AppCompatActivity {

    private ImageView walletImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        walletImage = findViewById(R.id.footer_wallet);

        walletImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent explicitIntent = new Intent(HomeActivity.this, WalletActivity.class); // WalletActivity needs to be created
//                startActivity(explicitIntent);
            }
        });
    }

    private void getCurrentFeedPost() {

    }
}
