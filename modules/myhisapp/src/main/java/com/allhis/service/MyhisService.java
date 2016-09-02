package com.allhis.service;

import com.allhis.bean.ItemBean;
import com.allhis.bean.RetMessage;
import com.allhis.dao.MysqlDao;
import com.allhis.toolkit.GlobalTools;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
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
        if(tableindex < 0) {
            retMessage.setErrorCode("-1000");
            retMessage.setErrorMessage("failed to get tableindex");
            log.error("get tableindex failed,umid:{}", umid);

        }else if(functionName == null || generalParam == null){
            retMessage.setErrorCode("-1002");
            retMessage.setErrorMessage("param error");
            log.error("param error! umid:{} functionName:{} generalParam:{}",umid,functionName,generalParam);

        }else{

            Map<String,String> jsonmap = jsonString2Map(generalParam);

            if(paramcheck(functionName,jsonmap)) {
                //实际工作者
                retMessage = doService(umid, tableindex, functionName, jsonmap);
            }else{
                retMessage.setErrorCode("-1001");
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
            int pageIndex = -1;
            int pageNum = -1;
            if(parammap.get("sortFlag")!=null){
                sortFlag = parammap.get("sortFlag").toString();
            }
            if(parammap.get("pageIndex")!=null){
                pageIndex = GlobalTools.convertStringToInt(parammap.get("pageIndex").toString());
            }
            if(parammap.get("pageNum")!=null){
                pageNum = GlobalTools.convertStringToInt(parammap.get("pageNum").toString());
            }
            retMessage = getAllProjects(umid,tableindex,sortFlag,pageIndex,pageNum);

        }else if(functionName.equals("addItemTip")){
            int projectId = GlobalTools.convertStringToInt(parammap.get("projectId").toString());
            int itemId = GlobalTools.convertStringToInt(parammap.get("itemId").toString());
            String tipContent = parammap.get("tipContent").toString();
            retMessage = addItemTip(umid, tableindex, projectId, itemId, tipContent);

        }else if(functionName.equals("addItemFile")){
            int projectId = GlobalTools.convertStringToInt(parammap.get("projectId").toString());
            int itemId = GlobalTools.convertStringToInt(parammap.get("itemId").toString());
            String fileName = parammap.get("fileName").toString();
            String filePath = parammap.get("filePath").toString();
            retMessage = addItemFile(umid, tableindex, projectId, itemId, fileName, filePath);

        }else if(functionName.equals("getProjectItems")){
            int projectId = GlobalTools.convertStringToInt(parammap.get("projectId").toString());
            retMessage = getProjectItems(umid,tableindex,projectId);
        }
        return retMessage;
    }

    private RetMessage getProjectItems(int umid,int tableindex,int projectId){
        RetMessage retMessage = new RetMessage();
        List<Map<String,Object>> mapList = mysqlDao.getProjectItems(umid,tableindex,projectId);
        List<ItemBean> itemBeanList = new ArrayList<>();
        for(Map<String,Object> map: mapList){
            int itemId = GlobalTools.convertStringToInt(map.get("id").toString());
            String itemName = map.get("itemname").toString();
            String itemContent = null;
            if(map.get("itemcontent")!=null){
                itemContent= map.get("itemcontent").toString();
            }
            String starttime = map.get("starttime").toString();
            String endtime = null;
            if(map.get("endtime")!=null){
                endtime = map.get("endtime").toString();
            }

            if(itemId > 0){
                ItemBean itemBean = new ItemBean();
                itemBean.setItemId(itemId);
                itemBean.setItemName(itemName);
                if(itemContent!=null){
                    itemBean.setItemContent(itemContent);
                }
                itemBean.setStartTime(starttime);
                if(endtime!=null){
                    itemBean.setEndTime(endtime);
                }
                List<Map<String,Object>> tipMapList = mysqlDao.getItemTips(umid,tableindex,projectId,itemId);
                List<Map<String,Object>> fileMapList = mysqlDao.getItemFiles(umid,tableindex,projectId,itemId);
                itemBean.setItemTipMapList(tipMapList);
                itemBean.setItemFileMapList(fileMapList);

                itemBeanList.add(itemBean);

            }else{
                log.error("itemid error! itemid:{}", itemId);
            }
        }

        //将List<ItemBean> itemBeanList 序列化
        String serializedItemBeanList = beanlist2JsonString(itemBeanList);
        if(serializedItemBeanList != null) {
            retMessage.setErrorCode("0");
            retMessage.setErrorMessage("success");
            retMessage.setRetContent(serializedItemBeanList);
        }else{
            retMessage.setErrorCode("-1050");
            retMessage.setErrorMessage("failed to serialize itemBeanList!");
            log.error("failed to serialize itemBeanList! umid:{} projectid:{}",umid,projectId);
        }

        return retMessage;
    }

    private RetMessage addItemFile(int umid,int tableindex,int projectId,int itemId,String fileName,String filePath){
        RetMessage retMessage = new RetMessage();
        //校验itemId是否存在
        if(mysqlDao.itemIdexists(umid, projectId, itemId,tableindex)) {
            String fileSuffix = "";
            if(fileName.indexOf(".") > -1){
                fileSuffix = fileName.substring(fileName.indexOf(".")+1,fileName.length());
            }
            int fileId = mysqlDao.addItemFile(tableindex, umid, projectId, itemId, fileName,fileSuffix,filePath);
            if (fileId > 0) {
                retMessage.setErrorCode("0");
                retMessage.setErrorMessage("success");
                Map<String, Object> map = new HashMap<>();
                map.put("fileId", fileId);
                retMessage.setRetContent(map2JsonString(map));
            } else {
                retMessage.setErrorCode("-1040");
                retMessage.setErrorMessage("failed to add item file");
                log.error("add item file failed,umid:{} projectid:{} itmeid:{}", umid, projectId, itemId);
            }
        }else{
            retMessage.setErrorCode("-1041");
            retMessage.setErrorMessage("item not exist");
            log.error("itme not exist! umid:{} projectid:{} itemid:{}", umid, projectId, itemId);
        }
        return retMessage;
    }

    private RetMessage addItemTip(int umid,int tableindex,int projectId,int itemId,String tipContent){
        RetMessage retMessage = new RetMessage();
        //校验itemId是否存在
        if(mysqlDao.itemIdexists(umid, projectId, itemId,tableindex)) {
            int tipId = mysqlDao.addItemTip(tableindex, umid, projectId, itemId, tipContent);
            if (tipId > 0) {
                retMessage.setErrorCode("0");
                retMessage.setErrorMessage("success");
                Map<String, Object> map = new HashMap<>();
                map.put("tipId", tipId);
                retMessage.setRetContent(map2JsonString(map));
            } else {
                retMessage.setErrorCode("-1030");
                retMessage.setErrorMessage("failed to add item tip");
                log.error("add item tip failed,umid:{} projectid:{} itmeid:{}", umid, projectId, itemId);
            }
        }else{
            retMessage.setErrorCode("-1031");
            retMessage.setErrorMessage("item not exist");
            log.error("itme not exist! umid:{} projectid:{} itemid:{}", umid, projectId, itemId);
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
            Map<String,Object> map = new HashMap<>();
            map.put("projectId",projectId);
            retMessage.setRetContent(map2JsonString(map));
        }else{
            retMessage.setErrorCode("-1010");
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
                Map<String,Object> map = new HashMap<>();
                map.put("itmeId",itemId);
                retMessage.setRetContent(map2JsonString(map));
            }else{
                retMessage.setErrorCode("-1020");
                retMessage.setErrorMessage("failed to create itme");
                log.error("cteate item failed,umid:{} projecid:{} itemname:{}",umid,projectid,itemname);
            }
        }else{
            retMessage.setErrorCode("-1021");
            retMessage.setErrorMessage("project not exist");
            log.error("project not exist! umid:{} projectid:{}",umid,projectid);
        }

        return retMessage;
    }

    private RetMessage getAllProjects(int umid,int tableindex,String sortFlag,int pageIndex,int pageNum){
        RetMessage retMessage = new RetMessage();
        List<Map<String,Object>> mapList = mysqlDao.getAllProjects(umid,tableindex,sortFlag,pageIndex,pageNum);
        retMessage.setErrorCode("0");
        retMessage.setErrorMessage("success");
        retMessage.setRetContent(maplist2JsonString(mapList));
        return retMessage;
    }

    //校验参数
    private boolean paramcheck(String functionName,Map<String,String> parammap){
        boolean ret = true;
        //之前已经初步判断过，functionName和parammap 都不为null
        if(functionName.equals("createProject")){
            if(parammap.get("projectName")==null) {
                ret = false;
            }
        }else if(functionName.equals("createItem")){
            if(parammap.get("projectId")==null || parammap.get("itemName")==null || parammap.get("itemContent")==null || parammap.get("begintime")==null || parammap.get("endtime")==null){
                ret = false;
            }
        }else if(functionName.equals("addItemTip")){
            if(parammap.get("projectId")==null || parammap.get("itemId")==null || parammap.get("tipContent")==null){
                ret = false;
            }
        }else if(functionName.equals("addItemFile")){
            if(parammap.get("projectId")==null || parammap.get("itemId")==null || parammap.get("fileName")==null || parammap.get("filePath")==null){
                ret = false;
            }
        }else if(functionName.equals("getProjectItems")){
            if(parammap.get("projectId")==null){
                ret = false;
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

    private String map2JsonString(Map<String,Object> map){
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

    private String beanlist2JsonString(List beanList){
        String ret = null;
        if(beanList!=null){
            try {
                ret = mapper.writeValueAsString(beanList);
            } catch (JsonProcessingException e) {
                log.error("exception catched in beanlist converting to json string! {}",e.toString());
            }
        }else{
            log.error("invalid beanlist!");
        }

        return ret;
    }
}
