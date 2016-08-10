package com.allhis.timecenter;

/**
 * Created by ljy on 16/7/1.
 * ok
 */
public class TimeResp {
    private int authCode;
    private String authMessage;
    private int year;

    public TimeResp(){

    }

    public TimeResp(int authCode, String authMessage){
        this.authCode = authCode;
        this.authMessage = authMessage;
    }

    public int getAuthCode(){
        return this.authCode;
    }
    public String getAuthMessage(){
        return this.authMessage;
    }
    public int getYear(){
        return this.year;
    }

    public void setYear(int year){
        this.year = year;
    }


}
