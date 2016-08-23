package com.allhis.dao;

import com.allhis.toolkit.GlobalTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Map;

/**
 * Created by ljy on 16/7/1.
 * ok
 */
@Repository
public class MysqlEmailDao {

    @Autowired
    @Qualifier("jdbctemplateemail")
    private JdbcTemplate jdbcTemplate;

    public List<Map<String,Object>> getLimitedUser(){
        return jdbcTemplate.queryForList("select umid from iprofile where iswhitelistip=1");
    }

    public List<Map<String,Object>> getLimitedIp(){
        return jdbcTemplate.queryForList("select ip from whitelistip");
    }

    public List<Map<String,Object>> getIprofileInfo(int umid){
        return jdbcTemplate.queryForList("select * from iprofile where id=?", umid);
    }

    public boolean isEmailExists(String email){
        //默认为true，查询出异常时不能开户
        boolean ret = true;
        int count = -1;
        Map<String,Object> map = jdbcTemplate.queryForMap("select count(*) as count from profile where emailaddress=?",email);
        if(map.get("count")!=null){
            count = GlobalTools.convertStringToInt(map.get("count").toString());
        }
        if(count==0){
            ret = false;
        }
        return ret;
    }

    //返回自增长id的值
    public int addToIprofile(final String password,final String siteip,final int siteport,final int dbindex,final int tableindex){
        int ret = -1;
        KeyHolder key=new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator(){

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement preState=con.prepareStatement("insert into iprofile(password,siteip,siteport,dbindex,tableindex) values(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                preState.setString(1, password);
                preState.setString(2,siteip);
                preState.setInt(3, siteport);
                preState.setInt(4,dbindex);
                preState.setInt(5,tableindex);
                return preState;
            }
        },key);
        ret = key.getKey().intValue();
        return ret;
    }

    //按邮件地址添加
    public void addToProfile1(int umid,String email,String createtime){
        jdbcTemplate.update("insert into profile(umid,emailaddress,createtime) values(?,?,?)",umid,email,createtime);
    }

    public int getUmidByEmail(String email){
        int ret = -1;
        List<Map<String,Object>> mapList = jdbcTemplate.queryForList("select * from profile where emailaddress=?",email);
        if(mapList.size()==1){
            Map<String,Object> map = mapList.get(0);
            if(map.get("umid")!=null){
                ret = GlobalTools.convertStringToInt(map.get("umid").toString());
            }
        }
        return ret;
    }

    public int getUmidByMobilenum(String mobilenum){
        int ret = -1;
        List<Map<String,Object>> mapList = jdbcTemplate.queryForList("select * from profile where mobilephone=?",mobilenum);
        if(mapList.size()==1){
            Map<String,Object> map = mapList.get(0);
            if(map.get("umid")!=null){
                ret = GlobalTools.convertStringToInt(map.get("umid").toString());
            }
        }
        return ret;
    }

    public boolean setToken(int umid,String token,String createtime){
        boolean ret = false;
        int i = jdbcTemplate.update("insert into token(umid,token,createtime) values(?,?,?)",umid,token,createtime);
        if(i>0){
            ret = true;
        }
        return ret;
    }

    public void deleteOldToken(int seconds){
        jdbcTemplate.update("delete from token where (now()-createtime)>?",seconds);
    }
}
