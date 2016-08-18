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
    public int addToProject(final int tableindex,final int umid,final String projectname, final String createtime){
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

}
