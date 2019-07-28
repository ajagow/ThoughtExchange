package com.mad.thoughtExchange;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Context;

import android.util.Log;

import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.mad.thoughtExchange.models.GsonRequestArray;
import com.mad.thoughtExchange.responses.ThoughtResponse;
import com.mad.thoughtExchange.utils.SharedPreferencesUtil;
import com.mad.thoughtExchange.utils.WalletMyIdeasItemAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WalletMyVotesFragment extends Fragment {

    private ListView listView;
    private static String GET_MY_VOTING_HISTORY = "/api/v1/users/me/votes";



    public WalletMyVotesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet_my_votes, container, false);

        listView = view.findViewById(R.id.my_votes_list_view);

        return view;
    }

    private void getVotingHistory() {

        Response.Listener<List<ThoughtResponse>> resonseListener = new Response.Listener<List<ThoughtResponse>>() {
            @Override
            public void onResponse(List<ThoughtResponse> response) {
                Log.d("getVotingHistory", "total response: "+response.toString());

                for (ThoughtResponse response1 : response) {
                    Log.d("getVotingHistory", response1.getId() + "");
                }
                setVotingHistory(response);
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

        GsonRequestArray<String, ThoughtResponse> gsonRequest = new GsonRequestArray<String, ThoughtResponse>(MainActivity.URL + GET_MY_VOTING_HISTORY, getContext(),
                ThoughtResponse.class, resonseListener, errorListener, headers);

        gsonRequest.volley();
    }

    private void setVotingHistory(List<ThoughtResponse> responses) {
        WalletMyIdeasItemAdapter adapter = new WalletMyIdeasItemAdapter(responses, getContext(), getActivity().getSupportFragmentManager());

        listView.setAdapter(adapter);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            getVotingHistory();
        }
    }





}
