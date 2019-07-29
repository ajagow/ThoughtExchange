package com.mad.thoughtExchange.responses;

import com.google.gson.annotations.SerializedName;
import com.mad.thoughtExchange.models.ThoughtModel;


public class MyInvestmentsResponse extends ThoughtResponse {

    @SerializedName("my_initial_investment")
    private String myInitialInvestment;

    @SerializedName("earnings")
    private int earnings;


    public String getMyInitialInvestment() {
        return myInitialInvestment;
    }

    public void setMyInitialInvestment(String myInitialInvestment) {
        this.myInitialInvestment = myInitialInvestment;
    }

    public int getEarnings() {
        return earnings;
    }

    public void setEarnings(int earnings) {
        this.earnings = earnings;
    }
}
