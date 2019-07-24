package com.mad.thoughtExchange;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.mad.thoughtExchange.models.GsonRequest;
import com.mad.thoughtExchange.responses.UserResponse;
import com.mad.thoughtExchange.utils.SharedPreferencesUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeInvestPopupFragment extends DialogFragment {

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

    private int uWorth;

    private static String USERS_PATH = "api/v1/users/me";


    public HomeInvestPopupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeInvestPopupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeInvestPopupFragment newInstance(String content, Date expireDate, int numInvestors, int worth, int id) {
        HomeInvestPopupFragment fragment = new HomeInvestPopupFragment();
        Bundle args = new Bundle();

        String numInvestorsVal = numInvestors + "";
        String worthVal = worth + "";

        args.putString(CONTENT, content);
        args.putString(EXPIRE_DATE, expireDate.toString());
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

        TextView numberOfInvestors = view.findViewById(R.id.popup_num_of_investors);
        TextView content = view.findViewById(R.id.popup_content);
        TextView endsAt = view.findViewById(R.id.popup_end_time);
        TextView investmentWorth = view.findViewById(R.id.popu_investment_worth);
        final EditText investmentAmount = view.findViewById(R.id.popup_investment_amount);
        Button investBtn = view.findViewById(R.id.popup_invest_btn);



        numberOfInvestors.setText(numInvestorsVal);
        content.setText(contentVal);
        endsAt.setText(expireDateVal);
        investmentWorth.setText(worthVal);

        /*
        Submit investment
         */
        investBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

                Response.Listener<UserResponse> responseListener = new Response.Listener<UserResponse>() {
                    @Override
                    public void onResponse(UserResponse response) {
                        uWorth = response.getNetWorth();
                        Log.d("WORTHVAL", String.valueOf(worthVal));
                    }
                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "user settings error");
                    }
                };

                String token = SharedPreferencesUtil.getStringFromSharedPreferences(getActivity().getSharedPreferences(SharedPreferencesUtil.myPreferences, Context.MODE_PRIVATE), SharedPreferencesUtil.token);

                Map<String, String> headers = new HashMap<>();
                headers.put("api-token", token);
                headers.put("Content-Type", "application/json");

                GsonRequest<String, UserResponse> gsonRequest = new GsonRequest<String, UserResponse>(
                        MainActivity.URL + USERS_PATH,
                        getContext(),
                        UserResponse.class,
                        responseListener,
                        errorListener,
                        headers
                );

                gsonRequest.volley();

                // case: user inputs 0
                if (investmentAmountVal <= 0) {
                    Toast.makeText(getActivity(), "Value must be greater than 0", Toast.LENGTH_SHORT).show();
                }
                // case: user has enough money
                else if (uWorth > investmentAmountVal) {
                    //TODO: subtract money from user via post to database
                    HomeInvestPopupFragment.this.dismiss();
                    getActivity().getSupportFragmentManager().beginTransaction().hide(HomeInvestPopupFragment.this);
                }
                // case: user inputs a stack overflow, not a number, or user doesn't have enough money
                else {
                    Toast.makeText(getActivity(), "Not enough money or not a number", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }

}
