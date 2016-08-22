package com.allhis.bean;

/**
 * Created by ljy on 16/8/22.
 * ok
 */
public class UserParamBean {
    private String siteip;
    private int siteport;
    private int dbindex;
    private int tableindex;

    public UserParamBean() {
        this.siteip = "localhost";
        this.siteport = 8080;
        this.dbindex = 0;
        this.tableindex = 0;
    }

    public String getSiteip() {
        return siteip;
    }

    public void setSiteip(String siteip) {
        this.siteip = siteip;
    }

    public int getSiteport() {
        return siteport;
    }

    public void setSiteport(int siteport) {
        this.siteport = siteport;
    }

    public int getDbindex() {
        return dbindex;
    }

    public void setDbindex(int dbindex) {
        this.dbindex = dbindex;
    }

    public int getTableindex() {
        return tableindex;
    }

    public void setTableindex(int tableindex) {
        this.tableindex = tableindex;
    }
}
