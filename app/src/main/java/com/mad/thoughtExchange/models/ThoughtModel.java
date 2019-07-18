package com.mad.thoughtExchange.models;

import com.google.gson.annotations.SerializedName;

public class ThoughtModel {
    @SerializedName("owner_id")
    private int owner_id;

    @SerializedName("initial_worth")
    private int initialWorth;

    @SerializedName("content")
    private String content;

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public int getInitial_worth() {
        return initialWorth;
    }

    public void setInitial_worth(int initialWorth) {
        this.initialWorth = initialWorth;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
