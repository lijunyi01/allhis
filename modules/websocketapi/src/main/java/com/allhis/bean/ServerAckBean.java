package com.allhis.bean;

/**
 * Created by ljy on 16/8/30.
 * ok
 */
public class ServerAckBean {

    private String errorCode;
    private String errorMessage;
    private String serial;
    private String generalAckContent;

    public ServerAckBean() {
        this.errorCode = "-1";
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getGeneralAckContent() {
        return generalAckContent;
    }

    public void setGeneralAckContent(String generalAckContent) {
        this.generalAckContent = generalAckContent;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
