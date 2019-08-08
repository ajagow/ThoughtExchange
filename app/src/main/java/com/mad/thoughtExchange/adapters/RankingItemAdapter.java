package com.mad.thoughtExchange.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.mad.thoughtExchange.R;
import com.mad.thoughtExchange.responses.RankingResponse;

import java.util.List;

/**
 * Adapter for displaying user ranking item
 */
public class RankingItemAdapter extends BaseAdapter {

    private List<RankingResponse> rankingResponses;
    private Context context;
    private FragmentManager fragmentManager;
    private ViewHolder viewHolder;


    public RankingItemAdapter(List<RankingResponse> rankingResponses,
                            Context context, FragmentManager fragmentManager) {
        this.rankingResponses = rankingResponses;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public int getCount() {
        return rankingResponses.size();
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

        final RankingResponse item = rankingResponses.get(i);

        if (view == null) {
            view = View.inflate(context, R.layout.ranking_item, null);
            viewHolder = bindNewViewHolder(view);

            // link view holder to my view
            view.setTag(viewHolder);
        } else {
            Log.d("navigation_icon_ranking, getView", "view != null");
            // If view already exists then restore view holder and I can access Image and Text View
            viewHolder = (ViewHolder) view.getTag();
        }
        setItemToViewHolder(item);

        return view;
    }

    // set view holder
    private void setItemToViewHolder(RankingResponse item) {
        viewHolder.rank.setText(Integer.toString(item.getRank()));
        viewHolder.username.setText(item.getName());
        viewHolder.netWorth.setText(Integer.toString(item.getNetWorth()));
    }

    // bind view holder to views
    private ViewHolder bindNewViewHolder(View view) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.rank = view.findViewById(R.id.rankingRank);
        viewHolder.username = view.findViewById(R.id.rankingUsername);
        viewHolder.netWorth = view.findViewById(R.id.rankingNetWorth);

        return viewHolder;
    }

    // View holder class
    static class ViewHolder {
        TextView rank;
        TextView username;
        TextView netWorth;
    }
}
