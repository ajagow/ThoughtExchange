package com.mad.thoughtExchange;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.mad.thoughtExchange.models.GsonRequestArray;
import com.mad.thoughtExchange.responses.VoteResponse;
import com.mad.thoughtExchange.utils.VolleyUtils;
import com.mad.thoughtExchange.adapters.VotesItemAdapter;

import java.util.List;
import java.util.Map;


public class VotesFragment extends Fragment {

    private static String GET_MY_VOTING_HISTORY = MainActivity.URL + "/api/v1/users/me/votes";

    private VotesItemAdapter adapter;

    private ListView listView;
    private LinearLayout noHistory;


    public VotesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_votes, container, false);
        initViews(view);

        return view;
    }

    private void getVotingHistory() {

        Response.Listener<List<VoteResponse>> resonseListener = new Response.Listener<List<VoteResponse>>() {
            @Override
            public void onResponse(List<VoteResponse> responses) {
                setVotingHistory(responses);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // if 404, that means user doesn't have any votes fragments
                if (error.networkResponse.statusCode == 404) {
                    noHistory.setVisibility(View.VISIBLE);
                    listView.setAdapter(null);
                }
                else {
                    Toast.makeText(getActivity(), "Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        };

        Map<String, String> headers = VolleyUtils.getAuthenticationHeader(getActivity());

        noHistory.setVisibility(View.INVISIBLE);

        GsonRequestArray<String, VoteResponse> gsonRequest = new GsonRequestArray<String, VoteResponse>(GET_MY_VOTING_HISTORY, getContext(),
                VoteResponse.class, resonseListener, errorListener, headers);

        gsonRequest.volley();
    }

    // set vote responses to VoteItemAdapter
    private void setVotingHistory(List<VoteResponse> responses) {
        adapter = new VotesItemAdapter(responses, getContext(), getActivity().getSupportFragmentManager());

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            getVotingHistory();
        }
    }

    // initialize views
    private void initViews(View view) {
        listView = view.findViewById(R.id.my_votes_list_view);
        noHistory = view.findViewById(R.id.votes_no_history);
    }
}

