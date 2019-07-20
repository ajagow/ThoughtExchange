package com.mad.thoughtExchange;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.Volley;
import com.mad.thoughtExchange.models.GsonRequest;
import com.mad.thoughtExchange.models.SignupModel;
import com.mad.thoughtExchange.models.ThoughtModel;
import com.mad.thoughtExchange.responses.SignupResponse;
import com.mad.thoughtExchange.responses.ThoughtResponse;
import com.mad.thoughtExchange.utils.SharedPreferencesUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class NewContentFragment extends Fragment {

    private static String USERS_PATH = "api/v1/thoughts/";

    private TextView textCounter;
    private EditText newPostContent;
    private EditText initialInvestment;
    private Button submitBtn;

    public NewContentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_content, container, false);

        newPostContent = view.findViewById(R.id.newpost_content);
        initialInvestment = view.findViewById(R.id.newpost_initial_investment);
        textCounter = view.findViewById(R.id.text_counter);
        submitBtn = view.findViewById(R.id.newpost_submit);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmit(view);

            }
        });

        setTextWatcherForPostCounter();

        return view;
    }

    public void onSubmit(View view) {

        String thought = newPostContent.getText().toString();
        String initInvestmentVal = initialInvestment.getText().toString(); //// need to check if input is integer
        int initInvestment = 1;

        // check if initial investment value is an positive integer
        try {
            initInvestment = Integer.parseInt(initInvestmentVal);
        }
        catch (NumberFormatException e) {
            // print toast
            Log.d("NumberFormatException", e.toString());
        }

        if (userHasEnoughMoneyToInvest(initInvestment)) {
            sendNewPost(thought, initInvestment);
        }

        else {
            Toast.makeText(getActivity(), "You don't have enough coins. Please lower initial invesment amount.", Toast.LENGTH_SHORT).show();

        }
    }

    private void sendNewPost(String thought, int initInvestment) {
        ThoughtModel thoughtModel = new ThoughtModel();

        // set values for POST request
        thoughtModel.setContents(thought);
        thoughtModel.setInitial_worth(initInvestment);

        Response.Listener<ThoughtResponse> responseListener = new Response.Listener<ThoughtResponse>() {
            @Override
            public void onResponse(ThoughtResponse response) {
                Log.d("response","thoughtResponse");
                onSuccessfulSubmit(response);
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

        GsonRequest<ThoughtModel, ThoughtResponse> gsonRequest = new GsonRequest<>(Request.Method.POST, MainActivity.URL + USERS_PATH, thoughtModel, getActivity(),
                ThoughtModel.class, ThoughtResponse.class, headers, responseListener, errorListener);

        gsonRequest.volley();
    }


    private void setTextWatcherForPostCounter() {
        TextWatcher mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Sets the textview to the current length
                String newCount = 100 - s.length() + " characters left";
                textCounter.setText(newCount);
            }

            public void afterTextChanged(Editable s) {
            }
        };
        newPostContent.addTextChangedListener(mTextEditorWatcher);
    }


    private void onSuccessfulSubmit(ThoughtResponse thoughtResponse) {
        clearValues();
        Fragment home = getFragmentManager().findFragmentByTag("homeFeedFragment");
        getActivity().findViewById(R.id.tab_header_and_line).setVisibility(View.VISIBLE);
        getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_down, R.anim.fade_out).hide(NewContentFragment.this).show(home).commit();
    }

    private void clearValues() {
        newPostContent.setText("");
        initialInvestment.setText("");
    }

    private boolean userHasEnoughMoneyToInvest(int investmentVal) {
        int networth = SharedPreferencesUtil.getIntFromSharedPreferences(getActivity().getSharedPreferences(SharedPreferencesUtil.myPreferences, Context.MODE_PRIVATE), SharedPreferencesUtil.networth);
        return investmentVal <= networth;
    }

}
