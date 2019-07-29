package com.mad.thoughtExchange.responses;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class VoteResponse {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getInitialWorth() {
        return initialWorth;
    }

    public void setInitialWorth(int initialWorth) {
        this.initialWorth = initialWorth;
    }

    public boolean getInvestmentActive() {
        return investmentActive;
    }

    public void setInvestmentActive(boolean investmentActive) {
        this.investmentActive = investmentActive;
    }

    public boolean getMarketActive() {
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @SerializedName("id")
    private int id;

    @SerializedName("contents")
    private String contents;

    @SerializedName("is_like")
    private boolean isLike;

    @SerializedName("owner_id")
    private int ownerId;

    @SerializedName("initial_worth")
    private int initialWorth;

    @SerializedName("investment_active")
    private boolean investmentActive;

    @SerializedName("market_active")
    private boolean marketActive;

    @SerializedName("modified_at")
    private Date modifiedAt;

    @SerializedName("created_at")
    private Date createdAt;
}