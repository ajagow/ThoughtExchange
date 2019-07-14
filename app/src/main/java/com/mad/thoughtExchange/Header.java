package com.mad.thoughtExchange;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Header extends RelativeLayout {
    public static final String TAG = Header.class.getSimpleName();

    private Button settings;
    protected TextView logo;
    private TextView coins;

    public Header(Context context) {
        super(context);
    }

    public Header(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Header(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void initHeader() {
        inflateHeader();
    }

    private void inflateHeader() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_header, this);
        settings = findViewById(R.id.settings);
        logo = findViewById(R.id.logo);
        coins = findViewById(R.id.coins);
    }
}