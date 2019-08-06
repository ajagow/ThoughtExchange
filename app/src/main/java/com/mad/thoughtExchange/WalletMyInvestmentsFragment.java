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


public class WalletMyInvestmentsFragment extends Fragment {

    private ListView listView;
    private LinearLayout noInvestments;
    private static String GET_MY_POSTS = "api/v1/investments/me";


    public WalletMyInvestmentsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet_my_investments, container, false);

        listView = view.findViewById(R.id.my_investments_list_view);
        noInvestments = view.findViewById(R.id.my_investments_no_investments);
        noInvestments.setVisibility(View.INVISIBLE);



        return view;
    }

    private void getInvestments() {
        //vote meaning like/dislike

        Response.Listener<List<MyInvestmentsResponse>> resonseListener = new Response.Listener<List<MyInvestmentsResponse>>() {
            @Override
            public void onResponse(List<MyInvestmentsResponse> response) {
                Log.d("getInvestments", "total response: "+response.toString());
                noInvestments.setVisibility(View.INVISIBLE);


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

                if (error.networkResponse.statusCode == 404) {
                    noInvestments.setVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(getActivity(), "Error:  " + error.networkResponse.toString() + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };

        String token = SharedPreferencesUtil.getStringFromSharedPreferences(getActivity().getSharedPreferences(SharedPreferencesUtil.myPreferences, Context.MODE_PRIVATE), SharedPreferencesUtil.token);
        Map<String, String> headers = new HashMap<>();
        headers.put("api-token", token);

        GsonRequestArray<String, MyInvestmentsResponse> gsonRequest = new GsonRequestArray<String, MyInvestmentsResponse>(MainActivity.URL + GET_MY_POSTS, getContext(),
                MyInvestmentsResponse.class, resonseListener, errorListener, headers);

        gsonRequest.volley();
    }

    private void setInvestments(List<MyInvestmentsResponse> responses) {
        WalletItemAdapter adapter = new WalletItemAdapter(responses, getContext(), getActivity().getSupportFragmentManager());

        listView.setAdapter(adapter);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            getInvestments();
        }
    }





}
