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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.mad.thoughtExchange.models.GsonRequestArray;
import com.mad.thoughtExchange.responses.FeedPostResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFeedFragment extends Fragment {

    private static final String POSTS_PATH = "api/v1/thoughts/marketFeedPost/1/24";
    private ImageView walletImage;
    private TextView currentFeedPost;
    private int currentPostId;

    public HomeFeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_feed, container, false);

        Log.d("FEED", "log");


        walletImage = view.findViewById(R.id.footer_wallet);
        currentFeedPost = view.findViewById(R.id.current_feed_post);
        currentPostId = -1;

        final Button likeButton = view.findViewById(R.id.like_button);
        final Button dislikeButton = view.findViewById(R.id.dislike_button);

        changeBackgroundOnClick(likeButton, R.drawable.like_button_clicked, R.drawable.like_button);
        changeBackgroundOnClick(dislikeButton, R.drawable.dislike_button_clicked, R.drawable.dislike_button);
        getCurrentFeedPost();


        return view;
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

        String token = getActivity().getIntent().getStringExtra("jwtToken");
        Map<String, String> headers = new HashMap<>();
        headers.put("api-token", token);
        headers.put("Content-Type", "application/json");

        GsonRequestArray<String, FeedPostResponse> gsonRequest = new GsonRequestArray<String, FeedPostResponse>(MainActivity.URL + POSTS_PATH, getContext(),
                FeedPostResponse.class, resonseListener, errorListener, headers);

        gsonRequest.volley();

    }

    private void onError(VolleyError error) {
        Toast.makeText(getActivity().getApplicationContext(), "Error:  " + error.networkResponse.toString() + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
