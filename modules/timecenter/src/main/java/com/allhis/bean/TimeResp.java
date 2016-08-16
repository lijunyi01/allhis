package com.allhis.bean;

/**
 * Created by ljy on 16/7/1.
 * ok
 */
public class TimeResp {
    private int errorCode;
    private String errorMessage;
    private int year;

    public TimeResp(){
        this.errorCode = -1;
        this.errorMessage = "failed";
    }

    public int getErrorCode(){
        return this.errorCode;
    }
    public String getErrorMessage(){
        return this.errorMessage;
    }
    public int getYear(){
        return this.year;
    }

    public void setYear(int year){
        this.year = year;
    }

    public void setErrorCode(int errorCode){
        this.errorCode = errorCode;
    }

    public void setErrorMessage(String errorMessage){
        this.errorMessage = errorMessage;
    }


}
