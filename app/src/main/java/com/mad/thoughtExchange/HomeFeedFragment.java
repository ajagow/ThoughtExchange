package com.mad.thoughtExchange;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.mad.thoughtExchange.models.GsonRequest;
import com.mad.thoughtExchange.models.GsonRequestArray;
import com.mad.thoughtExchange.models.LikesModel;
import com.mad.thoughtExchange.responses.FeedPostResponse;
import com.mad.thoughtExchange.responses.LikeResponse;
import com.mad.thoughtExchange.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFeedFragment extends Fragment {
    private static final String POSTS_PATH = "api/v1/thoughts/marketFeedPost/10/24/2000";
    private static final String LIKES_PATH = "api/v1/likes/";

    private List<FeedPostResponse> feedPostResponses = new ArrayList<FeedPostResponse>();

    private TextView currentFeedPost;
    private Button likeButton;
    private Button dislikeButton;
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

        currentFeedPost = view.findViewById(R.id.current_feed_post);
        currentPostId = -1;
        likeButton = view.findViewById(R.id.like_button);
        dislikeButton = view.findViewById(R.id.dislike_button);

//        changeBackgroundOnClick(likeButton, R.drawable.like_button_clicked, R.drawable.like_button);
//        changeBackgroundOnClick(dislikeButton, R.drawable.dislike_button_clicked, R.drawable.dislike_button);
        getCurrentFeedPost();

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("LIKE", "hello like");
                onLike(view, currentPostId);
            }
        });

        dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("DISLIKE", "hello dislike");
                onDislike(view, currentPostId);
            }
        });
        return view;
    }

    private void onLike(View view, int postId) {
        Log.d("LIKE", "you liked this");
        sendVote(view, postId, 1);
    }

    private void onDislike(View view, int postId) {
        Log.d("LIKE", "you disliked this");

        sendVote(view, postId, 0);
    }

    private void sendVote(View view, int postId, int vote) {
        //vote meaning like/dislike

        LikesModel likesModel = new LikesModel();

        // set values for POST request
        likesModel.setPostId(postId);
        likesModel.setIsLike(vote);

        Log.d("LIKES", "post id: " + postId +     "is like: " + vote);

        Response.Listener<LikeResponse> responseListener = new Response.Listener<LikeResponse>() {
            @Override
            public void onResponse(LikeResponse response) {
                Log.d("response","likesResponse");
                onSuccessfulVote(response);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", error.networkResponse.toString());
                Toast.makeText(getActivity(), "Error:  " + error.networkResponse.toString() + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        String token = SharedPreferencesUtil.getStringFromSharedPreferences(getActivity().getSharedPreferences(SharedPreferencesUtil.myPreferences, Context.MODE_PRIVATE), SharedPreferencesUtil.token);
        Map<String, String> headers = new HashMap<>();
        headers.put("api-token", token);

        GsonRequest<LikesModel, LikeResponse> gsonRequest = new GsonRequest<>(Request.Method.POST, MainActivity.URL + LIKES_PATH, likesModel, getActivity(),
                LikesModel.class, LikeResponse.class, headers, responseListener, errorListener);

        gsonRequest.volley();
    }

    private void onSuccessfulVote(LikeResponse response) {
        Log.d("onSuccessfulVote", response.toString());
        getMarketPostData();
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
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onError(error);
            }
        };

        Response.Listener<List<FeedPostResponse>> resonseListener = new Response.Listener<List<FeedPostResponse>>() {
            @Override
            public void onResponse(List<FeedPostResponse> response) {
                Log.d("getCurrenFeedPost",response.toString()); ///
                feedPostResponses = response;
                getMarketPostData();
            }
        };

        Map<String, String> headers = new HashMap<>();
        String token = SharedPreferencesUtil.getStringFromSharedPreferences(getActivity().getSharedPreferences(SharedPreferencesUtil.myPreferences, Context.MODE_PRIVATE), SharedPreferencesUtil.token);
        headers.put("api-token", token);

        GsonRequestArray<String, FeedPostResponse> gsonRequest = new GsonRequestArray<String, FeedPostResponse>(MainActivity.URL + POSTS_PATH, getContext(),
                FeedPostResponse.class, resonseListener, errorListener, headers);

        gsonRequest.volley();
    }

    private void onError(VolleyError error) {
        Toast.makeText(getActivity().getApplicationContext(), "Error:  " + error.networkResponse.toString() + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    // update market feed post with information
    private void getMarketPostData() {
        if (feedPostResponses.size() == 0) {
            currentFeedPost.setText("no new posts");
            setVoteButtonVisible(false);
        }
        else { // there are posts to display on feed
            FeedPostResponse post = feedPostResponses.remove(0);
            String postContent = post.getContents();

            setVoteButtonVisible(true);
            currentFeedPost.setText(postContent);
            currentPostId = post.getPost_id();
        }
    }

    private void setVoteButtonVisible(boolean visible) {
        if (visible) {
            Log.d("setVoteButtonVisible", "set to VISIBLE");
            likeButton.setVisibility(View.VISIBLE);
            dislikeButton.setVisibility(View.VISIBLE);
        }
        else {
            Log.d("setVoteButtonVisible", "set to INVISIBLE");
            likeButton.setVisibility(View.INVISIBLE);
            dislikeButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            getCurrentFeedPost();
        }
    }
}
