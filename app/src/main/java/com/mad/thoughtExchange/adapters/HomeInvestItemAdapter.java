package com.mad.thoughtExchange.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.mad.thoughtExchange.HomeInvestPopupFragment;
import com.mad.thoughtExchange.R;
import com.mad.thoughtExchange.responses.ThoughtResponse;

import com.mad.thoughtExchange.utils.GeneralUtils;
import java.util.List;

/**
 * Adapter for the ListView under the investment tab in the navigation_icon_home fragment.
 * Each represents an item that a User can potentially invest in.
 */

public class HomeInvestItemAdapter extends BaseAdapter {

    // List of thought responses to adapt
    private List<ThoughtResponse> thoughtResponses;

    // Initialize the View holder
    private ViewHolder viewHolder;

    // Receive the context from Main Activity
    private Context context;

    private FragmentManager fragmentManager;

    /**
     * Constructor for HomeInvestItemAdapter.
     * @param thoughtResponses list of thought responses to adapt
     * @param context context
     * @param fragmentManager fragment manager from activity
     */
    public HomeInvestItemAdapter(List<ThoughtResponse> thoughtResponses,
                                 Context context, FragmentManager fragmentManager) {
        this.thoughtResponses = thoughtResponses;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public int getCount() {
        return thoughtResponses.size();
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

        // get current thought response item
        final ThoughtResponse item = thoughtResponses.get(i);

        // convert fields in thought response item into strings
        String numInvestors = item.getNumInvestors() + "";
        String investmentWorth = item.getTotalWorth() + "";
        final String closingIn = "Closing in " + GeneralUtils.getCountdown(item.getCreatedAt(), 24);

        if (view == null) {
            view = View.inflate(context, R.layout.fragment_home_invest_item, null);

            viewHolder = bindNewViewHolder(view);
            viewHolder.id = item.getId();

            // link view holder to my view
            view.setTag(viewHolder);
        } else {
            // If view already exists then restore view holder and I can access Image and Text View
            viewHolder = (ViewHolder) view.getTag();
        }

        // set information in view
        viewHolder.numberOfInvestors.setText(numInvestors);
        viewHolder.endsAt.setText(closingIn);
        viewHolder.investmentWorth.setText(investmentWorth);
        viewHolder.content.setText(item.getContents());

        // when user clicks invest button, open HomeInvestPopupFragment so user can make an
        // investment
        viewHolder.investBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HomeInvestPopupFragment homeInvestPopupFragment = new HomeInvestPopupFragment().newInstance(
                        item.getContents(),
                        closingIn,
                        item.getNumInvestors(),
                        item.getTotalWorth(),
                        item.getId());

                // open the invest popup fragment
                homeInvestPopupFragment.show(fragmentManager, "fragment_fragment_home_invest_popup");
            }
        });
        return view;
    }

    // helper to bind view Holder
    private ViewHolder bindNewViewHolder(View view) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.numberOfInvestors = view.findViewById(R.id.num_of_investors);
        viewHolder.content = view.findViewById(R.id.investment_text);
        viewHolder.investmentWorth = view.findViewById(R.id.investment_worth);
        viewHolder.endsAt = view.findViewById(R.id.investment_end_time);
        viewHolder.investBtn = view.findViewById(R.id.make_investment_btn);

        return viewHolder;
    }

    // class to hold my child view
    static class ViewHolder {
        TextView numberOfInvestors;
        TextView content;
        TextView endsAt;
        TextView investmentWorth;
        int id;
        Button investBtn;
    }
}
