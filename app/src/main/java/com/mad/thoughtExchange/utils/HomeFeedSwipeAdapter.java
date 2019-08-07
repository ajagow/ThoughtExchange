package com.mad.thoughtExchange.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mad.thoughtExchange.R;
import com.mad.thoughtExchange.responses.FeedPostResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter for creating the Tinder-like wiping in the voting fragment.
 */
public class HomeFeedSwipeAdapter extends RecyclerView.Adapter<HomeFeedSwipeAdapter.HomeFeedViewHolder>{

    private List<FeedPostResponse> feedPostResponses;
    private Context context;

    /**
     * Constructor for HomeFeedSwipeAdapter.
     * @param feedPostResponses List of feed post responses to adapt
     * @param context context
     */
    public HomeFeedSwipeAdapter(List<FeedPostResponse> feedPostResponses, Context context) {
        this.feedPostResponses = feedPostResponses;
        this.context = context;
    }

    @Override
    public HomeFeedSwipeAdapter.HomeFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_feed_item, parent, false);
        HomeFeedViewHolder viewHolder = new HomeFeedViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HomeFeedSwipeAdapter.HomeFeedViewHolder holder, int position) {
        holder.bindResponse(feedPostResponses.get(position));
    }

    @Override
    public int getItemCount() {
        return feedPostResponses.size();
    }

    public class HomeFeedViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.current_feed_post) TextView current_feed_post;

        private Context context;

        public HomeFeedViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        public void bindResponse(FeedPostResponse response) {
            current_feed_post.setText(response.getContents());
        }
    }
}
