package com.example.venclima.models;

public class LoginResponse {

    private String token;
    private long expiresIn;
    private String refreshToken;
    private long refreshExpiresIn;

    public LoginResponse() {}

    public LoginResponse(String token, long expiresIn) {
        this.token = token;
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() { 
        return refreshToken; 
    }
    
    public void setRefreshToken(String refreshToken) { 
        this.refreshToken = refreshToken; 
    }

    public long getRefreshExpiresIn() { 
        return refreshExpiresIn; 
    }
    public void setRefreshExpiresIn(long refreshExpiresIn) { 
        this.refreshExpiresIn = refreshExpiresIn;
     }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

}
