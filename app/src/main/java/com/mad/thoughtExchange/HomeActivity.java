package com.mad.thoughtExchange;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.mad.thoughtExchange.models.GsonRequest;
import com.mad.thoughtExchange.models.GsonRequestArray;
import com.mad.thoughtExchange.responses.FeedPostResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private static final String POSTS_PATH = "api/v1/thoughts/marketFeedPost/1/24";
    private ImageView walletImage;
    private TextView currentFeedPost;
    private int currentPostId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        walletImage = findViewById(R.id.footer_wallet);
        currentFeedPost = findViewById(R.id.current_feed_post);
        currentPostId = -1;


        final Button likeButton = findViewById(R.id.like_button);
        final Button dislikeButton = findViewById(R.id.dislike_button);
        changeBackgroundOnClick(likeButton, R.drawable.like_button_clicked, R.drawable.like_button);
        changeBackgroundOnClick(dislikeButton, R.drawable.dislike_button_clicked, R.drawable.dislike_button);
        getCurrentFeedPost();

        walletImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent explicitIntent = new Intent(HomeActivity.this, HomeInvestActivity.class);
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

    private void getCurrentFeedPost() {

        Response.Listener<List<FeedPostResponse>> resonseListener = new Response.Listener<List<FeedPostResponse>>() {
            @Override
            public void onResponse(List<FeedPostResponse> response) {

                getMarketPostData(response);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                onError(error);
            }
        };

        String token = getIntent().getStringExtra("jwtToken");
        Map<String, String> headers = new HashMap<>();
        headers.put("api-token", token);
        headers.put("Content-Type", "application/json");

        GsonRequestArray<String, FeedPostResponse> gsonRequest = new GsonRequestArray<String, FeedPostResponse>(MainActivity.URL + POSTS_PATH, this,
                FeedPostResponse.class, resonseListener, errorListener, headers);

        gsonRequest.volley();

    }

    private void onError(VolleyError error) {
        Toast.makeText(getApplicationContext(), "Error:  " + error.networkResponse.toString() + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    // update market feed post with information
    private void getMarketPostData(List<FeedPostResponse> feedPostResponses) {
        if (feedPostResponses.size() == 0) {
            currentFeedPost.setText("no new posts");
        }

        else {
            FeedPostResponse feedPostResponse = feedPostResponses.get(0);
            String postContent = feedPostResponse.getContents();

            currentFeedPost.setText(postContent);
            currentPostId = feedPostResponse.getPost_id();
        }
    }
}
