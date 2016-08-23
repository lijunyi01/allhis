package com.allhis.service;

import com.allhis.bean.RetMessage;
import com.allhis.dao.MysqlEmailDao;
import com.allhis.dao.MysqlLogDao;
import com.allhis.toolkit.GlobalTools;
import org.apache.mina.util.CopyOnWriteMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by ljy on 16/7/1.
 * ok
 */
@Service
public class AuthService {

    private static Logger log = LoggerFactory.getLogger(AuthService.class);

    @Value("${system.beforeseconds}")
    private int beforeSeconds;
    @Value("${system.tokenlifecycle}")
    private int tokenlifecycle;

    @Value("${system.blacklistlifecycle}")
    private long blc;


    @Autowired
    private MysqlEmailDao mysqlEmailDao;
    @Autowired
    private MysqlLogDao mysqlLogDao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private boolean isGlobalDataInited = false;

//    //全局变量，存放允许登录的ip（对特定用户作限制）
//    public static List<String> ipList = new CopyOnWriteArrayList<>();
//    //全局变量,存放特定用户的帐号，这些帐号只允许从特定ip登录
//    public static List<String> userList = new CopyOnWriteArrayList<>();

    //全局变量，存放黑名单umid+ip /umid/ip 及放入的时间（毫秒）
    public static Map<String,Long> blackList = new CopyOnWriteMap<>();


    public RetMessage auth(String name,String pass,String ip,String platform){
        RetMessage retMessage = new RetMessage();
        //初始化
        if(!isGlobalDataInited){
            initMemoryTable();
            this.isGlobalDataInited = true;
        }

        int umid = getUmidByName(name);
        if(umid > 0) {
            int errorCode = getErrorCode(umid,pass,ip,platform);

            //校验成功需额外返回umid,token,siteip,siteport 等信息
            if(errorCode == 0){
                //先设置默认的错误值
                retMessage.setErrorCode("-100");
                retMessage.setErrorMessage(getErrorMessage(-100));
                String token = UUID.randomUUID().toString();
                if(insertToken(umid,token)){
                    List<Map<String,Object>> mapList = mysqlEmailDao.getIprofileInfo(umid);
                    String siteip = null;
                    String siteport = null;
                    if(mapList.size()==1){
                        Map<String,Object> map = mapList.get(0);
                        if(map.get("siteip")!=null){
                            siteip = map.get("siteip").toString();
                        }
                        if(map.get("siteport")!=null){
                            siteport = map.get("siteport").toString();
                        }
                        if(siteip!=null && siteport!=null){
                            retMessage.setErrorCode("0");
                            retMessage.setErrorMessage(getErrorMessage(0));
                            retMessage.setRetContent("umid="+umid+"<[CDATA]>token="+token+"<[CDATA]>siteip="+siteip+"<[CDATA]>siteport="+siteport);
                        }
                    }else{
                        log.error("get iprofile info failed! umid:{}",umid);
                    }
                }else{
                    log.error("insert token failed! umid:{},token:{}",umid,token);
                }
            }else{
                retMessage.setErrorCode(errorCode+"");
                retMessage.setErrorMessage(getErrorMessage(errorCode));
            }
        }else{
            retMessage.setErrorCode("-1");
            retMessage.setErrorMessage(getErrorMessage(-1));
            log.error("{} not exists!",name);
        }

        return retMessage;
    }


    //0:通过  -1:iprofile表里无此用户 －2:密码校验失败  －3:权限校验未通过
    //－10 至 -19 : verifyNameAndIp() 返回的错误
    //－20 至 -29 : extVerify() 返回的错误
    //-30 至 -39 : verifyBlacklist() 返回的错误
    public int getErrorCode(int umid,String pass,String ip,String platform){
        int ret = -1;

            ret = verifyBlacklist(umid, ip, platform);

            if (ret == 0) {
                //校验ip是否被允许登录等，是否再尝试密码等
                ret = verifyNameAndIp(umid, ip, platform);
            }

            if (ret == 0) {
                //校验是否登录太频繁
                ret = extVerify(umid, ip, platform);
            }

            //通过上述校验才继续校验其它内容；否则返回上述校验的错误码
            if (ret == 0) {
                ret = verifyPassAndRight(umid, pass, platform);
                if (ret != 0) {
                    mysqlLogDao.recordLoginFail(umid, ip, platform, ret);
                    mysqlLogDao.recordLoginFail_M(umid, ip, platform, ret);
                } else {
                    mysqlLogDao.recordLoginhistory(umid, ip, platform);
                    mysqlLogDao.recordLoginhistory_M(umid, ip, platform);
                }
            }

        return ret;
    }

