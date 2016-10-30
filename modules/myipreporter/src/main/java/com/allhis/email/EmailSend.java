package com.allhis.email;


import com.allhis.toolkit.GlobalTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.Map;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * Created by ljy on 15/6/10.
 * ok
 */
@Component
public class EmailSend {
    //private static Logger log = LoggerFactory.getLogger(EmailService.class);

    @Value("${email.mailfrom}")
    private String mailFrom;
    @Value("${email.mailfromname}")
    private String mailFromName;

    @Autowired
    MailUtil mailUtil;

    //subject,mailto,mailTemplatePath,map参数化
    public boolean mailSend(String subject,String mailto,String templatePath,Map<String,String> map){
        boolean ret =false;
        String template_s;
        String[] mailtos = {""};
        //从参数赋值
        mailtos[0]=mailto;

        String mailTemplatePath;
        //从参数赋值
        mailTemplatePath = templatePath;

        MailBean mailBean = new MailBean();
        mailBean.setFrom(mailFrom);
        mailBean.setFromName(mailFromName);
        mailBean.setSubject(subject);
        mailBean.setToEmails(mailtos);

        //从模版文件读取内容至template_s
        template_s = GlobalTools.readFileToString(mailTemplatePath,false);
        mailBean.setTemplate(template_s);
        // map 用于填充模版数据
        //Map<String,String> map = new HashMap<String,String>();
        //map.put("emailVerifyCode", key);
        //map.put("emailMessage",message);
        mailBean.setData(map);

        //发送邮件
        try {
            if(mailUtil.send(mailBean)){
                ret = true;
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return ret;
    }


    //发送特殊邮件，subject为keepalive，目的是经常用QQ邮箱发些邮件，避免smtp功能自动关闭
//    public boolean keepAliveMailSend(){
//        boolean ret =false;
//        String template_s;
//        String[] mailtos = {""};
//        mailtos[0]=mailTo;
//        String mailTemplatePath;
//
//        mailTemplatePath = notifyMailTemplatePath;
//
//        MailBean mailBean = new MailBean();
//        mailBean.setFrom(mailFrom);
//        mailBean.setFromName(mailFromName);
//        mailBean.setSubject("keepalive");
//        mailBean.setToEmails(mailtos);
//
//        //从模版文件读取内容至template_s
//        template_s = GlobalTools.readFileToString(mailTemplatePath);
//        mailBean.setTemplate(template_s);
//        // map 用于填充模版数据
//        Map<String,String> map = new HashMap<String,String>();
//        map.put("emailMessage","keepalive!");
//        mailBean.setData(map);
//
//        //发送邮件
//        try {
//            if(mailUtil.send(mailBean)){
//                ret = true;
//            }
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//
//        return ret;
//    }

}