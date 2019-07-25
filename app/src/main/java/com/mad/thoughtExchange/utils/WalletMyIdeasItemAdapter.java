package com.mad.thoughtExchange.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.mad.thoughtExchange.HomeInvestPopupFragment;
import com.mad.thoughtExchange.R;
import com.mad.thoughtExchange.responses.ThoughtResponse;

import java.util.List;

public class WalletMyIdeasItemAdapter extends BaseAdapter {

    // Arraylist with the data points that are to populated on my items that I'm creating
    private List<ThoughtResponse> thoughtResponses;

    // Initialize the View holder
    private ViewHolder viewHolder;

    // Receive the context from Main Activity
    private Context context;

    private FragmentManager fragmentManager;

    //Use utils class
    private GeneralUtils utils = new GeneralUtils();


    public WalletMyIdeasItemAdapter(List<ThoughtResponse> thoughtResponses,
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

        // we aren't attaching to root
        // create and return the view

        final ThoughtResponse item = thoughtResponses.get(i);

        String numInvestors = item.getNumInvestors() + "";
        String investmentWorth = item.getTotalWorth() + "";
        final String closingIn = "Posted on " + utils.getPostedDate(item.getCreatedAt());

        if (view == null) {
            view = View.inflate(context, R.layout.fragment_wallet_my_ideas_item, null);
            Log.d("VIEWLOG", "new view created");


            // create an object of view holder --> get hold of child view references
            viewHolder = new ViewHolder();

            viewHolder.numberOfInvestors = view.findViewById(R.id.my_ideas_investors);
            viewHolder.content = view.findViewById(R.id.my_ideas_content);
            viewHolder.investmentWorth = view.findViewById(R.id.my_ideas_value);
            viewHolder.endsAt = view.findViewById(R.id.my_ideas_date_posted);
            viewHolder.id = item.getId();


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



        return view;
    }

    // class to hold my child view
    static class ViewHolder {
        TextView numberOfInvestors;
        TextView content;
        TextView endsAt;
        TextView investmentWorth;
        int id;

    }

}