    public String getErrorMessage(int errorCode){
        String ret;
        if(errorCode == 0) {
            ret = "成功";
        }else if(errorCode == -1){   //其实是用户名不存在
            ret = "用户名密码错";
        }else if(errorCode == -2){   //真正的用户名密码校验错
            ret = "用户名密码错";
        }else if(errorCode == -3){
            ret = "无权限";
        }else if(errorCode == -10){
            ret = "拒绝从此ip登录";
        }else if(errorCode >=-14 && errorCode <= -12) {
            ret = "错误次数太多，可能在非法尝试密码";
        }else if(errorCode == -20) {
            ret = "登录太频繁";
        }else if(errorCode >=-39 && errorCode <= -30) {
            ret = "登录参数被列入黑名单，暂时不允许登录";
        }else if(errorCode == -40){
            ret = "token check failed";
        }else{
            ret="失败";
        }

        return ret;
    }

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

//    public void reinitGlobalData(){
//        ipList.clear();
//        userList.clear();
//        initGlobalData();
//    }

    private void initMemoryTable(){
        mysqlLogDao.clearLoginFailM();
        mysqlLogDao.clearLoginHistoryM();
        mysqlLogDao.loadDataFromLoginFail();
        mysqlLogDao.loadDataFromLoginHistory();
    }

    public void clearData(){
        //删除登录时间是多少秒之前的记录
        mysqlLogDao.deleteFromLoginhistory(3600*24*30);
        mysqlLogDao.deleteFromLoginfaillog(3600*24*30);
    }

    public void reinitMemoryTable(){
        initMemoryTable();
    }

    //用户名、ip校验的错误码范围从-10 至 －19;
    //－10: ip不被允许 ；
    //－12: name+ip 错误次数太多，可能在非法尝试密码
    //－13: ip 错误次数太多，可能在非法尝试不同用户的密码
    //－14: name 错误次数太多，可能在从不同的ip尝试同一用户的密码
    private int verifyNameAndIp(int umid,String ip,String platform){
        int ret = 0;
        int failCount = 0;

        //name+ip 为条件查询最近登录失败的次数
        failCount = mysqlLogDao.getFailCount1(umid+"", ip, beforeSeconds);
        if (failCount > 3) {
            ret = -12;
            blackList.put(umid+":"+ip,System.currentTimeMillis());
            log.error("put {} into blacklist!", umid + ":" + ip);
        }

        if(ret == 0) {
            //name 为条件查询最近登录失败的次数
            failCount = mysqlLogDao.getFailCount3(umid+"", beforeSeconds);
            if (failCount > 3) {
                ret = -14;
                blackList.put(umid+"",System.currentTimeMillis());
                log.error("put {} into blacklist!", umid);
            }
        }

        return ret;
    }

    //校验登录是否过于频繁
    private int extVerify(int umid,String ip,String platform){
        int ret = 0;
        int countInOneMinute = mysqlLogDao.getLoginSuccessCount(umid+"",platform,60);
        if(countInOneMinute >3){
            //一分钟内同应用登录过多次且之前4分钟内登录的次数大于4则拒绝该用户短时间内通过该platform再登录
            int countInFiveMinutes = mysqlLogDao.getLoginSuccessCount(umid+"",platform,300);
            if((countInFiveMinutes - countInOneMinute)>4) {
                ret = -20;
                blackList.put(umid+":"+platform,System.currentTimeMillis());
                log.error("{} login too requently and put it into blacklist!", umid + ":" + platform);
            }
        }
        return ret;
    }

