package com.allhis.bean;

/**
 * Created by ljy on 16/8/16.
 * ok
 */
public class PeriodBean {
    private String periodName;
    private int firstYear;
    private int lastYear;
    private int periodLevel;

    public String getPeriodName(){
        return this.periodName;
    }
    public int getFirstYear(){
        return this.firstYear;
    }
    public int getLastYear(){
        return this.lastYear;
    }
    public int getPeriodLevel(){
        return this.periodLevel;
    }

    public void setPeriodName(String periodName){
        this.periodName = periodName;
    }
    public void setFirstYear(int firstYear){
        this.firstYear = firstYear;
    }
    public void setLastYear(int lastYear){
        this.lastYear = lastYear;
    }
    public void setPeriodLevel(int periodLevel){
        this.periodLevel = periodLevel;
    }
}
