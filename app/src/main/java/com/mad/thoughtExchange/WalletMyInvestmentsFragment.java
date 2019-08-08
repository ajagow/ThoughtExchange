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
import com.mad.thoughtExchange.responses.MyInvestmentsResponse;
import com.mad.thoughtExchange.utils.VolleyUtils;
import com.mad.thoughtExchange.utils.WalletItemAdapter;

import java.util.List;
import java.util.Map;


/**
 * Fragment that displays a user's previous investments.
 */
public class WalletMyInvestmentsFragment extends Fragment {

    private ListView listView;
    private LinearLayout noInvestments;
    private static String GET_MY_POSTS = MainActivity.URL + "api/v1/investments/me";


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
        initViews(view);

        noInvestments.setVisibility(View.INVISIBLE);

        return view;
    }

    // get investments from api
    private void getInvestments() {

        Response.Listener<List<MyInvestmentsResponse>> resonseListener = new Response.Listener<List<MyInvestmentsResponse>>() {
            @Override
            public void onResponse(List<MyInvestmentsResponse> response) {
                noInvestments.setVisibility(View.INVISIBLE);
                setInvestments(response);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // if 404 error, user doesn't have an investment navigation_icon_history
                if (error.networkResponse.statusCode == 404) {
                    noInvestments.setVisibility(View.VISIBLE);
                }
                else {
                    String errorMsg = "Error:  " + error.networkResponse.toString() + error.getLocalizedMessage();
                    Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                }
            }
        };

        Map<String, String> headers = VolleyUtils.getAuthenticationHeader(getActivity());

        GsonRequestArray<String, MyInvestmentsResponse> gsonRequest = new GsonRequestArray<String, MyInvestmentsResponse>(GET_MY_POSTS, getContext(),
                MyInvestmentsResponse.class, resonseListener, errorListener, headers);

        gsonRequest.volley();
    }

    // set Investment Responses from api to adapter
    private void setInvestments(List<MyInvestmentsResponse> responses) {
        WalletItemAdapter adapter = new WalletItemAdapter(responses, getContext(),
            getActivity().getSupportFragmentManager());
        listView.setAdapter(adapter);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            getInvestments();
        }
    }

    private void initViews(View view) {
        listView = view.findViewById(R.id.my_investments_list_view);
        noInvestments = view.findViewById(R.id.my_investments_no_investments);
    }
}
