package com.mad.thoughtExchange.responses;

import com.google.gson.annotations.SerializedName;

public class VoteResponse {


    public int getVoteId() {
        return voteId;
    }

    public void setVoteId(int voteId) {
        this.voteId = voteId;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public int getIsLike() {
        return isLike;
    }

    public boolean getIsLikeBoolean() {
        return isLike == 1;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @SerializedName("id")
    private int voteId;

    @SerializedName("contents")
    private String contents;

    @SerializedName("is_like")
    private int isLike;

    @SerializedName("post_id")
    private int postId;

    @SerializedName("user_id")
    private int userId;

}