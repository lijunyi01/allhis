package com.allhis.service;

import com.allhis.bean.RetMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

    public RetMessage auth(String username,String password,String clientIp,String area){
        RetMessage retMessage;
        RestTemplate restTemplate = new RestTemplate();
        String authurl = authcenterurl + "name="+username+"&pass="+password+"&ip="+clientIp+"&platform=web";
        retMessage = restTemplate.getForObject(authurl, RetMessage.class);
        return retMessage;
    }
}
