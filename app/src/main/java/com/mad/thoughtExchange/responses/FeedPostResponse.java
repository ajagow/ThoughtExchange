package com.mad.thoughtExchange.responses;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class FeedPostResponse {
    @SerializedName("contents")
    private String contents;

    @SerializedName("created_at")
    private Date createdAt;

    @SerializedName("id")
    private int post_id;

    @SerializedName("investment_active")
    private boolean investmentActive;

    @SerializedName("market_active")
    private boolean marketActive;

    @SerializedName("modified_at")
    private Date modifiedAt;

    @SerializedName("owner_id")
    private int owner_id;


    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public boolean isInvestmentActive() {
        return investmentActive;
    }

    public void setInvestmentActive(boolean investmentActive) {
        this.investmentActive = investmentActive;
    }

    public boolean isMarketActive() {
        return marketActive;
    }

    public void setMarketActive(boolean marketActive) {
        this.marketActive = marketActive;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }
}
