package com.allhis.controller;

import com.allhis.service.AccountService;
//import allcom.toolkit.GlobalTools;
import com.allhis.bean.RetMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.NamedThreadLocal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
//import java.util.Map;

/**
 * Created by ljy on 15/5/12.
 * class 上的@RequestMapping("/aaa")注释和方法上的@RequestMapping("/greeting")要叠加
 * http://localhost:8080/aaa/greeting?name=ljy  才能正常访问；若果class上没有@RequestMapping("/aaa")，则
 * 访问http://localhost:8080/greeting?name=ljy 即可
 */

@CrossOrigin
@RestController
@RequestMapping("/ah_loginapi")
public class LoginController {

    private final AccountService accountService;

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    public LoginController(AccountService accountService) {
        this.accountService = accountService;
    }

    //  客户端访问URL  http://222.46.16.172:83/ah_loginapi/login?username=test@139email.com&password=111111
    @RequestMapping(value = "/login")
    public RetMessage loginAuth(
            @RequestParam(value="username") String username,
            @RequestParam(value="password") String password,
            @RequestParam(value = "devicetype",required = false,defaultValue = "web") String devicetype,
            @RequestParam(value = "deviceinfo",required = false,defaultValue = "")String deviceinfo,
//            @RequestParam(value = "ip",required = false,defaultValue = "")String ip,
            @RequestParam(value = "area",required = false,defaultValue = "cn")String area,
            HttpServletRequest httpServletRequest
    ) {

        RetMessage ret;
        String clientIp = httpServletRequest.getHeader("X-Real-IP");
        if(clientIp == null){
            clientIp = httpServletRequest.getRemoteAddr();
        }
        log.info("userlogin,username is:"+username +" and password is:"+password + " and ip is:"+clientIp);
        ret = accountService.auth(username,password,clientIp,area);
        return ret;
    }

    @RequestMapping(value = "/errorpage")
    public String getError(){
        return "error page!!!";
    }


    @RequestMapping(value = "/")
    public String getHome(){
        return "home page!!!";
    }


}
