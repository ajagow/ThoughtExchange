package com.mad.thoughtExchange.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.mad.thoughtExchange.HomeFeedSwipeFragment;
import com.mad.thoughtExchange.MainActivity;
import com.mad.thoughtExchange.R;
import com.mad.thoughtExchange.models.GsonRequest;
import com.mad.thoughtExchange.models.LikesModel;
import com.mad.thoughtExchange.responses.FeedPostResponse;
import com.mad.thoughtExchange.responses.LikeResponse;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.Direction;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFeedSwipeAdapter extends RecyclerView.Adapter<HomeFeedSwipeAdapter.HomeFeedViewHolder>{

    public HomeFeedSwipeAdapter(List<FeedPostResponse> feedPostResponses, Context context) {
        this.feedPostResponses = feedPostResponses;
        this.context = context;
    }

    private List<FeedPostResponse> feedPostResponses;
    private Context context;

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
