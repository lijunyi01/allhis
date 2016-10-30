package com.allhis.email;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by ljy on 15/7/9.
 * ok
 */
public class MailBean implements Serializable {

    private String from;
    private String fromName;
    private String[] toEmails;

    private String subject;

    private Map data ;          //邮件数据,需要填入邮件模版的数据
    private String template;    //邮件模板

    public MailBean(){

    }

    public String getFrom(){return this.from;}
    public void setFrom(String from){ this.from = from;}

    public String getFromName(){ return this.fromName;}
    public void setFromName(String fromName) {this.fromName = fromName;}

    public String[] getToEmails(){return this.toEmails;}
    public void setToEmails(String[] toEmails){ this.toEmails = toEmails;}

    public String getSubject(){return this.subject;}
    public void setSubject(String subject){this.subject = subject;}

    public Map getData(){return this.data;}
    public void setData(Map data){this.data = data;}

    public String getTemplate(){return this.template;}
    public void setTemplate(String template){this.template = template;}
}
