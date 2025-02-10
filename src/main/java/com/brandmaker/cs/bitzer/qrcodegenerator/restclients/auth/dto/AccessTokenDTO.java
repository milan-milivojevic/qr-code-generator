package com.brandmaker.cs.bitzer.qrcodegenerator.restclients.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *  DTO Object for storing access token
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessTokenDTO {
    public String access_token;
    public String getAccessToken() {
        return access_token;
    }
    public void setAccessToken(String firstName) {
        this.access_token = firstName;
    }
}
