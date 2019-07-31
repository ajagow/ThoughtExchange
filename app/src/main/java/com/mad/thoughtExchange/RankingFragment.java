package com.mad.thoughtExchange;

import android.content.Context;
import android.net.Uri;
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
import com.mad.thoughtExchange.responses.VoteResponse;
import com.mad.thoughtExchange.utils.RankingItemAdapter;
import com.mad.thoughtExchange.utils.SharedPreferencesUtil;
import com.mad.thoughtExchange.utils.VotesItemAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 */
public class RankingFragment extends Fragment {

    private static int RANKING_LIMIT = 3;
    private static String GET_RANKING = "/api/v1/users/rankings";
    private static String GET_ME = "/api/v1/users/me";

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

        return view;
    }

    private void getRankings() {
        Response.Listener<List<RankingResponse>> resonseListener = new Response.Listener<List<RankingResponse>>() {
            @Override
            public void onResponse(List<RankingResponse> responses) {
                //TODO: before moving to listview, have to set current user ranking info in fragment
                setRankings(responses);
                // pull current user response, set rank
                getMe(responses);
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
        Log.d("sharedPreferences","retrieved token: "+token);

        GsonRequestArray<String, RankingResponse> gsonRequest = new GsonRequestArray<String, RankingResponse>(MainActivity.URL + GET_RANKING, getContext(),
                RankingResponse.class, resonseListener, errorListener, headers);

        gsonRequest.volley();
    }

    private List<RankingResponse> pullTopRankings(List<RankingResponse> responses) {
        List<RankingResponse> topRankings = new ArrayList<RankingResponse>();

        for (RankingResponse response : responses) {
            // loop and pull out top rankings
            if (response.getRank() <= RANKING_LIMIT) {
                topRankings.add(response);
            }
        }
        return topRankings;
    }

    private void setRankings(List<RankingResponse> responses) {
        // set ranking response data to view

        // set leader board title
        String title = String.format(getResources().getString(R.string.leaderboardTitle), Integer.toString(RANKING_LIMIT));
        leaderboardtitle.setText(title);

        // set adapter to leader board list view
        List<RankingResponse> topRankings = pullTopRankings(responses);
        RankingItemAdapter adapter = new RankingItemAdapter(topRankings, getContext(), getActivity().getSupportFragmentManager());
        leaderboardListview.setAdapter(adapter);
    }
        //TODO: need to add the current user too

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            getRankings();
        }
    }

    private void setMe(UserResponse response, List<RankingResponse> rankings) {
        Log.d("setME", "in setme");
        username.setText(response.getName());
        netWorth.setText(Integer.toString(response.getNetWorth()));

        for (RankingResponse user : rankings) {
            if (user.getEmail().equals(response.getEmail())) {
                rank.setText(Integer.toString(user.getRank()));
            }
        }
        // TODO: set rank
    }

    private void getMe(List<RankingResponse> rankings) {

        Response.Listener<UserResponse> resonseListener = new Response.Listener<UserResponse>() {
            @Override
            public void onResponse(UserResponse response) {
                Log.d("getVotingHistory", "total response: "+response.toString());
                Log.d("getME", response.getEmail() + " EMAIL");

                setMe(response, rankings);
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
        Log.d("sharedPreferences","retrieved token: "+token);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        GsonRequest<UserResponse> gsonRequest = new GsonRequest<UserResponse>(MainActivity.URL + GET_ME, UserResponse.class, headers, resonseListener, errorListener);
        queue.add(gsonRequest);
    }
}
