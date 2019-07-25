package com.mad.thoughtExchange;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.mad.thoughtExchange.models.GsonRequestArray;
import com.mad.thoughtExchange.responses.ThoughtResponse;
import com.mad.thoughtExchange.utils.HomeInvestItemAdapter;
import com.mad.thoughtExchange.utils.SharedPreferencesUtil;
import com.mad.thoughtExchange.utils.WalletMyIdeasItemAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WalletMyIdeasFragment extends Fragment {

    private ListView listView;
    private static String GET_MY_POSTS = "api/v1/thoughts/me";



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

        listView = view.findViewById(R.id.my_ideas_list_view);




        return view;
    }

    private void getInvestments() {
        //vote meaning like/dislike

        Response.Listener<List<ThoughtResponse>> resonseListener = new Response.Listener<List<ThoughtResponse>>() {
            @Override
            public void onResponse(List<ThoughtResponse> response) {
                Log.d("getInvestments", "total response: "+response.toString());

                for (ThoughtResponse response1 : response) {
                    Log.d("getInvestments", response1.getId() + "");
                }
                setInvestments(response);
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

        GsonRequestArray<String, ThoughtResponse> gsonRequest = new GsonRequestArray<String, ThoughtResponse>(MainActivity.URL + GET_MY_POSTS, getContext(),
                ThoughtResponse.class, resonseListener, errorListener, headers);

        gsonRequest.volley();
    }

    private void setInvestments(List<ThoughtResponse> responses) {
        WalletMyIdeasItemAdapter adapter = new WalletMyIdeasItemAdapter(responses, getContext(), getActivity().getSupportFragmentManager());

        listView.setAdapter(adapter);
        //TODO: be able to invest from investment items
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            getInvestments();
        }
    }





}
