package com.allhis.controller;

import com.allhis.service.TimeService;
import com.allhis.bean.PeriodResp;
import com.allhis.bean.TimeResp;
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

    //通过年号查公元纪年
    //http://localhost:8080/timesearch/year?yearname=鲁隐公&sequence=1
    @RequestMapping("/year")
    public TimeResp time1(
            @RequestParam(value="yearname",required = true) String yearname,
            @RequestParam(value="sequence",required = true) int sequence,
            HttpServletRequest httpServletRequest
    ) {
        String requestIp = httpServletRequest.getRemoteAddr();
        logger.info("client [{}] search param: yearname={} sequence={}",requestIp,yearname,sequence);

        TimeResp timeResp = timeService.searchYear(yearname,sequence);
        return timeResp;
    }

    //通过公元年份查历史时期
    //http://localhost:8080/timesearch/period?year=-998&area=cn
    @RequestMapping("/period")
    public PeriodResp time2(
            @RequestParam(value="year",required = true) int year,
            @RequestParam(value="periodtype",required = false,defaultValue = "1") int periodtype,
            @RequestParam(value="area",required = false,defaultValue = "cn") String area,
            HttpServletRequest httpServletRequest
    ) {
        String requestIp = httpServletRequest.getRemoteAddr();
        logger.info("client [{}] search param: year={}",year);

        PeriodResp periodResp = timeService.searchPeriod(year, periodtype,area);
        return periodResp;
    }
}
