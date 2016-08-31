package com.allhis.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * Created by ljy on 16/8/18.
 * ok
 */
@Repository
public class MysqlDao {

    @Autowired
    @Qualifier("jdbctemplate1")
    JdbcTemplate jdbcTemplate;

    //返回自增长id的值
    public int addProject(final int tableindex,final int umid,final String projectname, final String createtime){
        int ret = -1;
        KeyHolder key=new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator(){

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement preState=con.prepareStatement("insert into myproject"+ tableindex +"(umid,projectname,createtime) values(?,?,?)", Statement.RETURN_GENERATED_KEYS);
                preState.setInt(1,umid);
                preState.setString(2,projectname);
                preState.setString(3,createtime);
                return preState;
            }
        },key);
        ret = key.getKey().intValue();
        return ret;
    }

    public int addItem(final int tableindex,final int umid,final int projectid,final String itemname, final String itemcontent, final String begintime, final String endtime){
        int ret = -1;
        KeyHolder key=new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator(){

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement preState=con.prepareStatement("insert into projectitem"+ tableindex +"(umid,projectid,itemname,itemcontent,begintime,endtime) values(?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                preState.setInt(1,umid);
                preState.setInt(2, projectid);
                preState.setString(3, itemname);
                preState.setString(4,itemcontent);
                preState.setString(5,begintime);
                preState.setString(6,endtime);
                return preState;
            }
        },key);
        ret = key.getKey().intValue();
        return ret;
    }

    public boolean projectIdexists(int umid,int projectId,int tableindex){
        boolean ret = false;
        List<Map<String,Object>> mapList = jdbcTemplate.queryForList("select * from myproject"+tableindex+" where id=? and umid=?",projectId,umid);
        if(mapList.size()==1){
            ret = true;
        }
        return ret;
    }

    public List<Map<String,Object>> getAllProjects(int umid,int tableindex,String sortFlag){
        List<Map<String,Object>> mapList;
        if(sortFlag!=null && sortFlag.equals("lasttime")){
            mapList = jdbcTemplate.queryForList("select * from myproject" + tableindex + " where umid=? order by lasttime desc", umid);
        }else {
            mapList = jdbcTemplate.queryForList("select * from myproject" + tableindex + " where umid=? order by id desc", umid);
        }
        return mapList;
    }

}
