package com.allhis.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by ljy on 16/8/18.
 * ok
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class RetMessage {

    private String errorCode;
    private String errorMessage;
    private String retContent;

    public RetMessage() {
        this.errorCode = "-1";
        this.retContent = "";
        this.errorMessage = "";
    }

    public RetMessage(String errorCode, String errorMessage, String retContent) {
        this.errorCode = errorCode;
        this.retContent = retContent;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode ;}

    public String getErrorMessage() {
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage;}

    public String getRetContent() {
        return retContent;
    }
    public void setRetContent(String retContent) { this.retContent = retContent;}

}
