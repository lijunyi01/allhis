package com.allhis.bean;

/**
 * Created by ljy on 16/8/24.
 * ok
 */
public class ClientReqBean {
    private int umid;
    private String token;
    private String serial;
    private String functionName;
    private String generalParam;

    public ClientReqBean() {
    }

    public int getUmid() {
        return umid;
    }

    public void setUmid(int umid) {
        this.umid = umid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getGeneralParam() {
        return generalParam;
    }

    public void setGeneralParam(String generalParam) {
        this.generalParam = generalParam;
    }
}
