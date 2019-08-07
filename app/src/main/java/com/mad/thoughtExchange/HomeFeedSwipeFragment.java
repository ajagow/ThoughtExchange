package com.mad.thoughtExchange;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.mad.thoughtExchange.models.GsonRequest;
import com.mad.thoughtExchange.models.GsonRequestArray;
import com.mad.thoughtExchange.models.LikesModel;
import com.mad.thoughtExchange.responses.FeedPostResponse;
import com.mad.thoughtExchange.responses.LikeResponse;
import com.mad.thoughtExchange.utils.HomeFeedSwipeAdapter;
import com.mad.thoughtExchange.utils.SharedPreferencesUtil;
import com.mad.thoughtExchange.utils.VolleyUtils;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;


/**
 * Fragment for Home Voting Page. Users can like or dislike posts in this fragment.
 */
public class HomeFeedSwipeFragment extends Fragment implements CardStackListener {

    private static final String LIKES_PATH = MainActivity.URL + "api/v1/likes/";
    private static final String POSTS_PATH = "api/v1/thoughts/marketFeedPost/10/24/48";

    private RecyclerView.Adapter adapter;
    private CardStackView cardStackView;

    private List<FeedPostResponse> feedPostResponses = new ArrayList<FeedPostResponse>();

    private CardStackLayoutManager manager;

    private RelativeLayout emptyFeed;

    private Button likeButton;
    private Button dislikeButton;

    // swipe animation for dislike (make card swipe left)
    SwipeAnimationSetting dislikeAnimationSetting = new SwipeAnimationSetting.Builder()
            .setDirection(Direction.Left)
            .setDuration(Duration.Normal.duration)
            .build();

    // swipe animation for like (make card swipe right)
    SwipeAnimationSetting likeAnimationSetting = new SwipeAnimationSetting.Builder()
            .setDirection(Direction.Right)
            .setDuration(Duration.Normal.duration)
            .build();

    public HomeFeedSwipeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        ButterKnife.bind(getActivity());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_feed_swipe, container, false);

        cardStackView = view.findViewById(R.id.home_feed_swipe);

        manager = new CardStackLayoutManager(getActivity(), this);
        manager.setCanScrollHorizontal(true);
        manager.setCanScrollVertical(false);
        cardStackView.setLayoutManager(manager);

        emptyFeed = view.findViewById(R.id.empty_feed);

        // get current feed posts (posts that users haven't seen yet)
        getCurrentFeedPost();

        // initialize view items
        initViews(view);


        // set on click listener for like button
        // if user clicks like, then card swipes to the right
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                manager.setSwipeAnimationSetting(likeAnimationSetting);

                cardStackView.swipe();

            }
        });

        // set on click listener for dislike button
        // if user clicks like, then card swipes to the left
        dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                manager.setSwipeAnimationSetting(dislikeAnimationSetting);
                cardStackView.swipe();

            }
        });

        return view;
    }

    // set vote buttons visible or invisible
    // should be invisible when user runs out posts to vote on
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

    // fetch all market active posts for user to vote on that they haven't seen yet
    private void getCurrentFeedPost() {

        Response.ErrorListener errorListener = VolleyUtils.logError("FETCH_POST");

        Response.Listener<List<FeedPostResponse>> responseListener = new Response.Listener<List<FeedPostResponse>>() {
            @Override
            public void onResponse(List<FeedPostResponse> response) {

                // set feedpost responses
                feedPostResponses = response;

                // if there are responses, show like/dislike button and hide no more posts message
                if (response.size() > 0) {
                    emptyFeed.setVisibility(View.INVISIBLE);
                    setVoteButtonVisible(true);
                }
                // if no responses, hide like/dislike button and display no more posts message
                if (response.size() == 0) {
                    emptyFeed.setVisibility(View.VISIBLE);
                    setVoteButtonVisible(false);
                }
                onSuccessfulResponse();
            }
        };

        Map<String, String> headers = new HashMap<>();
        String token = SharedPreferencesUtil.getStringFromSharedPreferences(getActivity().getSharedPreferences(SharedPreferencesUtil.myPreferences, Context.MODE_PRIVATE), SharedPreferencesUtil.token);
        headers.put("api-token", token);

        GsonRequestArray<String, FeedPostResponse> gsonRequest = new GsonRequestArray<String, FeedPostResponse>(MainActivity.URL + POSTS_PATH, getContext(),
                FeedPostResponse.class, responseListener, errorListener, headers);

        gsonRequest.volley();
    }


    // create new adapter with responses
    private void onSuccessfulResponse() {
        adapter = new HomeFeedSwipeAdapter(feedPostResponses, getContext());

        cardStackView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {
        CardStackListener.DEFAULT.onCardDragging(direction, ratio);
    }

    @Override
    public void onCardSwiped(Direction direction) {
        int currentPostID = -1; //TODO: pull actual post ID
        int pos = manager.getTopPosition();

        if (pos == feedPostResponses.size()) {
            emptyFeed.setVisibility(View.VISIBLE);
            setVoteButtonVisible(false);
        }

        currentPostID = feedPostResponses.get(pos-1).getPost_id();

        if (direction == Direction.Left) {
            onDislike(currentPostID);
        }
        else if (direction == Direction.Right) {
            onLike(currentPostID);
        }
    }

    @Override
    public void onCardRewound() {
        CardStackListener.DEFAULT.onCardRewound();
    }

    @Override
    public void onCardCanceled() {
        CardStackListener.DEFAULT.onCardCanceled();
    }

    @Override
    public void onCardAppeared(View view, int position) {
        CardStackListener.DEFAULT.onCardAppeared(view, position);
    }

    @Override
    public void onCardDisappeared(View view, int position) {
        CardStackListener.DEFAULT.onCardDisappeared(view, position);
    }

    // when post is liked, send vote to api
    private void onLike(int postId) {
        sendVote(postId, 1);
    }

    // when post is disliked, send vote to api
    private void onDislike(int postId) {
        sendVote(postId, 0);
    }

    // send vote to api. 1 = like, 0 = dislike
    private void sendVote(int postId, int vote) {
        LikesModel likesModel = new LikesModel();

        // set values for POST request
        likesModel.setPostId(postId);
        likesModel.setIsLike(vote);

        Response.Listener<LikeResponse> responseListener = new Response.Listener<LikeResponse>() {
            @Override
            public void onResponse(LikeResponse response) {
                Log.d("response","likesResponse");
            }
        };

        Response.ErrorListener errorListener = VolleyUtils.logError("VOTE");

        Map<String, String> headers = VolleyUtils.getAuthenticationHeader(getActivity());

        GsonRequest<LikesModel, LikeResponse> gsonRequest = new GsonRequest<>(Request.Method.POST, LIKES_PATH, likesModel, getActivity(),
                LikesModel.class, LikeResponse.class, headers, responseListener, errorListener);

        gsonRequest.volley();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            getCurrentFeedPost();

        }
    }

    private void initViews(View view) {
        likeButton = view.findViewById(R.id.like_button);
        dislikeButton = view.findViewById(R.id.dislike_button);
    }
}
