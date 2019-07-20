package com.mad.thoughtExchange.responses;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ThoughtResponse {
    /*
    e.g. RETURNS
    {
    "contents": "thots or thoughts?",
    "created_at": "2019-07-18T02:42:53.936180+00:00",
    "id": 3,
    "initial_worth": 1112,
    "investment_active": true,
    "market_active": false,
    "modified_at": "2019-07-18T02:42:53.936199+00:00",
    "owner_id": 8
}
     */
    //TODO: is anything necessary to return?

    @SerializedName("initial_worth")
    private int initialWorth;

    @SerializedName("contents")
    private String contents;

    @SerializedName("created_at")
    private Date createdAt;

    @SerializedName("num_investors")
    private int numInvestors;

    @SerializedName("total_worth")
    private int totalWorth;

    public int getInitial_worth() {
        return initialWorth;
    }

    public void setInitial_worth(int initialWorth) {
        this.initialWorth = initialWorth;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String content) {
        this.contents = content;
    }

    public int getInitialWorth() {
        return initialWorth;
    }

    public void setInitialWorth(int initialWorth) {
        this.initialWorth = initialWorth;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getNumInvestors() {
        return numInvestors;
    }

    public void setNumInvestors(int numInvestors) {
        this.numInvestors = numInvestors;
    }

    public int getTotalWorth() {
        return totalWorth;
    }

    public void setTotalWorth(int totalWorth) {
        this.totalWorth = totalWorth;
    }
}
