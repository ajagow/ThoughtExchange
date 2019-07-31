package com.mad.thoughtExchange;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Context;

import android.util.Log;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.mad.thoughtExchange.models.GsonRequestArray;
import com.mad.thoughtExchange.responses.VoteResponse;
import com.mad.thoughtExchange.utils.SharedPreferencesUtil;
import com.mad.thoughtExchange.utils.VotesItemAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class VotesFragment extends Fragment {

    private ListView listView;
    private LinearLayout noHistory;
    private static String GET_MY_VOTING_HISTORY = "/api/v1/users/me/votes";



    public VotesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("votes","in onCreateView");
        View view = inflater.inflate(R.layout.fragment_votes, container, false);
        Log.d("votes","inflated fragment_votes");
        listView = view.findViewById(R.id.my_votes_list_view);
        noHistory = view.findViewById(R.id.votes_no_history);

        return view;
    }

    private void getVotingHistory() {

        Response.Listener<List<VoteResponse>> resonseListener = new Response.Listener<List<VoteResponse>>() {
            @Override
            public void onResponse(List<VoteResponse> responses) {
                Log.d("getVotingHistory", "total response: "+responses.toString());

                for (VoteResponse response1 : responses) {
                    Log.d("getVotingHistory", response1.isLike() + " LIKE");
                }
                setVotingHistory(responses);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", error.networkResponse.toString());
                if (error.networkResponse.statusCode == 404) {

                    noHistory.setVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(getActivity(), "Please try again.", Toast.LENGTH_SHORT).show();

                }
            }
        };

        String token = SharedPreferencesUtil.getStringFromSharedPreferences(getActivity().getSharedPreferences(SharedPreferencesUtil.myPreferences, Context.MODE_PRIVATE), SharedPreferencesUtil.token);
        Map<String, String> headers = new HashMap<>();
        headers.put("api-token", token);
        Log.d("sharedPreferences","retrieved token: "+token);

        noHistory.setVisibility(View.INVISIBLE);


        GsonRequestArray<String, VoteResponse> gsonRequest = new GsonRequestArray<String, VoteResponse>(MainActivity.URL + GET_MY_VOTING_HISTORY, getContext(),
                VoteResponse.class, resonseListener, errorListener, headers);

        gsonRequest.volley();
    }

    private void setVotingHistory(List<VoteResponse> responses) {
        VotesItemAdapter adapter = new VotesItemAdapter(responses, getContext(), getActivity().getSupportFragmentManager());

        listView.setAdapter(adapter);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            getVotingHistory();
        }
    }





}

