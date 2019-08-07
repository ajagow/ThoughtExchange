package com.mad.thoughtExchange;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.mad.thoughtExchange.models.GsonRequestArray;
import com.mad.thoughtExchange.responses.MyInvestmentsResponse;
import com.mad.thoughtExchange.responses.ThoughtResponse;
import com.mad.thoughtExchange.utils.SharedPreferencesUtil;
import com.mad.thoughtExchange.utils.WalletItemAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WalletMyIdeasFragment extends Fragment {

    private static String GET_MY_POSTS = MainActivity.URL + "api/v1/thoughts/me";

    private ListView listView;
    private LinearLayout noHistory;


    public WalletMyIdeasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet_my_ideas, container, false);

        initViews(view);

        return view;
    }

    private void getInvestments() {
        //vote meaning like/dislike

        // before fetch data
        noHistory.setVisibility(View.INVISIBLE);

        Response.Listener<List<MyInvestmentsResponse>> resonseListener = new Response.Listener<List<MyInvestmentsResponse>>() {
            @Override
            public void onResponse(List<MyInvestmentsResponse> response) {
                Log.d("getInvestments", "total response: "+response.toString());

                if (response.size() == 0) {
                    noHistory.setVisibility(View.VISIBLE);
                }

                for (ThoughtResponse response1 : response) {
                    Log.d("getInvestments", response1.getId() + "");
                }
                setInvestments(response);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error:  " + error.networkResponse.toString() + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        String token = SharedPreferencesUtil.getStringFromSharedPreferences(getActivity()
            .getSharedPreferences(SharedPreferencesUtil.myPreferences, Context.MODE_PRIVATE), SharedPreferencesUtil.token);
        Map<String, String> headers = new HashMap<>();
        headers.put("api-token", token);
        Log.d("sharedPreferences","retrieved token: "+token);

        GsonRequestArray<String, MyInvestmentsResponse> gsonRequest = new GsonRequestArray<String, MyInvestmentsResponse>(GET_MY_POSTS, getContext(),
                MyInvestmentsResponse.class, resonseListener, errorListener, headers);

        gsonRequest.volley();
    }

    private void setInvestments(List<MyInvestmentsResponse> responses) {

        WalletItemAdapter adapter = new WalletItemAdapter(responses, getContext(), getActivity().getSupportFragmentManager());

        listView.setAdapter(adapter);
        //TODO: be able to invest from investment items
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            getInvestments();
        }
    }

    private void initViews(View view) {
        listView = view.findViewById(R.id.my_ideas_list_view);
        noHistory = view.findViewById(R.id.my_ideas_no_history);
    }
}
