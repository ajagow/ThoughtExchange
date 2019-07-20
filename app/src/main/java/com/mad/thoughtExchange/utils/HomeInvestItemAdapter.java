package com.mad.thoughtExchange.utils;

public class HomeInvestItemAdapter {

    String thought;
    String endTime;
    int numInvestors;
    int numWorth;

    public HomeInvestItemAdapter(String thought, String endTime, int numInvestors, int numWorth) {
        this.thought = thought;
        this.endTime = endTime;
        this.numInvestors = numInvestors;
        this.numWorth = numWorth;
    }



    public String getThought() {
        return thought;
    }

    public String getEndTime() {
        return endTime;
    }

    public int getNumInvestors() {
        return numInvestors;
    }

    public int getNumWorth() {
        return numWorth;
    }

    public void setThought(String thought) {
        this.thought = thought;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setNumInvestors(int numInvestors) {
        this.numInvestors = numInvestors;
    }

    public void setNumWorth(int numWorth) {
        this.numWorth = numWorth;
    }


}
