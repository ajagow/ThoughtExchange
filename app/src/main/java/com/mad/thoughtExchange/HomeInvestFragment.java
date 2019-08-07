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
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.mad.thoughtExchange.models.GsonRequest;
import com.mad.thoughtExchange.models.GsonRequestArray;
import com.mad.thoughtExchange.models.LikesModel;
import com.mad.thoughtExchange.responses.FeedPostResponse;
import com.mad.thoughtExchange.responses.LikeResponse;
import com.mad.thoughtExchange.responses.ThoughtResponse;
import com.mad.thoughtExchange.utils.HomeInvestItemAdapter;
import com.mad.thoughtExchange.utils.SharedPreferencesUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeInvestFragment extends Fragment {

    private static String GET_INVESTMENTS_URL = "api/v1/thoughts/investments/10/24";

    private ListView listView;
    private LinearLayout noNewInvestments;

    private  HomeInvestItemAdapter adapter;

    public HomeInvestFragment() {
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

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_invest, container, false);

        initViews(view);

        getInvestments();

        Log.d("INVEST", "log");

        return view;
    }

    public void getInvestments() {
        //vote meaning like/dislike

        Response.Listener<List<ThoughtResponse>> resonseListener = new Response.Listener<List<ThoughtResponse>>() {
            @Override
            public void onResponse(List<ThoughtResponse> response) {
                Log.d("getInvestments", "total response: "+response.toString());

                if (response.size() == 0) {
                    noNewInvestments.setVisibility(View.VISIBLE);
                    if (adapter != null) {
                        listView.setAdapter(null);
                    }
                }

                else {
                    setInvestments(response);
                    noNewInvestments.setVisibility(View.INVISIBLE);
                }
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

        GsonRequestArray<String, ThoughtResponse> gsonRequest = new GsonRequestArray<String, ThoughtResponse>(MainActivity.URL + GET_INVESTMENTS_URL, getContext(),
                ThoughtResponse.class, resonseListener, errorListener, headers);

        gsonRequest.volley();
    }

    private void setInvestments(List<ThoughtResponse> responses) {
        adapter = new HomeInvestItemAdapter(responses, getContext(), getActivity().getSupportFragmentManager());

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            getInvestments();
            
        }
    }

    private void initViews(View view) {
        listView = view.findViewById(R.id.myListView);
        noNewInvestments = view.findViewById(R.id.investments_feed_none);
    }
}
