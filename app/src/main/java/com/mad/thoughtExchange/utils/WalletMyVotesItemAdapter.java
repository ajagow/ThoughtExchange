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

public class WalletMyVotesItemAdapter extends BaseAdapter {

    private List<VoteResponse> voteResponses;
    private Context context;
    private FragmentManager fragmentManager;
    private ViewHolder viewHolder;

    private GeneralUtils utils = new GeneralUtils();


    public WalletMyVotesItemAdapter(List<VoteResponse> voteResponses,
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

        final boolean voteValue = item.getIsLikeBoolean();

        if (view == null) {
            view = View.inflate(context, R.layout.fragment_wallet_my_votes, null);
            Log.d("VIEWLOG", "new view created");

            // create an object of view holder --> get hold of child view references
            viewHolder = new ViewHolder();

            viewHolder.content = view.findViewById(R.id.votes_content);
            viewHolder.id = item.getPostId();

            // link view holder to my view
            view.setTag(viewHolder);
        } else {
            // If view already exists then restore view holder and I can access Image and Text View
            viewHolder = (ViewHolder) view.getTag();

        }

        viewHolder.content.setText(item.getContents());
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
        ImageView voteValue;
        TextView content;
        TextView endsAt;
        int id;
    }
}
