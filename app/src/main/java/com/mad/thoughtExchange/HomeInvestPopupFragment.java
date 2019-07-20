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

import java.util.Date;


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
        TextView content = view.findViewById(R.id.popup_content);;
        TextView endsAt = view.findViewById(R.id.popup_end_time);;
        TextView investmentWorth = view.findViewById(R.id.popu_investment_worth);
        final EditText investmentAmount = view.findViewById(R.id.popup_investment_amount);
        Button investBtn = view.findViewById(R.id.popup_invest_btn);



        numberOfInvestors.setText(numInvestorsVal);
        content.setText(contentVal);
        endsAt.setText(expireDateVal);
        investmentWorth.setText(worthVal);

        investBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String invesmentAmountStr = investmentAmount.getEditableText().toString();

                if (!invesmentAmountStr.isEmpty()) {
                    int investmentAmountVal = Integer.parseInt(investmentAmount.getEditableText().toString());
                    Log.d("INVEST", investmentAmountVal + "   id: " + idVal);

                }

                HomeInvestPopupFragment.this.dismiss();
                getActivity().getSupportFragmentManager().beginTransaction().hide(HomeInvestPopupFragment.this);

            }
        });


        return view;
    }






}
