package com.mad.thoughtExchange.responses;

import com.google.gson.annotations.SerializedName;
import com.mad.thoughtExchange.models.ThoughtModel;

/**
 * Response to fetch user's investments API request
 */
public class MyInvestmentsResponse extends ThoughtResponse {

    @SerializedName("my_initial_investment")
    private String myInitialInvestment;

    @SerializedName("earnings")
    private int earnings;

    @SerializedName("num_likes")
    private String numberLikes;

    @SerializedName("num_dislikes")
    private String numDislikes;

    @SerializedName("market_active")
    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

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

    public String getNumberLikes() {
        return numberLikes;
    }

    public void setNumberLikes(String numberLikes) {
        this.numberLikes = numberLikes;
    }

    public String getNumDislikes() {
        return numDislikes;
    }

    public void setNumDislikes(String numDislikes) {
        this.numDislikes = numDislikes;
    }
}
