package com.allhis.service;

import com.allhis.bean.RetMessage;
import com.allhis.dao.MysqlDao;
import com.allhis.toolkit.GlobalTools;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import jdk.nashorn.internal.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ljy on 16/8/18.
 * ok
 */
@Service
public class MyhisService {

    private static Logger log = LoggerFactory.getLogger(MyhisService.class);

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private MysqlDao mysqlDao;
    @Autowired
    private ObjectMapper mapper;

    public RetMessage generalService(int umid,String functionName,String generalParam){
        RetMessage retMessage = new RetMessage();
        int tableindex = userInfoService.getTableIndex(umid);
        if(tableindex < 0){
            retMessage.setErrorCode("-100");
            retMessage.setErrorMessage("failed to get tableindex");
            log.error("get tableindex failed,umid:{}",umid);

        }else{

            Map<String,String> jsonmap = jsonString2Map(generalParam);

            if(paramcheck(functionName,jsonmap)) {
                //实际工作者
                retMessage = doService(umid, tableindex, functionName, jsonmap);
            }else{
                retMessage.setErrorCode("-101");
                retMessage.setErrorMessage("param error");
                log.error("param check failed,umid:{} functionName:{} generalParam:{}",umid,functionName,generalParam);
            }
        }

        return retMessage;
    }

    private RetMessage doService(int umid,int tableindex,String functionName,Map<String,String> parammap){
        RetMessage retMessage = new RetMessage();
        if(functionName.equals("createProject")){
            String projectName = parammap.get("projectName").toString();
            retMessage = createProject(projectName,umid,tableindex);
        }else if(functionName.equals("createItem")){
            int projectId = GlobalTools.convertStringToInt(parammap.get("projectId").toString());
            String itemName = parammap.get("itemName").toString();
            String itemContent = parammap.get("itemContent").toString();
            String begintime = parammap.get("begintime").toString();
            String endtime = parammap.get("endtime").toString();
            retMessage = createItem(umid,tableindex,projectId,itemName,itemContent,begintime,endtime);
        }else if(functionName.equals("getAllProjects")){
            String sortFlag = null;
            if(parammap.get("sortFlag")!=null){
                sortFlag = parammap.get("sortFlag").toString();
            }
            retMessage = getAllProjects(umid,tableindex,sortFlag);
        }
        return retMessage;
    }

    private RetMessage createProject(String projectname,int umid,int tableindex){
        RetMessage retMessage = new RetMessage();

        //创建项目
        String nt = String.valueOf(GlobalTools.getTimeBefore(0));
        int projectId = mysqlDao.addProject(tableindex, umid, projectname, nt);
        if(projectId >0){   //project项目创建成功
            retMessage.setErrorCode("0");
            retMessage.setErrorMessage("success");
            Map<String,String> map = new HashMap<>();
            map.put("projectId",projectId+"");
            retMessage.setRetContent(map2JsonString(map));
        }else{
            retMessage.setErrorCode("-10");
            retMessage.setErrorMessage("failed to create project");
            log.error("cteate project failed,umid:{} projectname:{} tableindex:{}",umid,projectname,tableindex);
        }
        return retMessage;
    }

    private RetMessage createItem(int umid,int tableindex,int projectid,String itemname,String itemcontent,String begintime,String endtime){
        RetMessage retMessage = new RetMessage();
        //校验projectid是否存在
        if(mysqlDao.projectIdexists(umid,projectid,tableindex)){
            int itemId = mysqlDao.addItem(tableindex,umid,projectid,itemname,itemcontent,begintime,endtime);
            if(itemId > 0){
                retMessage.setErrorCode("0");
                retMessage.setErrorMessage("success");
                Map<String,String> map = new HashMap<>();
                map.put("itmeId",itemId+"");
                retMessage.setRetContent(map2JsonString(map));
            }else{
                retMessage.setErrorCode("-12");
                retMessage.setErrorMessage("failed to create itme");
                log.error("cteate item failed,umid:{} projecid:{} itemname:{}",umid,projectid,itemname);
            }
        }else{
            retMessage.setErrorCode("-11");
            retMessage.setErrorMessage("project not exist");
            log.error("project not exist! umid:{} projectid:{}",umid,projectid);
        }

        return retMessage;
    }

    private RetMessage getAllProjects(int umid,int tableindex,String sortFlag){
        RetMessage retMessage = new RetMessage();
        List<Map<String,Object>> mapList = mysqlDao.getAllProjects(umid,tableindex,sortFlag);
        retMessage.setErrorCode("0");
        retMessage.setErrorMessage("success");
        retMessage.setRetContent(maplist2JsonString(mapList));
        return retMessage;
    }

    private boolean paramcheck(String functionName,Map<String,String> parammap){
        boolean ret = false;
        if(functionName.equals("createProject")){
            if(parammap!=null){
                //todo:
                ret = true;
            }
        }
        return ret;
    }

    private Map<String,String> jsonString2Map(String jsonString){
        Map<String,String> jsonmap = null;
        if(jsonString!=null && jsonString.trim().length()>0) {
            try {
                jsonmap = mapper.readValue(jsonString, new TypeReference<HashMap<String, String>>() {
                });
            } catch (IOException e) {
                log.error("exception catched in json string converting to map! {}", e.toString());
            }
        }else{
            log.error("invalid jsonstring! jsonstring:{}",jsonString);
        }
        return jsonmap;
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

    private String maplist2JsonString(List<Map<String,Object>> mapList){
        String ret = null;
        if(mapList!=null){
            try {
                ret = mapper.writeValueAsString(mapList);
            } catch (JsonProcessingException e) {
                log.error("exception catched in maplist converting to json string! {}",e.toString());
            }
        }else{
            log.error("invalid maplist!");
        }
        return ret;
    }
}
