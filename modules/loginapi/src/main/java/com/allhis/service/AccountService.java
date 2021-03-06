package com.allhis.service;

import com.allhis.bean.RetMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by ljy on 16/8/25.
 * ok
 */
@Service
public class AccountService {
    private static Logger log = LoggerFactory.getLogger(AccountService.class);

    @Value("${system.authcenterurl}")
    private String authcenterurl;

    private final RestTemplate restTemplate;

    @Autowired
    public AccountService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public RetMessage auth(String username,String password,String clientIp,String area){
        RetMessage retMessage = null;
        String authurl = authcenterurl + "name="+username+"&pass="+password+"&ip="+clientIp+"&platform=web";
        try {
            retMessage = restTemplate.getForObject(authurl, RetMessage.class);
        }catch (Exception e){
            log.error("catch exception! {}",e.toString());
        }
        return retMessage;
    }
}
