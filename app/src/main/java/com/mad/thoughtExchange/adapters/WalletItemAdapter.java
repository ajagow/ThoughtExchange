package com.mad.thoughtExchange.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.mad.thoughtExchange.R;
import com.mad.thoughtExchange.responses.MyInvestmentsResponse;

import com.mad.thoughtExchange.utils.GeneralUtils;
import java.util.Date;
import java.util.List;

/**
 * Adapter for each item in the wallet (both past investments and past ideas)
 */
public class WalletItemAdapter extends BaseAdapter {

    // List with the data points that are to populated on my items that I'm creating
    private List<MyInvestmentsResponse> myInvestmentsResponses;

    // Initialize the View holder
    private ViewHolder viewHolder;

    // Receive the context from Main Activity
    private Context context;

    // Fragment manager from activity
    private FragmentManager fragmentManager;


    public WalletItemAdapter(List<MyInvestmentsResponse> myInvestmentsResponses,
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


        final MyInvestmentsResponse item = myInvestmentsResponses.get(i);

        // convert item values to strings so that they can be used in text views
        String numInvestors = item.getNumInvestors() + "";
        String investmentWorth = item.getTotalWorth() + "";
        String numLikes = item.getNumberLikes() + "";
        String numDislikes = item.getNumDislikes() + "";
        Date creationDate = item.getCreatedAt();

        final String closingIn = "Posted on " + GeneralUtils.getPostedDate(item.getCreatedAt());

        String myInitialInvestment = item.getMyInitialInvestment() + "";
        String earnings = item.getEarnings() + "";


        if (view == null) {
            view = View.inflate(context, R.layout.fragment_wallet_my_investments_item, null);

            // create an object of view holder --> get hold of child view references
            setNewViewHolder(view, item);

            TextView gainOrLostView = view.findViewById(R.id.my_investments_gain_lost);
            if (item.getEarnings() < 0) {
                gainOrLostView.setText("You lost: ");
            } else {
                gainOrLostView.setText("You gained: ");
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
        viewHolder.numLikes.setText(numLikes);
        viewHolder.numDislikes.setText(numDislikes);

        setViewHolderIndicator(creationDate, view);

        // if item is closed on market, then display closed tag and how much a user earned
        if (GeneralUtils.isFinishedOnMarket(item.getCreatedAt())) {
            viewHolder.indicator.setVisibility(View.VISIBLE);
            viewHolder.earnings.setVisibility(View.VISIBLE);
            viewHolder.earningsView.setVisibility(View.VISIBLE);
            viewHolder.gainOrLost.setVisibility(View.VISIBLE);
        }

        // if item is still market active, then display active tag and hide how much a user earned
        if (!GeneralUtils.isFinishedOnMarket(item.getCreatedAt())) {
            //viewHolder.indicator.setVisibility(View.INVISIBLE);
            viewHolder.earnings.setVisibility(View.INVISIBLE);
            viewHolder.earningsView.setVisibility(View.INVISIBLE);
            viewHolder.gainOrLost.setVisibility(View.INVISIBLE);
        }

        if (item.getEarnings() < 0) {
            viewHolder.gainOrLost.setText("You lost: ");
        } else {
            viewHolder.gainOrLost.setText("You gained: ");
        }

        return view;
    }

    /**
     * set indicator card for wallet item.
     * calculates market active, sets active/closed cards
     *
     * @param creationDate date of thought creation
     * @param view item view
     */
    private void setViewHolderIndicator(Date creationDate, View view) {
        boolean isClosed = GeneralUtils.isFinishedOnMarket(creationDate);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup parent = (ViewGroup) view.findViewById(R.id.my_investments_header_layout);

        TextView placeholder = (TextView) view.findViewById(R.id.my_investments_closed_indicator);
        TextView closedCardTemp = (TextView) view.findViewById(R.id.my_investments_closed_indicator_temp);

        int index = parent.indexOfChild(placeholder);
        parent.removeView(placeholder);
        parent.removeView(closedCardTemp);

        if (!isClosed) {
            TextView activeCard = (TextView) layoutInflater.inflate(R.layout.active_card_textview, parent, false);
            parent.addView(activeCard, index);

            viewHolder.earningsView.setVisibility(View.INVISIBLE);
            viewHolder.earnings.setVisibility(View.INVISIBLE);
            viewHolder.earningsView.setVisibility(View.INVISIBLE);
            viewHolder.gainOrLost.setVisibility(View.INVISIBLE);
        } else {
            TextView closedCard = (TextView) layoutInflater.inflate(R.layout.closed_card_textview, parent, false);
            parent.addView(closedCard, index);

            viewHolder.earnings.setVisibility(View.VISIBLE);
            viewHolder.earningsView.setVisibility(View.VISIBLE);
            viewHolder.gainOrLost.setVisibility(View.VISIBLE);
        }
    }

    private void setNewViewHolder(View view, MyInvestmentsResponse item) {
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
        viewHolder.numLikes = view.findViewById(R.id.my_investments_num_likes);
        viewHolder.numDislikes = view.findViewById(R.id.my_investments_num_dislikes);
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
        TextView numLikes;
        TextView numDislikes;
        int id;
    }
}