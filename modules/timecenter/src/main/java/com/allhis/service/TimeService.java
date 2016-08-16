package com.allhis.service;

import com.allhis.bean.PeriodBean;
import com.allhis.bean.PeriodResp;
import com.allhis.dao.MysqlDao;
import com.allhis.bean.TimeResp;
import com.allhis.toolkit.GlobalTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ljy on 16/7/1.
 * ok
 */
@Service
public class TimeService {

    private static Logger log = LoggerFactory.getLogger(TimeService.class);

    @Value("${system.beforeseconds}")
    private int beforeSeconds;

    @Value("${system.blacklistlifecycle}")
    private long blc;


    @Autowired
    private MysqlDao mysqlDao;


    public TimeResp searchYear(String yearname,int sequence){
        TimeResp timeResp = new TimeResp();
        if(yearname.trim().length()>0 && sequence >=0){
            int nameid = mysqlDao.getIdByName(yearname);
            if(nameid>0){
                int baseYear = mysqlDao.getBaseYear(nameid);
                if (baseYear != 100000000) {
                    timeResp.setErrorCode(0);
                    timeResp.setErrorMessage(getErrorMessage(0));
                    timeResp.setYear(baseYear + sequence - 1);
                } else {
                    log.error("failed in getBaseYear!nameid:{}",nameid);
                }
            }else{
                log.error("failed in getIdByName!yearname:{},nameid:{}",yearname,nameid);
            }
        }else {
            log.error("param error!yearname:{},sequence:{}",yearname,sequence);
        }
        return timeResp;
    }

    public PeriodResp searchPeriod(int year,int periodtype,String area){
        PeriodResp periodResp = new PeriodResp();
        List<Map<String,Object>> mapList = mysqlDao.getPeriodInfo(year,periodtype);
        int periodnameid = -1;
        int periodlevel = -1;
        int beginyear = 1000000;
        int endyear = 1000000;
        String periodName = null;
        List<PeriodBean> periodBeanList = new ArrayList<>();
        for(Map<String,Object> map:mapList){
            if(map.get("periodnameid")!=null){
                periodnameid = GlobalTools.convertStringToInt(map.get("periodnameid").toString());
            }
            if(map.get("periodlevel")!=null){
                periodlevel = GlobalTools.convertStringToInt(map.get("periodlevel").toString());
            }
            if(map.get("beginyear")!=null){
                beginyear = GlobalTools.convertStringToInt(map.get("beginyear").toString());
            }
            if(map.get("endyear")!=null){
                endyear = GlobalTools.convertStringToInt(map.get("endyear").toString());
            }
            if(periodnameid!=-1 && periodlevel!=-1 && beginyear!=1000000 && endyear!=1000000){
                periodName = mysqlDao.getNameById(periodnameid,area);
                if(periodName != null){
                    PeriodBean periodBean = new PeriodBean();
                    periodBean.setPeriodName(periodName);
                    periodBean.setPeriodLevel(periodlevel);
                    periodBean.setFirstYear(beginyear);
                    periodBean.setLastYear(endyear);
                    periodBeanList.add(periodBean);
                }
            }else{
                log.error("error in serarchPeriod! year:{},periodtype:{}",year,periodtype);
            }
        }
        if(periodBeanList.size()>0){
            periodResp.setErrorCode(0);
            periodResp.setErrorMessage(getErrorMessage(0));
            periodResp.setPeriodBeanList(periodBeanList);
        }
        return periodResp;
    }

