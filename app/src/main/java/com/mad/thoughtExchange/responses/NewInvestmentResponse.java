package com.mad.thoughtExchange.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Response to new investment API request
 */

public class NewInvestmentResponse {
    @SerializedName("id")
    private int id;

    @SerializedName("initial_investment")
    private int initialInvestment;

    @SerializedName("investor_id")
    private int investorId;

    @SerializedName("post_id")
    private int postId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInitialInvestment() {
        return initialInvestment;
    }

    public void setInitialInvestment(int initialInvestment) {
        this.initialInvestment = initialInvestment;
    }

    public int getInvestorId() {
        return investorId;
    }

    public void setInvestorId(int investorId) {
        this.investorId = investorId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}
