package com.mad.thoughtExchange.responses;

import com.google.gson.annotations.SerializedName;


/**
 * Response to user login API request
 */
public class LoginResponse {
    @SerializedName("jwt_token")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String givenToken) {
        this.token = givenToken;
    }
}
