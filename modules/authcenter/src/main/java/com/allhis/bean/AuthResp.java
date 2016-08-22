package com.allhis.bean;

/**
 * Created by ljy on 16/7/1.
 * ok
 */
public class AuthResp {
    private int authCode;
    private String authMessage;

    public AuthResp(){

    }

    public AuthResp(int authCode, String authMessage){
        this.authCode = authCode;
        this.authMessage = authMessage;
    }

    public int getAuthCode(){
        return this.authCode;
    }
    public String getAuthMessage(){
        return this.authMessage;
    }

}
