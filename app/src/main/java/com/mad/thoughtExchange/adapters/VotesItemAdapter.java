package com.mad.thoughtExchange.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.mad.thoughtExchange.R;
import com.mad.thoughtExchange.responses.VoteResponse;

import com.mad.thoughtExchange.utils.GeneralUtils;
import java.util.List;

/**
 * Adapter for displaying vote navigation_icon_history of a user.
 */
public class VotesItemAdapter extends BaseAdapter {

    private List<VoteResponse> voteResponses;
    private Context context;
    private FragmentManager fragmentManager;
    private ViewHolder viewHolder;


    public VotesItemAdapter(List<VoteResponse> voteResponses,
                            Context context, FragmentManager fragmentManager) {
        this.voteResponses = voteResponses;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public int getCount() {
        return voteResponses.size();
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

        final VoteResponse item = voteResponses.get(i);

        final boolean voteValue = item.isLike();

        if (view == null) {
            view = View.inflate(context, R.layout.my_votes_item, null);

            viewHolder = bindNewViewHolder(view);
            viewHolder.id = item.getId();

            // link view holder to my view
            view.setTag(viewHolder);
        } else {
            // If view already exists then restore view holder and I can access Image and Text View
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.content.setText(item.getContents());
        viewHolder.postedAt.setText(GeneralUtils.getPostedDate(item.getCreatedAt()));

        // set whether vote is a like or dislike
        setVoteValueImage(viewHolder, voteValue);

        return view;
    }

    // set whether a vote was a like or dislike
    private void setVoteValueImage(ViewHolder viewHolder, boolean is_like) {
        if(is_like){
            viewHolder.voteValueText.setBackground(ContextCompat.getDrawable(context, R.drawable.tag_background_green));
            viewHolder.voteValueText.setText("LIKED");
        } else {
            viewHolder.voteValueText.setBackground(ContextCompat.getDrawable(context, R.drawable.tag_background_red));
            viewHolder.voteValueText.setText("DISLIKED");
        }
    }

    // bind vew holder to views
    private ViewHolder bindNewViewHolder(View view) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.content = view.findViewById(R.id.votes_content);
        viewHolder.postedAt = view.findViewById(R.id.votes_date_posted);
        viewHolder.voteValue = view.findViewById(R.id.voteAdapterContainer);
        viewHolder.voteValueText = view.findViewById(R.id.votes_text_value);

        return viewHolder;
    }

    // view holder class
    static class ViewHolder {
        int id;
        TextView content;
        TextView postedAt;
        RelativeLayout voteValue;
        TextView voteValueText;
    }
}
