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

    public int addItemTip(final int tableindex,final int umid,final int projectId,final int itemId,final String tipContent){
        int ret = -1;
        KeyHolder key=new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator(){

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement preState=con.prepareStatement("insert into itemtips"+ tableindex +"(umid,projectid,itemid,tipcontent) values(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                preState.setInt(1,umid);
                preState.setInt(2, projectId);
                preState.setInt(3, itemId);
                preState.setString(4,tipContent);
                return preState;
            }
        },key);
        ret = key.getKey().intValue();
        return ret;
    }

    public int addItemFile(final int tableindex,final int umid,final int projectId,final int itemId,final String fileName,final String fileSuffix,final String filePath){
        int ret = -1;
        KeyHolder key=new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator(){

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement preState=con.prepareStatement("insert into itemfile"+ tableindex +"(umid,projectid,itemid,filename,filesuffix,filepath) values(?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                preState.setInt(1,umid);
                preState.setInt(2, projectId);
                preState.setInt(3, itemId);
                preState.setString(4,fileName);
                preState.setString(5,fileSuffix);
                preState.setString(6,filePath);
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

    public boolean itemIdexists(int umid,int itemId,int tableindex){
        boolean ret = false;
        List<Map<String,Object>> mapList = jdbcTemplate.queryForList("select * from projectitem"+tableindex+" where id=? and umid=?",itemId,umid);
        if(mapList.size()==1){
            ret = true;
        }
        return ret;
    }

    public boolean fileIdexists(int umid,int fileId,int tableindex){
        boolean ret = false;
        List<Map<String,Object>> mapList = jdbcTemplate.queryForList("select * from itemfile"+tableindex+" where id=? and umid=?",fileId,umid);
        if(mapList.size()==1){
            ret = true;
        }
        return ret;
    }

    public boolean tipIdexists(int umid,int tipId,int tableindex){
        boolean ret = false;
        List<Map<String,Object>> mapList = jdbcTemplate.queryForList("select * from itemtips"+tableindex+" where id=? and umid=?",tipId,umid);
        if(mapList.size()==1){
            ret = true;
        }
        return ret;
    }

    public List<Map<String,Object>> getAllProjects(int umid,int tableindex,String sortFlag,int pageIndex,int pageNum){
        List<Map<String,Object>> mapList;
        if(pageIndex>0 && pageNum >0) {
            int beginPos = (pageIndex-1)*pageNum;
            if (sortFlag != null && sortFlag.equals("lasttime")) {
                mapList = jdbcTemplate.queryForList("select * from myproject" + tableindex + " where umid=? limit {},{} order by lasttime desc", umid,beginPos,pageNum);
            } else {
                mapList = jdbcTemplate.queryForList("select * from myproject" + tableindex + " where umid=? limit {},{} order by id desc", umid,beginPos,pageNum);
            }
        }else{
            if (sortFlag != null && sortFlag.equals("lasttime")) {
                mapList = jdbcTemplate.queryForList("select * from myproject" + tableindex + " where umid=? order by lasttime desc", umid);
            } else {
                mapList = jdbcTemplate.queryForList("select * from myproject" + tableindex + " where umid=? order by id desc", umid);
            }
        }
        return mapList;
    }

    public List<Map<String,Object>> getProjectItems(int umid,int tableindex,int projectId){
        return jdbcTemplate.queryForList("select * from projectitem" + tableindex +" where umid=? and projectid=?",umid,projectId);
    }

    public List<Map<String,Object>> getItemTips(int umid,int tableindex,int projectId,int itemId){
        return jdbcTemplate.queryForList("select id,tipcontent from itemtips" + tableindex +" where umid=? and projectid=? and itmeid=?",umid,projectId,itemId);
    }

    public List<Map<String,Object>> getItemFiles(int umid,int tableindex,int projectId,int itemId){
        return jdbcTemplate.queryForList("select id,filename,filesuffix,filepath from itemfile" + tableindex +" where umid=? and projectid=? and itmeid=?",umid,projectId,itemId);
    }

    public int delItemFile(int umid,int tableindex,int fileId){
        return jdbcTemplate.update("delete from itemfile"+tableindex+" where id=? and umid=?",fileId,umid);
    }

    public int delItemTip(int umid,int tableindex,int tipId){
        return jdbcTemplate.update("delete from itemtips"+tableindex+" where id=? and umid=?",tipId,umid);
    }

    public int delItemTips(int umid,int tableindex,int itemId){
        return jdbcTemplate.update("delete from itemtips"+tableindex+" where itemid=? and umid=?",itemId,umid);
    }

    public int delItemFiles(int umid,int tableindex,int itemId){
        return jdbcTemplate.update("delete from itemfile"+tableindex+" where itemid=? and umid=?",itemId,umid);
    }

    public int delItem(int umid,int tableindex,int itemId){
        return jdbcTemplate.update("delete from projectitem"+tableindex+" where id=? and umid=?",itemId,umid);
    }

}
