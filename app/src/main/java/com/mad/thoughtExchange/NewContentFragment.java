package com.mad.thoughtExchange;

import android.os.Bundle;
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

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.mad.thoughtExchange.models.GsonRequest;
import com.mad.thoughtExchange.models.NewPostModel;
import com.mad.thoughtExchange.responses.ThoughtResponse;
import com.mad.thoughtExchange.utils.NavBarSetupUtil;
import com.mad.thoughtExchange.utils.SharedPreferencesUtil;
import com.mad.thoughtExchange.utils.VolleyUtils;

import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class NewContentFragment extends Fragment {

    private static String THOUGHTS_PATH = MainActivity.URL + "api/v1/thoughts/";

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

        initViews(view);

        newPostContent.setHorizontallyScrolling(false);
        newPostContent.setMaxLines(Integer.MAX_VALUE);

        // set submit button listener
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // submit post to api
                onSubmit(view);

            }
        });

        setTextWatcherForPostCounter();

        return view;
    }

    public void onSubmit(View view) {

        String thought = newPostContent.getText().toString();
        String initInvestmentVal = initialInvestment.getText().toString(); //// need to check if input is integer
        int initInvestment;

        // check if initial investment value is an positive integer
        try {
            initInvestment = Integer.parseInt(initInvestmentVal);
            if (thought.equals("") || thought.trim().length() <= 0) {
                Toast.makeText(getActivity(), "Thoughts can't be empty", Toast.LENGTH_SHORT).show();
            }
            else if (initInvestment <= 0) {
                Toast.makeText(getActivity(), "Enter an amount greater than 0", Toast.LENGTH_SHORT).show();
            } else if (!userHasEnoughMoneyToInvest(initInvestment)) {
                Toast.makeText(getActivity(), "You don't have enough coins. Enter a lower amount", Toast.LENGTH_SHORT).show();
            } else {
                onSuccessfulSubmit(initInvestment);
                sendNewPost(thought, initInvestment);
            }
        } catch (NumberFormatException e) {
            // print toast
            Log.d("NumberFormatException", String.valueOf(e));
            Toast.makeText(getActivity(), "Invalid number, try again", Toast.LENGTH_SHORT).show();
        }
    }

    // send post to api
    private void sendNewPost(String thought, int initInvestment) {
        NewPostModel thoughtModel = new NewPostModel();

        // set values for POST request
        thoughtModel.setContents(thought);
        thoughtModel.setInitial_worth(initInvestment);

        Response.Listener<ThoughtResponse> responseListener = new Response.Listener<ThoughtResponse>() {
            @Override
            public void onResponse(ThoughtResponse response) {
                // nothing happens when successful response
            }
        };

        Response.ErrorListener errorListener = VolleyUtils.logError("NEW_POST");

        Map<String, String> headers = VolleyUtils.getAuthenticationHeader(getActivity());

        GsonRequest<NewPostModel, ThoughtResponse> gsonRequest = new GsonRequest<>(Request.Method.POST, THOUGHTS_PATH, thoughtModel, getActivity(),
                NewPostModel.class, ThoughtResponse.class, headers, responseListener, errorListener);

        gsonRequest.volley();
    }


    // update view to show how many characters a user has left while they're typing
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

    // on successful submit, reset how many coins a user has and go back to navigation_icon_home page
    private void onSuccessfulSubmit(int initInvestment) {
        clearValues();

        int networth = SharedPreferencesUtil.getIntFromSharedPreferences(getActivity().getSharedPreferences(SharedPreferencesUtil.myPreferences, MODE_PRIVATE), SharedPreferencesUtil.networth);
        int newWorth = networth - initInvestment;
        SharedPreferencesUtil.saveToSharedPreferences(getActivity().getSharedPreferences(SharedPreferencesUtil.myPreferences, MODE_PRIVATE), SharedPreferencesUtil.networth, newWorth);
        TextView uWorth = getActivity().findViewById(R.id.worth);
        uWorth.setText(String.valueOf(newWorth));

        //
        NavBarSetupUtil.onAnyMenuItemClick(getFragmentManager(),
                "HOME",
                getActivity().findViewById(R.id.tab_header_and_line),
                getActivity().findViewById(R.id.tab_feed),
                getActivity().findViewById(R.id.tab_invest),
                getActivity().findViewById(R.id.space));
        SpaceNavigationView spaceNavigationView= getActivity().findViewById(R.id.space);
        spaceNavigationView.changeCurrentItem(0);


    }

    // clear values inside new content fragment after a post is submitted
    private void clearValues() {
        newPostContent.setText("");
        initialInvestment.setText("");
    }

    // check that user has enough coins to make their desired investment
    private boolean userHasEnoughMoneyToInvest(int investmentVal) {
        int networth = SharedPreferencesUtil.getIntFromSharedPreferences(getActivity().getSharedPreferences(SharedPreferencesUtil.myPreferences, MODE_PRIVATE), SharedPreferencesUtil.networth);
        return investmentVal <= networth;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        clearValues();
    }

    // initialize views
    private void initViews(View view) {
        newPostContent = view.findViewById(R.id.newpost_content);
        initialInvestment = view.findViewById(R.id.newpost_initial_investment);
        textCounter = view.findViewById(R.id.text_counter);
        submitBtn = view.findViewById(R.id.newpost_submit);
    }

}
