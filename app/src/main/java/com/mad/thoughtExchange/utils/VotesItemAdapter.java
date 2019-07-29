package com.mad.thoughtExchange.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.mad.thoughtExchange.R;
import com.mad.thoughtExchange.responses.ThoughtResponse;
import com.mad.thoughtExchange.responses.VoteResponse;

import java.util.List;

public class VotesItemAdapter extends BaseAdapter {

    private List<VoteResponse> voteResponses;
    private Context context;
    private FragmentManager fragmentManager;
    private ViewHolder viewHolder;

    private GeneralUtils utils = new GeneralUtils();


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

        final String closingIn = "Posted on " + utils.getPostedDate(item.getCreatedAt());
        final boolean voteValue = item.isLike();

        if (view == null) {
            Log.d("votes, getView", "view == null");
            view = View.inflate(context, R.layout.my_votes_item, null);
            Log.d("VIEWLOG", "new view created");

            viewHolder = new ViewHolder();

            viewHolder.id = item.getId();
            viewHolder.content = view.findViewById(R.id.votes_content);
            viewHolder.postedAt = view.findViewById(R.id.votes_date_posted);
            viewHolder.voteValue = view.findViewById(R.id.votes_value);

            // link view holder to my view
            view.setTag(viewHolder);
        } else {
            Log.d("votes, getView", "view != null");
            // If view already exists then restore view holder and I can access Image and Text View
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.content.setText(item.getContents());
        viewHolder.postedAt.setText(closingIn);
        setVoteValueImage(viewHolder, voteValue);

        return view;
    }

    private void setVoteValueImage(ViewHolder viewHolder, boolean is_like) {
        if(is_like){
            viewHolder.voteValue.setImageResource(R.drawable.like_button_clicked);
        } else {
            viewHolder.voteValue.setImageResource(R.drawable.dislike_button_clicked);
        }
    }

    static class ViewHolder {
        int id;
        TextView content;
        TextView postedAt;
        ImageView voteValue;
    }
}
