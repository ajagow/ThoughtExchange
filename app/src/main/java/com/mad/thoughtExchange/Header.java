package com.mad.thoughtExchange;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.mad.thoughtExchange.models.GsonRequest;
import com.mad.thoughtExchange.responses.UserResponse;
import com.mad.thoughtExchange.utils.SharedPreferencesUtil;

import java.util.HashMap;
import java.util.Map;
import com.mad.thoughtExchange.utils.SharedPreferencesUtil;

import static android.content.Context.MODE_PRIVATE;

public class Header extends RelativeLayout {
    private static String USERS_PATH = "api/v1/users/me";
    public static final String TAG = Header.class.getSimpleName();

    private ImageView settings;
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
//        settings = findViewById(R.id.logout);
        logo = findViewById(R.id.logo);
        coins = findViewById(R.id.worth);
    }
}
