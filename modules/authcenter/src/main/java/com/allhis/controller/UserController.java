package com.allhis.controller;

import com.allhis.bean.RetMessage;
import com.allhis.service.AuthService;
import com.allhis.service.UserService;
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
@RequestMapping("/user")
public class UserController {

    @Autowired
    protected UserService userService;

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    //http://localhost:8080/user/adduserbymail?email=test@139email.com&pass=11111&verifyCode=1111&ip=192.168.0.1&platform=web
    @RequestMapping("/adduserbymail")
    public RetMessage authing(
            @RequestParam(value="email", required = true) String email,
            @RequestParam(value="pass", required = true) String pass,
            @RequestParam(value="verifyCode", required = true) String verifyCode,
            @RequestParam(value="ip", defaultValue="")String ip,
            @RequestParam(value="platform", defaultValue="") String platform,
            HttpServletRequest httpServletRequest
    ) {
        String requestIp = httpServletRequest.getRemoteAddr();
        logger.info("client [{}] adduserbymail param: email={} ip={} platform={}",requestIp,email,ip,platform);

        return userService.addUserByMail(email,pass,ip,verifyCode);
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
