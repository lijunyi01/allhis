package com.allhis.dao;

import com.allhis.toolkit.GlobalTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by ljy on 16/7/1.
 * ok
 */
@Repository
public class MysqlDao {

    @Autowired
    @Qualifier("jdbctemplate1")
    private JdbcTemplate jdbcTemplate;

    public int getBaseYear(String yearName){
        int ret = 100000000;
        List<Map<String,Object>> mapList = jdbcTemplate.queryForList("select * from yearbase where yearname=?",yearName);
        if(mapList.size()==1){
            Map<String,Object> map = mapList.get(0);
            if(map.get("firstyear")!=null){
                ret = GlobalTools.convertStringToInt(map.get("firstyear").toString());
            }
        }
        return ret;
    }

//    //查询最近几秒内登录失败的记录数 ,按ip和用户名过滤
//    public int getFailCount1(String name,String ip,int beforeSeconds){
//        int ret = 0;
//        Map<String,Object> map = jdbcTemplate.queryForMap("select count(*) as count from loginfaillog_m where loginip=? and umid=? and (now()-logintime)<?",ip,name,beforeSeconds);
//        String c = map.get("count").toString();
//        ret = GlobalTools.convertStringToInt(c);
//        return ret;
//    }
//
//    //查询最近几秒内登录失败的记录数 ,按ip过滤
//    public int getFailCount2(String ip,int beforeSeconds){
//        int ret = 0;
//        Map<String,Object> map = jdbcTemplate.queryForMap("select count(*) as count from loginfaillog_m where loginip=? and (now()-logintime)<?",ip,beforeSeconds);
//        String c = map.get("count").toString();
//        ret = GlobalTools.convertStringToInt(c);
//        return ret;
//    }
//
//    //查询最近几秒内登录失败的记录数 ,按name过滤
//    public int getFailCount3(String name,int beforeSeconds){
//        int ret = 0;
//        Map<String,Object> map = jdbcTemplate.queryForMap("select count(*) as count from loginfaillog_m where umid=? and (now()-logintime)<?",name,beforeSeconds);
//        String c = map.get("count").toString();
//        ret = GlobalTools.convertStringToInt(c);
//        return ret;
//    }
//
//    //查询最近几秒内登录成功的记录数 ,按name，platform过滤
//    public int getLoginSuccessCount(String name,String platform,int beforeSeconds){
//        int ret = 0;
//        Map<String,Object> map = jdbcTemplate.queryForMap("select count(*) as count from login_history_m where umid=? and loginfrom=? and (now()-logintime)<?",name,platform,beforeSeconds);
//        String c = map.get("count").toString();
//        ret = GlobalTools.convertStringToInt(c);
//        return ret;
//    }
//
//    public void recordLoginFail(String name,String ip,String platform,int errorCode){
//        jdbcTemplate.update("insert into loginfaillog values(?,now(),?,?,?)",name,ip,platform,errorCode);
//    }
//
//    public void recordLoginFail_M(String name,String ip,String platform,int errorCode){
//        jdbcTemplate.update("insert into loginfaillog_m values(?,now(),?,?,?)",name,ip,platform,errorCode);
//    }
//
//    public void recordLoginhistory(String name,String ip,String platform){
//        jdbcTemplate.update("insert into login_history values(?,now(),?,?)",name,ip,platform);
//    }
//
//    public void recordLoginhistory_M(String name,String ip,String platform){
//        jdbcTemplate.update("insert into login_history_m values(?,now(),?,?)",name,ip,platform);
//    }
//
//    public void clearLoginFailM(){
//        jdbcTemplate.update("truncate table loginfaillog_m");
//    }
//
//    public void clearLoginHistoryM(){
//        jdbcTemplate.update("truncate table login_history_m");
//    }
//
//    //取最近一小时的放入内存表
//    public void loadDataFromLoginFail(){
//        jdbcTemplate.update("insert into loginfaillog_m select * from loginfaillog where (now()-logintime)<3600");
//    }
//
//    public void loadDataFromLoginHistory(){
//        jdbcTemplate.update("insert into login_history_m select * from login_history where (now()-logintime)<3600");
//    }
//
//    public void deleteFromLoginhistory(int beforeSec){
//        jdbcTemplate.update("delete from login_history where (now()-logintime)>?",beforeSec);
//    }
//
//    public void deleteFromLoginfaillog(int beforeSec){
//        jdbcTemplate.update("delete from loginfaillog where (now()-logintime)>?",beforeSec);
//    }
//
//    //测试queryforlist  查到null也会使list的size为1
//    public int getMaxId(){
//        int ret = -1;
//        List<Map<String,Object>> list = jdbcTemplate.queryForList("select max(logintime) as maxid from loginfaillog");
//        if(list.size()==1){
//            //由于list.get(0).get("maxid") 有可能是null，因此此处有可能导致程序异常
//            //list.get(0)一定不是null
//            Map<String,Object> map = list.get(0);
//            if(map !=null) {
//                ret = GlobalTools.convertStringToInt(list.get(0).get("maxid").toString());
//            }
//        }
//        return ret;
//    }
}
