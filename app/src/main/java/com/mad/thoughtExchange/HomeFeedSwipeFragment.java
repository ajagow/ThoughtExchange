package com.mad.thoughtExchange;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.mad.thoughtExchange.models.GsonRequest;
import com.mad.thoughtExchange.models.GsonRequestArray;
import com.mad.thoughtExchange.models.LikesModel;
import com.mad.thoughtExchange.responses.FeedPostResponse;
import com.mad.thoughtExchange.responses.LikeResponse;
import com.mad.thoughtExchange.utils.HomeFeedSwipeAdapter;
import com.mad.thoughtExchange.utils.SharedPreferencesUtil;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFeedSwipeFragment extends Fragment implements CardStackListener {

    private static final String POSTS_PATH = "api/v1/thoughts";
    private static final String LIKES_PATH = "api/v1/likes/";
    //private static final String POSTS_PATH = "api/v1/thoughts/marketFeedPost/10/24/2000";

    private RecyclerView.Adapter adapter;
    private CardStackView cardStackView;

    private List<FeedPostResponse> feedPostResponses = new ArrayList<FeedPostResponse>();


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
        Log.d("carStackView", "found cardStackView");

        CardStackLayoutManager manager = new CardStackLayoutManager(getActivity(), this);
        manager.setCanScrollHorizontal(true);
        manager.setCanScrollVertical(false);
        cardStackView.setLayoutManager(manager);
        Log.d("cardStackView", "setLayoutManager");

        getCurrentFeedPost();

        return view;
    }

    private void getCurrentFeedPost() {
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onError(error);
            }
        };

        Response.Listener<List<FeedPostResponse>> responseListener = new Response.Listener<List<FeedPostResponse>>() {
            @Override
            public void onResponse(List<FeedPostResponse> response) {
                Log.d("feed response",response.toString()); ///
                feedPostResponses = response;
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

    private void onError(VolleyError error) {
        Toast.makeText(getActivity().getApplicationContext(), "Error:  " + error.networkResponse.toString() + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    private void onSuccessfulResponse() {
        adapter = new HomeFeedSwipeAdapter(feedPostResponses, getContext());

        cardStackView.setAdapter(adapter);
    }
    @Override
    public void onCardDragging(Direction direction, float ratio) {
        CardStackListener.DEFAULT.onCardDragging(direction, ratio);
    }

    @Override
    public void onCardSwiped(Direction direction) {
        int currentPostID = -1; //TODO: pull actual post ID
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

    private void onLike(int postId) {
        Log.d("LIKE", "you liked this");
        sendVote(postId, 1);
    }

    private void onDislike(int postId) {
        Log.d("LIKE", "you disliked this");
        sendVote(postId, 0);
    }

    private void sendVote(int postId, int vote) {
        LikesModel likesModel = new LikesModel();

        // set values for POST request
        likesModel.setPostId(postId);
        likesModel.setIsLike(vote);
        Log.d("LIKES", "post id: " + postId +     "is like: " + vote);

        Response.Listener<LikeResponse> responseListener = new Response.Listener<LikeResponse>() {
            @Override
            public void onResponse(LikeResponse response) {
                Log.d("response","likesResponse");
                //onSuccessfulVote(response);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", error.networkResponse.toString());
            }
        };

        String token = SharedPreferencesUtil.getStringFromSharedPreferences(getActivity().getSharedPreferences(SharedPreferencesUtil.myPreferences, Context.MODE_PRIVATE), SharedPreferencesUtil.token);
        Map<String, String> headers = new HashMap<>();
        headers.put("api-token", token);

        GsonRequest<LikesModel, LikeResponse> gsonRequest = new GsonRequest<>(Request.Method.POST, MainActivity.URL + LIKES_PATH, likesModel, getActivity(),
                LikesModel.class, LikeResponse.class, headers, responseListener, errorListener);

        gsonRequest.volley();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            getCurrentFeedPost();
        }
    }
}
