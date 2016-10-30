package com.allhis.service;

import com.allhis.email.EmailSend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ljy on 16/10/30.
 * ok
 */
@Service
public class AppService {

    private static Logger logger = LoggerFactory.getLogger(AppService.class);

    public static Map<String,String> ipMap = new HashMap<>();

    @Autowired
    private EmailSend emailSend;

    @Value("${email.mailTemplatePath}")
    private String templatePath;


    public void reportip(String clientIp){
        if(ipMap.size() == 0){
            ipMap.put("privateYunIp",clientIp);
            if(sendIpByMail(clientIp)){
                ipMap.put("privateYunIp",clientIp);
            }else{
                logger.error("send mail failed when init!");
            }
        }else{
            if(!ipMap.get("privateYunIp").equals(clientIp)){
                if(sendIpByMail(clientIp)){
                    ipMap.put("privateYunIp",clientIp);
                }else{
                    logger.error("send mail failed!");
                }
            }else{
                logger.info("ip no changed! ip is:{}",clientIp);
            }
        }
    }

    private boolean sendIpByMail(String ip){
        boolean ret;

        Map<String,String> map = new HashMap<>();
        map.put("ip",ip);
        ret = emailSend.mailSend("私有云ip通知", "lijunyi01@qq.com",templatePath,map);

        return ret;
    }

}
