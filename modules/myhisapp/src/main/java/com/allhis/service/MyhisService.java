package com.allhis.service;

import com.allhis.bean.RetMessage;
import com.allhis.dao.MysqlDao;
import com.allhis.toolkit.GlobalTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ljy on 16/8/18.
 * ok
 */
@Service
public class MyhisService {

    private static Logger log = LoggerFactory.getLogger(MyhisService.class);

    @Autowired
    UserInfoService userInfoService;
    @Autowired
    MysqlDao mysqlDao;

    public RetMessage createProject(String projectname,int umid){
        RetMessage retMessage = new RetMessage();
        int tableindex = userInfoService.getTableIndex(umid);
        if(tableindex < 0){
            retMessage.setErrorCode("-100");
            retMessage.setErrorMessage("failed to get tableindex");
            log.error("get tableindex failed,umid:{}",umid);

        }else{
            //创建项目
            String nt = String.valueOf(GlobalTools.getTimeBefore(0));
            int projectId = mysqlDao.addToProject(tableindex,umid,projectname,nt);
            if(projectId >0){   //project项目创建成功
                retMessage.setErrorCode("0");
                retMessage.setErrorMessage("success");
                retMessage.setRetContent("projectid="+projectId);
            }else{
                retMessage.setErrorCode("-10");
                retMessage.setErrorMessage("failed to create project");
                log.error("cteate project failed,umid:{} projectname:{} tableindex:{}",umid,projectname,tableindex);
            }
        }

        return retMessage;
    }
}
