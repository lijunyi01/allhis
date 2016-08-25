package com.allhis.controller;

import com.allhis.bean.RetMessage;
import com.allhis.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by ljy on 16/7/1.
 * ok
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    private static Logger logger = LoggerFactory.getLogger(AuthController.class);

    //http://localhost:8080/auth/authjson?name=test@139email.com&pass=11111&ip=192.168.0.1&platform=web
    //http://localhost:8080/auth/authjson?name=18001831657&pass=11111&ip=192.168.0.1&platform=web
    @RequestMapping("/authjson")
    public RetMessage authing(
            @RequestParam(value="name", defaultValue="") String name,
            @RequestParam(value="pass", defaultValue="") String pass,
            @RequestParam(value="ip", defaultValue="")String ip,
            @RequestParam(value="platform", defaultValue="") String platform,
            HttpServletRequest httpServletRequest
    ) {

        String requestIp = httpServletRequest.getRemoteAddr();
        logger.info("client [{}] auth param: name={} ip={} platform={}",requestIp,name,ip,platform);
        RetMessage retMessage = authService.auth(name,pass,ip,platform);
        return retMessage;
    }

    //http://localhost:8080/auth/authtoken?umid=1&token=38c92a61-490d-437f-9923-9442980a7d9e
    @RequestMapping("/authtoken")
    public RetMessage authtoken(
            @RequestParam(value="umid", required = true) int umid,
            @RequestParam(value="token", required = true) String token,
            HttpServletRequest httpServletRequest
    ) {

        String requestIp = httpServletRequest.getRemoteAddr();
        logger.info("token check param: umid={} token={}",umid,token);
        RetMessage retMessage = authService.tokencheck(umid,token);
        return retMessage;
    }


}
