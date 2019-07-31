package com.mad.thoughtExchange.models;

import com.google.gson.annotations.SerializedName;

public class InvestmentModel {
    @SerializedName("post_id")
    private int postId;

    @SerializedName("initial_investment")
    private int initialInvestment;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getInitialInvestment() {
        return initialInvestment;
    }

    public void setInitialInvestment(int initialInvestment) {
        this.initialInvestment = initialInvestment;
    }
}
