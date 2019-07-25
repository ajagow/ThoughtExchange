package com.mad.thoughtExchange.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;


public class NewPostModel {

    @SerializedName("initial_worth")
    private int initialWorth;

    @SerializedName("contents")
    private String contents;


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


}
