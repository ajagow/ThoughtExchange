package com.mad.thoughtExchange.responses;

import com.google.gson.annotations.SerializedName;

public class UserResponse {

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("email")
    private String email;

    @SerializedName("name")
    private String name;

    public int getNetWorth() {
        return netWorth;
    }

    public void setNetWorth(int netWorth) {
        this.netWorth = netWorth;
    }

    @SerializedName("net_worth")
    private int netWorth;
}
