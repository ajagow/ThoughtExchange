package com.mad.thoughtExchange;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.GsonRequest;
import com.android.volley.toolbox.Volley;
import com.mad.thoughtExchange.models.GsonRequestArray;
import com.mad.thoughtExchange.responses.RankingResponse;
import com.mad.thoughtExchange.responses.UserResponse;
import com.mad.thoughtExchange.utils.RankingItemAdapter;
import com.mad.thoughtExchange.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 */
public class RankingFragment extends Fragment {

    private static int RANKING_LIMIT = 3;
    private static String GET_RANKING = MainActivity.URL + "/api/v1/users/rankings";
    private static String GET_ME = MainActivity.URL + "/api/v1/users/me";

    String token;

    TextView leaderboardtitle; // "Global Top _"
    TextView username;
    TextView rank;
    TextView netWorth;
    ListView leaderboardListview;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);

        leaderboardtitle = view.findViewById(R.id.leaderboardTitle);
        username = view.findViewById(R.id.currentUserUsername);
        rank = view.findViewById(R.id.currentUserRank);
        netWorth = view.findViewById(R.id.currentUserNetWorth);
        leaderboardListview = view.findViewById(R.id.leaderboardListView);

        SharedPreferences sPreferences = getActivity().getSharedPreferences(SharedPreferencesUtil.myPreferences, Context.MODE_PRIVATE);
        token = SharedPreferencesUtil.getStringFromSharedPreferences(sPreferences, SharedPreferencesUtil.token);

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            getRankings();
        }
    }

    private void getRankings() {
        Response.Listener<List<RankingResponse>> resonseListener = new Response.Listener<List<RankingResponse>>() {
            @Override
            public void onResponse(List<RankingResponse> responses) {
                setRankings(responses);
                getMe(responses);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", error.networkResponse.toString());
                String message = "Error: " + error.networkResponse.toString() + error.getLocalizedMessage();
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        };

        // update headers
        Map<String, String> headers = new HashMap<>();
        headers.put("api-token", token);

        GsonRequestArray<String, RankingResponse> gsonRequest = new GsonRequestArray<String, RankingResponse>(GET_RANKING,
            getContext(), RankingResponse.class, resonseListener, errorListener, headers);

        gsonRequest.volley();
    }

    private List<RankingResponse> pullTopRankings(List<RankingResponse> responses) {
        List<RankingResponse> topRankings = new ArrayList<RankingResponse>();

        for (RankingResponse response : responses) {
            if (response.getRank() <= RANKING_LIMIT) {
                topRankings.add(response);
            }
        }
        return topRankings;
    }

    private void setRankings(List<RankingResponse> responses) {
        // set leader board title
        String placeholderTitle = getResources().getString(R.string.leaderboardTitle);
        String title = String.format(placeholderTitle, Integer.toString(RANKING_LIMIT));
        leaderboardtitle.setText(title);

        // set adapter to leader board list view
        List<RankingResponse> topRankings = pullTopRankings(responses);
        RankingItemAdapter adapter = new RankingItemAdapter(topRankings, getContext(),
            getActivity().getSupportFragmentManager());
        leaderboardListview.setAdapter(adapter);
    }

    private void setMe(UserResponse response, List<RankingResponse> rankings) {
        username.setText(response.getName());
        netWorth.setText(Integer.toString(response.getNetWorth()));

        for (RankingResponse user : rankings) {
            if (user.getEmail().equals(response.getEmail())) {
                rank.setText(Integer.toString(user.getRank()));
            }
        }
    }

    private void getMe(List<RankingResponse> rankings) {

        Response.Listener<UserResponse> resonseListener = new Response.Listener<UserResponse>() {
            @Override
            public void onResponse(UserResponse response) {
                setMe(response, rankings);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", error.networkResponse.toString());
                String message = "Error: " + error.networkResponse.toString() + error.getLocalizedMessage();
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        };

        Map<String, String> headers = new HashMap<>();
        headers.put("api-token", token);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        GsonRequest<UserResponse> gsonRequest = new GsonRequest<UserResponse>(GET_ME,
            UserResponse.class, headers, resonseListener, errorListener);
        queue.add(gsonRequest);
    }
}
