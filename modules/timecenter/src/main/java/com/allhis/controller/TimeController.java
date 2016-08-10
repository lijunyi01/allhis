package com.allhis.controller;

import com.allhis.service.TimeService;
import com.allhis.timecenter.TimeResp;
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

    //http://localhost:8080/timesearch/timejson?yearname=鲁隐公&sequence=1
    @RequestMapping("/timejson")
    public TimeResp authing(
            @RequestParam(value="yearname") String yearname,
            @RequestParam(value="sequence") String sequence,
            HttpServletRequest httpServletRequest
    ) {
        String requestIp = httpServletRequest.getRemoteAddr();
        logger.info("client [{}] search param: yearname={} sequence={}",requestIp,yearname,sequence);

        TimeResp timeResp = timeService.searchYear(yearname,sequence);
        return timeResp;
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
