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
import com.mad.thoughtExchange.responses.MyInvestmentsResponse;
import com.mad.thoughtExchange.utils.VolleyUtils;
import com.mad.thoughtExchange.adapters.WalletItemAdapter;

import java.util.List;
import java.util.Map;


/**
 * Represents a Fragment that displays a user's previous posts.
 */
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

    // get a user's previous investments from the api
    private void getInvestments() {
        // before fetch data
        noHistory.setVisibility(View.INVISIBLE);

        Response.Listener<List<MyInvestmentsResponse>> responseListener = new Response.Listener<List<MyInvestmentsResponse>>() {
            @Override
            public void onResponse(List<MyInvestmentsResponse> responses) {
                setNoHistoryVisibility(responses);
                setInvestments(responses);
            }
        };

        Response.ErrorListener errorListener = VolleyUtils.logError("WALLET_MY_IDEAS");


        Map<String, String> headers = VolleyUtils.getAuthenticationHeader(getActivity());


        GsonRequestArray<String, MyInvestmentsResponse> gsonRequest = new GsonRequestArray<String, MyInvestmentsResponse>(GET_MY_POSTS,
            getContext(), MyInvestmentsResponse.class, responseListener, errorListener, headers);

        gsonRequest.volley();
    }

    private void setInvestments(List<MyInvestmentsResponse> responses) {
        WalletItemAdapter adapter = new WalletItemAdapter(responses, getContext(),
            getActivity().getSupportFragmentManager());

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            getInvestments();
        }
    }

    // if there are no responses, then hide no navigation_icon_history message
    private void setNoHistoryVisibility(List<MyInvestmentsResponse> responses) {
        if (responses.size() == 0) {
            noHistory.setVisibility(View.VISIBLE);
        }
        else {
            noHistory.setVisibility(View.INVISIBLE);
        }
    }

    // initialize views
    private void initViews(View view) {
        listView = view.findViewById(R.id.my_ideas_list_view);
        noHistory = view.findViewById(R.id.my_ideas_no_history);
    }
}
