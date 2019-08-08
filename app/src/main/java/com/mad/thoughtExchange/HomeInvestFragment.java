package com.mad.thoughtExchange;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.android.volley.Response;
import com.mad.thoughtExchange.models.GsonRequestArray;
import com.mad.thoughtExchange.responses.ThoughtResponse;
import com.mad.thoughtExchange.adapters.HomeInvestItemAdapter;
import com.mad.thoughtExchange.utils.VolleyUtils;

import java.util.List;
import java.util.Map;


public class HomeInvestFragment extends Fragment {

    private static String GET_INVESTMENTS_PATH = MainActivity.URL + "api/v1/thoughts/investments/10/24";

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

        // get potential investments from api
        getInvestments();

        return view;
    }

    // fetch investments from api
    void getInvestments() {

        Response.Listener<List<ThoughtResponse>> responseListener = new Response.Listener<List<ThoughtResponse>>() {
            @Override
            public void onResponse(List<ThoughtResponse> response) {

                // if no potential investments, set adapter to null and show no new investment msg
                if (response.size() == 0) {
                    noNewInvestments.setVisibility(View.VISIBLE);
                    if (adapter != null) {
                        listView.setAdapter(null);
                    }
                }

                else {
                    // set investments
                    setInvestments(response);

                    // hide no new invesments message
                    noNewInvestments.setVisibility(View.INVISIBLE);
                }
            }
        };

        Response.ErrorListener errorListener = VolleyUtils.logError("INVESTMENTS");

        Map<String, String> headers = VolleyUtils.getAuthenticationHeader(getActivity());

        GsonRequestArray<String, ThoughtResponse> gsonRequest =
            new GsonRequestArray<String, ThoughtResponse>(GET_INVESTMENTS_PATH, getContext(),
                ThoughtResponse.class, responseListener, errorListener, headers);

        gsonRequest.volley();
    }

    // set list of investments to HomeInvestItemAdapter
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

    // initialize views
    private void initViews(View view) {
        listView = view.findViewById(R.id.myListView);
        noNewInvestments = view.findViewById(R.id.investments_feed_none);
    }
}
