package com.allhis.bean;

import java.util.List;

/**
 * Created by ljy on 16/7/1.
 * ok
 */
public class PeriodResp {
    private int errorCode;
    private String errorMessage;
    private List<PeriodBean> periodBeanList;

    public PeriodResp(){
        this.errorCode = -1;
        this.errorMessage = "failed";
    }

    public int getErrorCode(){
        return this.errorCode;
    }
    public String getErrorMessage(){
        return this.errorMessage;
    }

    public List<PeriodBean> getPeriodBeanList(){
        return this.periodBeanList;
    }

    public void setErrorCode(int errorCode){
        this.errorCode = errorCode;
    }

    public void setErrorMessage(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public void setPeriodBeanList(List<PeriodBean> periodBeanList){
        this.periodBeanList = periodBeanList;
    }


}
