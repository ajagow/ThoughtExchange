package com.mad.thoughtExchange.models;

import com.google.gson.annotations.SerializedName;


/**
 * Model to represent like POST API request
 */
public class LikesModel {

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    @SerializedName("post_id")
    private int postId;

    @SerializedName("is_like")
    private int isLike;
}
