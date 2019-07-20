package com.mad.thoughtExchange.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.mad.thoughtExchange.R;
import com.mad.thoughtExchange.responses.ThoughtResponse;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HomeInvestItemAdapter extends BaseAdapter {

    // Arraylist with the data points that are to populated on my items that I'm creating
    private List<ThoughtResponse> thoughtResponses;

    // Initialize the View holder
    private ViewHolder viewHolder;

    // Receive the context from Main Activity
    private Context context;

    //Use utils class
    private GeneralUtils utils = new GeneralUtils();


    public HomeInvestItemAdapter(List<ThoughtResponse> thoughtResponses,
                                 Context context) {
        this.thoughtResponses = thoughtResponses;
        this.context = context;
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

        if (view == null) {
            view = View.inflate(context, R.layout.fragment_home_invest_item, null);
            Log.d("VIEWLOG", "new view created");


            // create an object of view holder --> get hold of child view references
            viewHolder = new ViewHolder();

            viewHolder.numberOfInvestors = view.findViewById(R.id.num_of_investors);
            viewHolder.content = view.findViewById(R.id.investment_text);
            viewHolder.investmentWorth = view.findViewById(R.id.investment_worth);
            viewHolder.endsAt = view.findViewById(R.id.investment_end_time);
            viewHolder.investBtn = view.findViewById(R.id.make_investment_btn);

            viewHolder.investBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("CLICK", "clicked" + item.getId());
                }
            });

            // link view holder to my view
            view.setTag(viewHolder);
        } else {
            // If view already exists then restore view holder and I can access Image and Text View
            viewHolder = (ViewHolder) view.getTag();

        }


        String numInvestors = item.getNumInvestors() + "";
        String investmentWorth = item.getInitialWorth() + "";

        viewHolder.numberOfInvestors.setText(numInvestors);
        viewHolder.endsAt.setText("Closing in " + utils.getCountdown(item.getCreatedAt()));
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
        Button investBtn;

    }

}