    //校验基本参数是否在黑名单
    private int verifyBlacklist(int umid,String ip,String platform){
        int ret;
        if(blackList.containsKey(umid)){
            ret = -30;
        }else if(blackList.containsKey(ip)){
            ret = -31;
        }else if(blackList.containsKey(umid+":"+ip)){
            ret = -32;
        }else if(blackList.containsKey(umid+":"+platform)){
            ret = -33;
        }else{
            ret = 0;
        }
        return ret;
    }

    //校验密码及权限
    //0:通过  -1:iprofile表里无此用户 －2:密码校验失败  －3:权限校验未通过
    private int verifyPassAndRight(int umid,String pass,String platform){
        int ret = -1;
        List<Map<String,Object>> mapList = mysqlEmailDao.getIprofileInfo(umid);
        if(mapList.size()==1){
            Map<String,Object> map = mapList.get(0);
            String password = map.get("password").toString();
            String status = map.get("status").toString();

            if(status.equals("0")){
                ret = -3;
            }else if(status.equals("1")){
                if(!platform.equals("web")){
                    ret = -3;
                }
            }else if(status.equals("3")){
                if(!platform.equals("web") && !platform.equals("pop3")){
                    ret = -3;
                }
            }else if(status.equals("5")){
                if(!platform.equals("web") && !platform.equals("smtp")){
                    ret = -3;
                }
            }else if(status.equals("7")){
                if(!platform.equals("web") && !platform.equals("pop3") && !platform.equals("smtp")){
                    ret = -3;
                }
            }else if(status.equals("11")){
                if(!platform.equals("web") && !platform.equals("pop3") && !platform.equals("imap")){
                    ret = -3;
                }
            }else if(status.equals("15") || status.equals("31")){
                ret = 0;
            }else{
                ret = -3;
            }

            if(ret != -3){
                boolean isPasswordOk = false;
                try{
                    isPasswordOk = passwordEncoder.matches(pass, password);
                }catch (Exception e){
                    log.info("password maybe set by hand!!!");
                    e.printStackTrace();
                }

                if(isPasswordOk){
                    ret = 0;
                }else{
                    ret = -2;
                }
            }
        }
        return ret;
    }

    public void deleteFromBlackList(){
        for (Map.Entry<String,Long> entry : blackList.entrySet()) {
            long lInputTime = entry.getValue();
            if(System.currentTimeMillis() - lInputTime > 1000*60*blc){
                blackList.remove(entry.getKey());
            }
        }
    }

    public int getUmidByName(String name){
        int ret = -1;
        if(name.indexOf("@")>-1){     //邮件地址登录
            ret = mysqlEmailDao.getUmidByEmail(name);
        }else{      //手机号码登录
            ret = mysqlEmailDao.getUmidByMobilenum(name);
        }
        return ret;
    }

    private boolean insertToken(int umid,String token){
        boolean ret = false;
        String nt = String.valueOf(GlobalTools.getTimeBefore(0));
        if(mysqlEmailDao.setToken(umid,token,nt)){
            ret = true;
        }
        return ret;
    }

    public void deleteOldToken(){
        //tokenlifecycle单位为分钟，转化为秒
        mysqlEmailDao.deleteOldToken(tokenlifecycle*60);
    }

    public RetMessage tokencheck(int umid,String token){
        RetMessage retMessage = new RetMessage();
        int count = mysqlEmailDao.getTokenCount(umid,token,tokenlifecycle*60);
        if(count > 0){
            retMessage.setErrorCode("0");
            retMessage.setErrorMessage(getErrorMessage(0));
            log.debug("umid:{} token check ok!",umid);
        }else{
            retMessage.setErrorCode("-40");
            retMessage.setErrorMessage(getErrorMessage(-40));
            log.error("umid:{} token check failed! token:{}",umid,token);
        }
        return retMessage;
    }
}
