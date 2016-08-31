package com.allhis.service;

import com.allhis.bean.RetMessage;
import com.allhis.bean.UserParamBean;
import com.allhis.dao.MysqlEmailDao;
import com.allhis.dao.MysqlLogDao;
import com.allhis.toolkit.GlobalTools;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.mina.util.CopyOnWriteMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ljy on 16/7/1.
 * ok
 */
@Service
public class UserService {

    private static Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private MysqlEmailDao mysqlEmailDao;
    @Autowired
    private MysqlLogDao mysqlLogDao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ObjectMapper mapper;

    public RetMessage addUserByMail(String email,String pass,String userIp,String verifyCode){
        RetMessage retMessage = new RetMessage();
        if(isEmailExists(email)){
            retMessage.setErrorCode("-2");
            retMessage.setErrorMessage("emailaddress already exists");
            log.error("emailaddress:{} already exists",email);
        }else{
            //判断校验码是否对
            if(isVerifyCodeOk(email, verifyCode)){
                String encodedPass = passwordEncoder.encode(pass);
                UserParamBean userParamBean = getUserParam(userIp);
                int umid = createUserAccount1(email,encodedPass,userParamBean.getSiteip(),userParamBean.getSiteport(),userParamBean.getDbindex(),userParamBean.getTableindex());
                if(umid > 0){
                    retMessage.setErrorCode("0");
                    retMessage.setErrorMessage("success");
                }else{
                    log.error("create user failed! email:{}",email);
                }
            }else{
                retMessage.setErrorCode("-3");
                retMessage.setErrorMessage("verifyCode check failed");
                log.error("verifyCode check failed,email:{} verifyCode:{}",email,verifyCode);
            }
        }
        return retMessage;
    }

    private boolean isEmailExists(String email){
        return mysqlEmailDao.isEmailExists(email);
    }

    private boolean isVerifyCodeOk(String email,String verifyCode){
        boolean ret = false;
        //todo:
        ret = true;
        return ret;
    }

    //通过邮件地址创建用户帐号
    @Transactional
    private int createUserAccount1(String email,String pass,String siteip,int siteport,int dbindex,int tableindex){
        int ret = -1;
        int umid = mysqlEmailDao.addToIprofile(pass,siteip,siteport,dbindex,tableindex);
        String createtime = String.valueOf(GlobalTools.getTimeBefore(0));
        mysqlEmailDao.addToProfile1(umid,email,createtime);
        if(umid>0){
            ret = umid;
        }
        return ret;
    }

    private UserParamBean getUserParam(String userIp){
        UserParamBean userParamBean = new UserParamBean();
        //todo:根据某些规则确定用户参数
        return userParamBean;
    }

    public RetMessage getUserInfo(int umid){
        RetMessage retMessage = new RetMessage();
        List<Map<String,Object>> mapList = mysqlEmailDao.getIprofileInfo(umid);
        if(mapList.size()==1){
            String siteip = null;
            String siteport = null;
            String dbindex = null;
            String tableindex = null;
            Map<String,Object> map = mapList.get(0);
            if(map.get("siteip")!=null){
                siteip = map.get("siteip").toString();
            }
            if(map.get("siteport")!=null){
                siteport = map.get("siteport").toString();
            }
            if(map.get("dbindex")!=null){
                dbindex = map.get("dbindex").toString();
            }
            if(map.get("tableindex")!=null){
                tableindex = map.get("tableindex").toString();
            }
            if(siteip!=null && siteport!=null && dbindex!=null && tableindex!=null){
                retMessage.setErrorCode("0");
                retMessage.setErrorMessage("success");
                Map<String,String> contentmap = new HashMap<>();
                contentmap.put("siteip",siteip);
                contentmap.put("siteport",siteport);
                contentmap.put("dbindex",dbindex);
                contentmap.put("tableindex",tableindex);
                retMessage.setRetContent(map2JsonString(contentmap));
            }else{
                retMessage.setErrorCode("-2");
                retMessage.setErrorMessage("get userinfo failed");
                log.error("umid:{} get user info failed!",umid);
            }

        }else{
            retMessage.setErrorCode("-1");
            retMessage.setErrorMessage("umid:"+ umid +" not exists!");
            log.error("umid:{} not exists!",umid);
        }
        return retMessage;
    }

    private String map2JsonString(Map<String,String> map){
        String ret = null;
        if(map!=null){
            try {
                ret = mapper.writeValueAsString(map);
            } catch (JsonProcessingException e) {
                log.error("exception catched in map converting to json string! {}",e.toString());
            }
        }else{
            log.error("invalid jsonmap!");
        }
        return ret;
    }

}
