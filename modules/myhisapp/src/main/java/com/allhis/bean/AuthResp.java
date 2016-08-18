package com.allhis.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by ljy on 16/8/18.
 * ok
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResp {

    private int authCode;
    private String authMessage;
    private String content;

    public AuthResp(){

    }

    public int getAuthCode(){
        return this.authCode;
    }
    public String getAuthMessage(){
        return this.authMessage;
    }
    public String getContent(){return this.content;}
}
