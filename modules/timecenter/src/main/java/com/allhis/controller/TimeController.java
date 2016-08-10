package com.allhis.controller;

import com.allhis.timecenter.AuthResp;
import com.allhis.service.TimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by ljy on 16/7/1.
 * ok
 */
@RestController
@RequestMapping("/timesearch")
public class TimeController {

    @Autowired
    private TimeService timeService;

    private static Logger logger = LoggerFactory.getLogger(TimeController.class);

    //http://localhost:8080/auth/authjson?name=test@139email.com&pass=11111&ip=192.168.0.1&platform=web
    @RequestMapping("/timejson")
    public AuthResp authing(
            @RequestParam(value="name", defaultValue="") String name,
            @RequestParam(value="pass", defaultValue="") String pass,
            @RequestParam(value="ip", defaultValue="")String ip,
            @RequestParam(value="platform", defaultValue="") String platform,
            HttpServletRequest httpServletRequest
    ) {
        int errorCode;
        String errorMessage;
        String requestIp = httpServletRequest.getRemoteAddr();
        logger.info("client [{}] auth param: umid={} ip={} platform={}",requestIp,name,ip,platform);
        errorCode = timeService.getErrorCode(name,pass,ip,platform);
        errorMessage = timeService.getErrorMessage(errorCode);
        logger.info("server [{}] auth response-json:{} {}",requestIp,errorCode,errorMessage);
        return new AuthResp(errorCode,errorMessage);
    }

//    @RequestMapping("/authplain")
//    public String authing2(
//            @RequestParam(value="name", defaultValue="") String name,
//            @RequestParam(value="pass", defaultValue="") String pass,
//            @RequestParam(value="ip", defaultValue="") String ip,
//            @RequestParam(value="platform", defaultValue="") String platform,
//            HttpServletRequest httpServletRequest
//    ) {
//        int errorCode;
//        String errorMessage;
//        String requestIp = httpServletRequest.getRemoteAddr();
//        logger.info("client [{}] auth param: umid={} ip={} platform={}",requestIp,name,ip,platform);
//        errorCode = authService.getErrorCode(name,pass,ip,platform);
//        errorMessage = authService.getErrorMessage(errorCode);
//        logger.info("server [{}] auth response-plain:{} {}",requestIp,errorCode,errorMessage);
//        return errorCode + " "+errorMessage;
//    }

}
