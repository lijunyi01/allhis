package com.allhis.controller;

import com.allhis.bean.RetMessage;
import com.allhis.service.MyhisService;
import com.allhis.toolkit.GlobalTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by ljy on 16/7/1.
 * ok
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/appinterface")
public class appController {

    @Autowired
    private MyhisService myhisService;

    private static Logger logger = LoggerFactory.getLogger(appController.class);

    //新增项目接口
    //http://localhost:8080/appinterface/createproject?projectname=恐龙的历程&umid=666
//    @RequestMapping("/createproject")
//    public RetMessage api1(
//            @RequestParam(value="projectname",required = true) String projectname,
//            @RequestParam(value="umid",required = true) int umid,
//            HttpServletRequest httpServletRequest
//    ) {
//        String requestIp = httpServletRequest.getRemoteAddr();
//        logger.info("client [{}] createproject param: projectname={} umid={}",requestIp,projectname,umid);
//
//        RetMessage retMessage = myhisService.createProject(projectname,umid);
//        return retMessage;
//    }

    //通用接口
    //http://router.cimba.cn:8091/appinterface/gen?umid=1&functionName=getAllProjects&generalParam={}
    //http://router.cimba.cn:8091/appinterface/gen?umid=1&functionName=getProjectItems&generalParam={"projectId":91}
    @RequestMapping("/gen")
    public RetMessage genapi(
            @RequestParam(value="umid",required = false, defaultValue = "-1") int umid,
            @RequestParam(value="functionName",required = true) String functionName,
            @RequestParam(value="generalParam",required = true) String generalParam,
            HttpServletRequest httpServletRequest
    ) {
        String requestIp = httpServletRequest.getRemoteAddr();
        int umidInHeader = GlobalTools.convertStringToInt(httpServletRequest.getHeader("Umid"));

        logger.debug("client [{}] gen param: functionname={} umid={} umidInHeader={} generalParam={}", requestIp, functionName,umid,umidInHeader,generalParam);

        if(umid <=0){
            umid = umidInHeader;
        }
        RetMessage retMessage = myhisService.generalService(umid,functionName,generalParam);
        return retMessage;
    }
}
