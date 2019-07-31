package com.mad.thoughtExchange.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.mad.thoughtExchange.R;
import com.mad.thoughtExchange.responses.MyInvestmentsResponse;

import java.util.List;

public class WalletMyInvestmentsItemAdapter extends BaseAdapter {

    // Arraylist with the data points that are to populated on my items that I'm creating
    private List<MyInvestmentsResponse> myInvestmentsResponses;

    // Initialize the View holder
    private ViewHolder viewHolder;

    // Receive the context from Main Activity
    private Context context;

    private FragmentManager fragmentManager;

    //Use utils class
    private GeneralUtils utils = new GeneralUtils();


    public WalletMyInvestmentsItemAdapter(List<MyInvestmentsResponse> myInvestmentsResponses,
                                          Context context, FragmentManager fragmentManager) {
        this.myInvestmentsResponses = myInvestmentsResponses;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public int getCount() {
        return myInvestmentsResponses.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        // we aren't attaching to root
        // create and return the view

        final MyInvestmentsResponse item = myInvestmentsResponses.get(i);

        String numInvestors = item.getNumInvestors() + "";
        String investmentWorth = item.getTotalWorth() + "";

        //todo: fixe this
        final String closingIn = "Posted on " + utils.getPostedDate(item.getCreatedAt());

        String myInitialInvestment = item.getMyInitialInvestment() + "";
        String earnings = item.getEarnings() + "";





        if (view == null) {
            view = View.inflate(context, R.layout.fragment_wallet_my_investments_item, null);
            Log.d("VIEWLOG", "new view created");


            // create an object of view holder --> get hold of child view references
            viewHolder = new ViewHolder();

            viewHolder.numberOfInvestors = view.findViewById(R.id.my_investments_num_investors);
            viewHolder.content = view.findViewById(R.id.my_investments_content);
            viewHolder.investmentWorth = view.findViewById(R.id.my_investments_total_worth);
            viewHolder.endsAt = view.findViewById(R.id.my_investments_ended_at);
            viewHolder.id = item.getId();
            viewHolder.indicator = view.findViewById(R.id.my_investments_closed_indicator);
            viewHolder.earnings = view.findViewById(R.id.my_investments_earnings);
            viewHolder.initialInvestment = view.findViewById(R.id.my_investments_my_invested);

            viewHolder.earningsView = view.findViewById(R.id.my_investments_earning_box);
            viewHolder.gainOrLost = view.findViewById(R.id.my_investments_gain_lost);

            if (!utils.isFinishedOnMarket(item.getCreatedAt())) {
                TextView  indicator = view.findViewById(R.id.my_investments_closed_indicator);
                indicator.setVisibility(View.INVISIBLE);

                LinearLayout earningsView = view.findViewById(R.id.my_investments_earning_box);
                earningsView.setVisibility(View.INVISIBLE);


            }

            if (item.getEarnings() < 0) {
                TextView gainOrLostView = view.findViewById(R.id.my_investments_gain_lost);
                gainOrLostView.setText("You lost: ");

            }


            // link view holder to my view
            view.setTag(viewHolder);
        } else {
            // If view already exists then restore view holder and I can access Image and Text View
            viewHolder = (ViewHolder) view.getTag();

        }



        viewHolder.numberOfInvestors.setText(numInvestors);

        viewHolder.endsAt.setText(closingIn);
        viewHolder.investmentWorth.setText(investmentWorth);
        viewHolder.content.setText(item.getContents());
        viewHolder.earnings.setText(earnings);
        viewHolder.initialInvestment.setText(myInitialInvestment);

        if (utils.isFinishedOnMarket(item.getCreatedAt())) {
            viewHolder.indicator.setVisibility(View.VISIBLE);
            viewHolder.earnings.setVisibility(View.VISIBLE);
            viewHolder.earningsView.setVisibility(View.VISIBLE);
            viewHolder.gainOrLost.setVisibility(View.VISIBLE);

        }

        if (!utils.isFinishedOnMarket(item.getCreatedAt())) {
            viewHolder.indicator.setVisibility(View.INVISIBLE);
            viewHolder.earnings.setVisibility(View.INVISIBLE);
            viewHolder.earningsView.setVisibility(View.INVISIBLE);
            viewHolder.gainOrLost.setVisibility(View.INVISIBLE);

        }

        if (item.getEarnings() < 0) {
            viewHolder.gainOrLost.setText("You lost: ");

        }



        return view;
    }

    // class to hold my child view
    static class ViewHolder {
        TextView numberOfInvestors;
        TextView content;
        TextView endsAt;
        TextView investmentWorth;
        TextView indicator;
        TextView earnings;
        TextView initialInvestment;
        LinearLayout earningsView;
        TextView gainOrLost;
        int id;

    }

}