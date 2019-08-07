package com.mad.thoughtExchange;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.mad.thoughtExchange.models.GsonRequest;
import com.mad.thoughtExchange.models.InvestmentModel;
import com.mad.thoughtExchange.responses.NewInvestmentResponse;
import com.mad.thoughtExchange.utils.SharedPreferencesUtil;
import com.mad.thoughtExchange.utils.VolleyUtils;

import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


/**
 * Fragment for showing investments users can potentially invest in.
 */
public class HomeInvestPopupFragment extends DialogFragment {

    private static String INVESTMENT_PATH = MainActivity.URL + "api/v1/investments/";
    private static String CONTENT = "content";
    private static String EXPIRE_DATE = "expireDate";
    private static String NUM_INVESTORS = "numInvestors";
    private static String WORTH = "worth";
    private static String ID = "id";

    private String contentVal;
    private String expireDateVal;
    private String numInvestorsVal;
    private String worthVal;
    private int idVal;

    private TextView numberOfInvestors;
    private TextView content;
    private TextView endsAt;
    private TextView investmentWorth;
    private Button investBtn;
    private Button closeBtn;


    public HomeInvestPopupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeInvestPopupFragment.
     */
    public static HomeInvestPopupFragment newInstance(String content, String expireDate, int numInvestors, int worth, int id) {
        HomeInvestPopupFragment fragment = new HomeInvestPopupFragment();
        Bundle args = new Bundle();

        String numInvestorsVal = numInvestors + "";
        String worthVal = worth + "";

        args.putString(CONTENT, content);
        args.putString(EXPIRE_DATE, expireDate);
        args.putString(NUM_INVESTORS, numInvestorsVal);
        args.putString(WORTH, worthVal);
        args.putInt(ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            contentVal = getArguments().getString(CONTENT);
            expireDateVal = getArguments().getString(EXPIRE_DATE);
            numInvestorsVal = getArguments().getString(NUM_INVESTORS);
            worthVal = getArguments().getString(WORTH);
            idVal = getArguments().getInt(ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_home_invest_popup, container, false);

        initViews(view);
        final EditText investmentAmount = view.findViewById(R.id.popup_investment_amount);

        // enable scrolling for content
        content.setMovementMethod(new ScrollingMovementMethod());

        numberOfInvestors.setText(numInvestorsVal);
        content.setText(contentVal);
        endsAt.setText(expireDateVal);
        investmentWorth.setText(worthVal);

        /*
        Submit investment Button
         */
        investBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int uWorth = SharedPreferencesUtil.getIntFromSharedPreferences(
                        getActivity().getSharedPreferences(SharedPreferencesUtil.myPreferences, MODE_PRIVATE),
                        SharedPreferencesUtil.networth);

                String invesmentAmountStr = investmentAmount.getEditableText().toString();
                int investmentAmountVal = 0;


                if (!invesmentAmountStr.isEmpty()) {
                    try {
                        investmentAmountVal = Integer.parseInt(investmentAmount.getEditableText().toString());
                        Log.d("INVEST", investmentAmountVal + "   id: " + idVal);
                    } catch (NumberFormatException nfe) {
                        Log.d("ERROR", String.valueOf(nfe));
                    }
                }

                // case: user inputs 0
                if (investmentAmountVal <= 0) {
                    Toast.makeText(getActivity(), "Value must be greater than 0", Toast.LENGTH_SHORT).show();
                } else if (investmentAmountVal > uWorth) {
                    Toast.makeText(getActivity(), "You don\'t have enough coins", Toast.LENGTH_SHORT).show();
                } else {

                    HomeInvestPopupFragment.this.dismiss();
                    Fragment fragment = getFragmentManager().findFragmentByTag(DashboardActivity.HOME_INVEST_FRAGMENT_NAME);

                    // send investment request to api
                    sendInvestmentRequest(investmentAmountVal, idVal, fragment);

                    // update net worth in header
                    int networth = SharedPreferencesUtil.getIntFromSharedPreferences(getActivity().getSharedPreferences(SharedPreferencesUtil.myPreferences, MODE_PRIVATE), SharedPreferencesUtil.networth);
                    int newWorth = networth - investmentAmountVal;
                    SharedPreferencesUtil.saveToSharedPreferences(getActivity().getSharedPreferences(SharedPreferencesUtil.myPreferences, MODE_PRIVATE), SharedPreferencesUtil.networth, newWorth);
                    TextView uWorthTextView = getActivity().findViewById(R.id.worth);
                    uWorthTextView.setText(String.valueOf(newWorth));

                }
            }
        });

        // set close button to close popup
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeInvestPopupFragment.this.dismiss();
            }
        });

        return view;
    }

    // send investment request to api
    private void sendInvestmentRequest(int initInvestment, int postId, Fragment fragment) {
        InvestmentModel investmentModel = new InvestmentModel();

        // set values for POST request
        investmentModel.setInitialInvestment(initInvestment);
        investmentModel.setPostId(postId);

        Response.Listener<NewInvestmentResponse> responseListener = new Response.Listener<NewInvestmentResponse>() {
            @Override
            public void onResponse(NewInvestmentResponse response) {

                // refresh investments in HomeInvestFragment
                HomeInvestFragment homeInvestFragment = (HomeInvestFragment) fragment;
                homeInvestFragment.getInvestments();

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // 400 error means user doesn't have enough coins
                if (error.networkResponse.statusCode == 400) {
                    Toast.makeText(getActivity(), "You don't have enough coins. Please enter a lower amount.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Please try again.", Toast.LENGTH_SHORT).show();

                }
            }
        };

        Map<String, String> headers = VolleyUtils.getAuthenticationHeader(getActivity());


        GsonRequest<InvestmentModel, NewInvestmentResponse> gsonRequest = new GsonRequest<>(Request.Method.POST, INVESTMENT_PATH, investmentModel, getActivity(),
                InvestmentModel.class, NewInvestmentResponse.class, headers, responseListener, errorListener);

        gsonRequest.volley();
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    // init views
    private void initViews(View view) {
        numberOfInvestors = view.findViewById(R.id.popup_num_of_investors);
        content = view.findViewById(R.id.popup_content);
        endsAt = view.findViewById(R.id.popup_end_time);
        investmentWorth = view.findViewById(R.id.popu_investment_worth);
        investBtn = view.findViewById(R.id.popup_invest_btn);
        closeBtn = view.findViewById(R.id.popup_close_btn);
    }
}