    public String getErrorMessage(int errorCode){
        String ret;
        if(errorCode == 0) {
            ret = "success";
        }else if(errorCode == -1){
            ret = "参数错误";
        }else if(errorCode == -2){
            ret = "年代偏移量错误";
        }else if(errorCode == -3) {
            ret = "未查询到相关年号";
        }else if(errorCode == -4){
            ret = "查询元年失败";
        }else{
            ret="失败";
        }

        return ret;
    }
//
//    private void initGlobalData(){
//        List<Map<String,Object>> mapList = mysqlEmailDao.getLimitedUser();
//        List<Map<String,Object>> mapList2 = mysqlEmailDao.getLimitedIp();
//        for(Map<String,Object> map:mapList){
//            userList.add(map.get("umid").toString());
//        }
//        for(Map<String,Object> map:mapList2){
//            ipList.add(map.get("ip").toString());
//        }
//    }
//
//    public void reinitGlobalData(){
//        ipList.clear();
//        userList.clear();
//        initGlobalData();
//    }
//
//    private void initMemoryTable(){
//        mysqlLogDao.clearLoginFailM();
//        mysqlLogDao.clearLoginHistoryM();
//        mysqlLogDao.loadDataFromLoginFail();
//        mysqlLogDao.loadDataFromLoginHistory();
//    }
//
//    public void clearData(){
//        //删除登录时间是多少秒之前的记录
//        mysqlLogDao.deleteFromLoginhistory(3600*24*30);
//        mysqlLogDao.deleteFromLoginfaillog(3600*24*30);
//    }
//
//    public void reinitMemoryTable(){
//        initMemoryTable();
//    }
//
//    //用户名、ip校验的错误码范围从-10 至 －19;
//    //－10: ip不被允许 ；
//    //－12: name+ip 错误次数太多，可能在非法尝试密码
//    //－13: ip 错误次数太多，可能在非法尝试不同用户的密码
//    //－14: name 错误次数太多，可能在从不同的ip尝试同一用户的密码
//    private int verifyNameAndIp(String name,String ip,String platform){
//        int ret = 0;
//        int failCount = 0;
//        //查看用户帐号是否设置为限定特定ip访问，如是的话再比对ip
//        if(userList.contains(name)){
//            String subIp = GlobalTools.getIpSegment(ip);
//            if(!ipList.contains(ip) && !ipList.contains(subIp)){
//                ret = -10;
//                log.error("{} from {} id refused to login", name, ip);
//            }
//        }
//
//        if(ret == 0) {
//
//            //name+ip 为条件查询最近登录失败的次数
//            failCount = mysqlLogDao.getFailCount1(name, ip, beforeSeconds);
//            if (failCount > 3) {
//                ret = -12;
//                blackList.put(name+":"+ip,System.currentTimeMillis());
//                log.error("put {} into blacklist!", name + ":" + ip);
//            }
//
//
//            //可能会因一个用户频繁登录失败引起全体无法使用，不妥
////            if(ret == 0) {
////                //ip 为条件查询最近登录失败的次数
////                failCount = mysqlLogDao.getFailCount2(ip, beforeSeconds);
////                if (failCount > 3) {
////                    ret = -13;
////                    blackList.put(ip,System.currentTimeMillis());
////                    log.error("put {} into blacklist!", ip);
////                }
////            }
//
//            if(ret == 0) {
//                //name 为条件查询最近登录失败的次数
//                failCount = mysqlLogDao.getFailCount3(name, beforeSeconds);
//                if (failCount > 3) {
//                    ret = -14;
//                    blackList.put(name,System.currentTimeMillis());
//                    log.error("put {} into blacklist!", name);
//                }
//            }
//        }
//        return ret;
//    }
//
//    //校验登录是否过于频繁
//    private int extVerify(String name,String ip,String platform){
//        int ret = 0;
//        int countInOneMinute = mysqlLogDao.getLoginSuccessCount(name,platform,60);
//        if(countInOneMinute >3){
//            //一分钟内同应用登录过多次且之前4分钟内登录的次数大于4则拒绝该用户短时间内通过该platform再登录
//            int countInFiveMinutes = mysqlLogDao.getLoginSuccessCount(name,platform,300);
//            if((countInFiveMinutes - countInOneMinute)>4) {
//                ret = -20;
//                blackList.put(name+":"+platform,System.currentTimeMillis());
//                log.error("{} login too requently and put it into blacklist!", name + ":" + platform);
//            }
//        }
//        return ret;
//    }
//
//    //校验基本参数是否在黑名单
//    private int verifyBlacklist(String name,String ip,String platform){
//        int ret;
//        if(blackList.containsKey(name)){
//            ret = -30;
//        }else if(blackList.containsKey(ip)){
//            ret = -31;
//        }else if(blackList.containsKey(name+":"+ip)){
//            ret = -32;
//        }else if(blackList.containsKey(name+":"+platform)){
//            ret = -33;
//        }else{
//            ret = 0;
//        }
//        return ret;
//    }
//
//    //校验密码及权限
//    //0:通过  -1:iprofile表里无此用户 －2:密码校验失败  －3:权限校验未通过
//    private int verifyPassAndRight(String name,String pass,String platform){
//        int ret = -1;
//        List<Map<String,Object>> mapList = mysqlEmailDao.getIprofileInfo(name);
//        if(mapList.size()==1){
//            Map<String,Object> map = mapList.get(0);
//            String password = map.get("passwd").toString();
//            String status = map.get("status").toString();
//
//            if(status.equals("0")){
//                ret = -3;
//            }else if(status.equals("1")){
//                if(!platform.equals("web")){
//                    ret = -3;
//                }
//            }else if(status.equals("3")){
//                if(!platform.equals("web") && !platform.equals("pop3")){
//                    ret = -3;
//                }
//            }else if(status.equals("5")){
//                if(!platform.equals("web") && !platform.equals("smtp")){
//                    ret = -3;
//                }
//            }else if(status.equals("7")){
//                if(!platform.equals("web") && !platform.equals("pop3") && !platform.equals("smtp")){
//                    ret = -3;
//                }
//            }else if(status.equals("11")){
//                if(!platform.equals("web") && !platform.equals("pop3") && !platform.equals("imap")){
//                    ret = -3;
//                }
//            }else if(status.equals("15") || status.equals("31")){
//                ret = 0;
//            }else{
//                ret = -3;
//            }
//
//            if(ret != -3){
//                String md5Pass = "";
//                try {
//                    md5Pass = GlobalTools.md5(pass);
//                } catch (NoSuchAlgorithmException e) {
//                    log.error("exception:" + e.toString());
//                }
//
//                if(password.equals(md5Pass)){
//                    ret = 0;
//                }else{
//                    ret = -2;
//                }
//            }
//        }
//        return ret;
//    }
//
//    public void deleteFromBlackList(){
//        for (Map.Entry<String,Long> entry : blackList.entrySet()) {
//            long lInputTime = entry.getValue();
//            if(System.currentTimeMillis() - lInputTime > 1000*60*blc){
//                blackList.remove(entry.getKey());
//            }
//        }
//    }
//
//    //测试 queryforlist
//    public void getMaxtest(){
//        mysqlLogDao.getMaxId();
//    }
}
