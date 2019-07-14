package com.mad.thoughtExchange.responses;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("jwt_token")
    private String token;

    public String getToken() {
        return token;
    }
}
