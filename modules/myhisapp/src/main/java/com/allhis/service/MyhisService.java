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
import org.springframework.transaction.annotation.Transactional;

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
        switch (functionName) {
            case "createProject": {
                String projectName = parammap.get("projectName");
                String projectDes = parammap.get("projectDes");
                retMessage = createProject(projectName, umid, tableindex, projectDes);

                break;
            }
            case "createItem": {
                int projectId = GlobalTools.convertStringToInt(parammap.get("projectId"));
                String itemType = parammap.get("type");
                String itemName = parammap.get("itemName");
                String itemDes = parammap.get("itemDes");
                String startYear = parammap.get("startYear");
                String startYearNDFlag = parammap.get("startYearNDFlag");
                String endYear = parammap.get("endYear");
                String endYearNDFlag = parammap.get("endYearNDFlag");
                String startYear_Des = parammap.get("startYear_des");
                String endYear_Des = parammap.get("endYear_Des");
                String startTime = parammap.get("startTime");
                String endTime = parammap.get("endTime");
                retMessage = createItem(umid, tableindex,projectId,itemType,itemName, itemDes,startYear,startYearNDFlag,endYear,endYearNDFlag,startYear_Des,endYear_Des,startTime,endTime);

                break;
            }
            case "getAllProjects":
                String sortFlag = null;
                int pageIndex = -1;
                int pageNum = -1;
                if (parammap.get("sortFlag") != null) {
                    sortFlag = parammap.get("sortFlag");
                }
                if (parammap.get("pageIndex") != null) {
                    pageIndex = GlobalTools.convertStringToInt(parammap.get("pageIndex"));
                }
                if (parammap.get("pageNum") != null) {
                    pageNum = GlobalTools.convertStringToInt(parammap.get("pageNum"));
                }
                retMessage = getAllProjects(umid, tableindex, sortFlag, pageIndex, pageNum);

                break;
            case "addItemTip": {
                int projectId = GlobalTools.convertStringToInt(parammap.get("projectId"));
                int itemId = GlobalTools.convertStringToInt(parammap.get("itemId"));
                String tipContent = parammap.get("tipContent");
                retMessage = addItemTip(umid, tableindex, projectId, itemId, tipContent);

                break;
            }
            case "addItemFile": {
                int projectId = GlobalTools.convertStringToInt(parammap.get("projectId"));
                int itemId = GlobalTools.convertStringToInt(parammap.get("itemId"));
                String fileName = parammap.get("fileName");
                String filePath = parammap.get("filePath");
                retMessage = addItemFile(umid, tableindex, projectId, itemId, fileName, filePath);

                break;
            }
            case "getProjectItems": {
                int projectId = GlobalTools.convertStringToInt(parammap.get("projectId"));
                retMessage = getProjectItems(umid, tableindex, projectId);

                break;
            }
            case "delItemFile": {
                int projectId = GlobalTools.convertStringToInt(parammap.get("projectId"));
                int fileId = GlobalTools.convertStringToInt(parammap.get("fileId"));
                retMessage = delItemFile(umid, tableindex, fileId, projectId);

                break;
            }
            case "delItemTip": {
                int projectId = GlobalTools.convertStringToInt(parammap.get("projectId"));
                int tipId = GlobalTools.convertStringToInt(parammap.get("tipId"));
                retMessage = delItemTip(umid, tableindex, tipId, projectId);

                break;
            }
            case "delItem": {
                int itemId = GlobalTools.convertStringToInt(parammap.get("itemId"));
                int projectId = GlobalTools.convertStringToInt(parammap.get("projectId"));
                retMessage = delItem(umid, tableindex, itemId, projectId);

                break;
            }
            case "delProject": {
                int projectId = GlobalTools.convertStringToInt(parammap.get("projectId"));
                retMessage = delProject(umid, tableindex, projectId);

                break;
            }
            case "modifyProject": {
                int projectId = GlobalTools.convertStringToInt(parammap.get("projectId"));
                String projectName = parammap.get("projectName");
                String projectDes = parammap.get("projectDes");
                retMessage = modifyProject(umid, tableindex, projectId, projectName, projectDes);

                break;
            }
            case "modifyItem": {
                int projectId = GlobalTools.convertStringToInt(parammap.get("projectId"));
                int itemId = GlobalTools.convertStringToInt(parammap.get("itemId"));
                String itemName = parammap.get("itemName");
                String itemContent = parammap.get("itemContent");
                String begintime = parammap.get("begintime");
                String endtime = parammap.get("endtime");
                retMessage = modifyItem(umid, tableindex, projectId, itemId, itemName, itemContent, begintime, endtime);

                break;
            }
            case "modifyItemTip": {
                int projectId = GlobalTools.convertStringToInt(parammap.get("projectId"));
                int tipId = GlobalTools.convertStringToInt(parammap.get("tipId"));
                String tipContent = parammap.get("tipContent");
                retMessage = modifyItemTip(umid, tableindex, projectId, tipId, tipContent);
                break;
            }
        }
        return retMessage;
    }

    private RetMessage modifyItemTip(int umid,int tableindex,int projectId,int tipId,String tipContent){
        RetMessage retMessage = new RetMessage();
        if(mysqlDao.tipIdexists(umid,tipId,tableindex)){
            if(mysqlDao.modifyItemTip(tableindex,umid,tipId,tipContent) > 0){
                retMessage.setErrorCode("0");
                retMessage.setErrorMessage("success");
                //更新project表的最后修改时间
                renewLastTime(tableindex,projectId);
            }else{
                retMessage.setErrorCode("-1120");
                retMessage.setErrorMessage("modify itemtip failed");
                log.error("modify itemtip failed! umid:{} tipid:{}", umid, tipId);
            }
        }else{
            retMessage.setErrorCode("-1071");
            retMessage.setErrorMessage("tipid not exist");
            log.error("tipid not exist! umid:{} tipid:{}", umid, tipId);
        }
        return retMessage;
    }

    private RetMessage modifyItem(int umid,int tableindex,int projectId,int itemId,String itemName,String itemContent,String begintime,String endtime){
        RetMessage retMessage = new RetMessage();
        if(mysqlDao.itemIdexists(umid,itemId,tableindex)){
            if(mysqlDao.modifyItem(tableindex,umid,itemId,itemName,itemContent,begintime,endtime)>0){
                retMessage.setErrorCode("0");
                retMessage.setErrorMessage("success");
                //更新project表的最后修改时间
                renewLastTime(tableindex,projectId);
            }else{
                retMessage.setErrorCode("-1110");
                retMessage.setErrorMessage("modify item failed");
                log.error("modify item failed! umid:{} itemid:{}", umid, itemId);
            }
        }else{
            retMessage.setErrorCode("-1081");
            retMessage.setErrorMessage("itemid not exist");
            log.error("itemid not exist! umid:{} itemid:{}", umid, itemId);
        }
        return retMessage;
    }

    private RetMessage modifyProject(int umid,int tableindex,int projectId,String projectName,String projectDes){
        RetMessage retMessage = new RetMessage();
        //校验projectId是否存在
        if(mysqlDao.projectIdexists(umid, projectId, tableindex)) {
            String nt = String.valueOf(GlobalTools.getTimeBefore(0));
            if(mysqlDao.modifyProject(umid,tableindex,projectId,projectName,projectDes,nt)>0){
                retMessage.setErrorCode("0");
                retMessage.setErrorMessage("success");
            }else{
                retMessage.setErrorCode("-1100");
                retMessage.setErrorMessage("modify project failed");
                log.error("modify project failed! umid:{} projectid:{}", umid, projectId);
            }
        }else{
            retMessage.setErrorCode("-1091");
            retMessage.setErrorMessage("projectid not exist");
            log.error("projectid not exist! umid:{} projectid:{}", umid, projectId);
        }
        return retMessage;
    }

    private RetMessage delProject(int umid,int tableindex,int projectId){
        RetMessage retMessage = new RetMessage();
        //校验projectId是否存在
        if(mysqlDao.projectIdexists(umid, projectId, tableindex)) {
            deleteProject(umid, tableindex, projectId);
            if(!mysqlDao.projectIdexists(umid, projectId, tableindex)){
                retMessage.setErrorCode("0");
                retMessage.setErrorMessage("success");
            }else{
                retMessage.setErrorCode("-1090");
                retMessage.setErrorMessage("del project failed!");
            }
        }else{
            retMessage.setErrorCode("-1091");
            retMessage.setErrorMessage("projectid not exist");
            log.error("projectid not exist! umid:{} projectid:{}", umid, projectId);
        }
        return retMessage;
    }

    @Transactional
    private void deleteProject(int umid,int tableindex,int projectId){
        mysqlDao.delItemFiles(umid,tableindex,projectId,1);
        mysqlDao.delItemTips(umid,tableindex,projectId,1);
        mysqlDao.delItemsByProjectId(umid, tableindex, projectId);
        mysqlDao.delProject(umid,tableindex,projectId);
    }

    private RetMessage delItem(int umid,int tableindex,int itemId,int projectId){
        RetMessage retMessage = new RetMessage();
        //校验itemId是否存在
        if(mysqlDao.itemIdexists(umid, itemId, tableindex)) {
            deleteItem(umid,tableindex,itemId);
            if(!mysqlDao.itemIdexists(umid, itemId, tableindex)){
                retMessage.setErrorCode("0");
                retMessage.setErrorMessage("success");
                //更新project表的最后修改时间
                renewLastTime(tableindex,projectId);
            }else{
                retMessage.setErrorCode("-1080");
                retMessage.setErrorMessage("del item failed!");
            }
        }else{
            retMessage.setErrorCode("-1081");
            retMessage.setErrorMessage("itemid not exist");
            log.error("itemid not exist! umid:{} itemid:{}", umid, itemId);
        }
        return retMessage;
    }

    @Transactional
    private void deleteItem(int umid,int tableindex,int itemId){
        mysqlDao.delItemFiles(umid,tableindex,itemId,2);
        mysqlDao.delItemTips(umid,tableindex,itemId,2);
        mysqlDao.delItem(umid,tableindex,itemId);
    }

    private RetMessage delItemTip(int umid,int tableindex,int tipId,int projectId){
        RetMessage retMessage = new RetMessage();
        //校验itemId是否存在
        if(mysqlDao.tipIdexists(umid, tipId, tableindex)) {
            if(mysqlDao.delItemTip(umid, tableindex, tipId) > 0){
                retMessage.setErrorCode("0");
                retMessage.setErrorMessage("success");
                //更新project表的最后修改时间
                renewLastTime(tableindex,projectId);
            }else{
                retMessage.setErrorCode("-1070");
                retMessage.setErrorMessage("del tip failed!");
            }
        }else{
            retMessage.setErrorCode("-1071");
            retMessage.setErrorMessage("tipid not exist");
            log.error("tipid not exist! umid:{} tipid:{}", umid, tipId);
        }
        return retMessage;
    }

    private RetMessage delItemFile(int umid,int tableindex,int fileId,int projectId){
        RetMessage retMessage = new RetMessage();
        //校验itemId是否存在
        if(mysqlDao.fileIdexists(umid, fileId, tableindex)) {
            if(mysqlDao.delItemFile(umid,tableindex,fileId) > 0){
                retMessage.setErrorCode("0");
                retMessage.setErrorMessage("success");
                //更新project表的最后修改时间
                renewLastTime(tableindex,projectId);
            }else{
                retMessage.setErrorCode("-1060");
                retMessage.setErrorMessage("del file failed!");
            }
        }else{
            retMessage.setErrorCode("-1061");
            retMessage.setErrorMessage("fileid not exist");
            log.error("fileid not exist! umid:{} fileid:{}", umid, fileId);
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
            String itemContent = "";
            if(map.get("itemcontent")!=null){
                itemContent= map.get("itemcontent").toString();
            }
            int startYear = GlobalTools.convertStringToInt(map.get("startyear").toString());
            int endYear = GlobalTools.convertStringToInt(map.get("endyear").toString());
            int itemType = GlobalTools.convertStringToInt(map.get("itemtype").toString());
            int startYearNDFlag = GlobalTools.convertStringToInt(map.get("startyearndflag").toString());
            int endYearNDFlag = GlobalTools.convertStringToInt(map.get("endyearndflag").toString());
            String startYearDes = "";
            if(map.get("startyear_des")!=null){
                startYearDes = map.get("startyear_des").toString();
            }
            String endYearDes = "";
            if(map.get("endyear_des")!=null){
                endYearDes = map.get("endyear_des").toString();
            }
            String starttime = "";
            if(map.get("starttime")!=null){
                starttime = map.get("starttime").toString();
            }
            String endtime = "";
            if(map.get("endtime")!=null){
                endtime = map.get("endtime").toString();
            }

            if(itemId > 0){
                ItemBean itemBean = new ItemBean();
                itemBean.setItemId(itemId);
                itemBean.setItemName(itemName);
                itemBean.setItemContent(itemContent);
                itemBean.setItemType(itemType);
                itemBean.setStartYear(startYear);
                itemBean.setStartYearNDFlag(startYearNDFlag);
                itemBean.setEndYear(endYear);
                itemBean.setEndYearNDFlag(endYearNDFlag);
                itemBean.setStartYearDes(startYearDes);
                itemBean.setEndYearDes(endYearDes);
                itemBean.setStartTime(starttime);
                itemBean.setEndTime(endtime);
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
        if(mysqlDao.itemIdexists(umid,itemId,tableindex)) {
            String fileSuffix = "";
            if(fileName.contains(".")){
                fileSuffix = fileName.substring(fileName.indexOf(".")+1,fileName.length());
            }
            int fileId = mysqlDao.addItemFile(tableindex, umid, projectId, itemId, fileName,fileSuffix,filePath);
            if (fileId > 0) {
                retMessage.setErrorCode("0");
                retMessage.setErrorMessage("success");
                Map<String, Object> map = new HashMap<>();
                map.put("fileId", fileId);
                retMessage.setRetContent(map2JsonString(map));
                //更新project表的最后修改时间
                renewLastTime(tableindex,projectId);
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
        if(mysqlDao.itemIdexists(umid,itemId,tableindex)) {
            int tipId = mysqlDao.addItemTip(tableindex, umid, projectId, itemId, tipContent);
            if (tipId > 0) {
                retMessage.setErrorCode("0");
                retMessage.setErrorMessage("success");
                Map<String, Object> map = new HashMap<>();
                map.put("tipId", tipId);
                retMessage.setRetContent(map2JsonString(map));
                //更新project表的最后修改时间
                renewLastTime(tableindex,projectId);
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

    private RetMessage createProject(String projectname,int umid,int tableindex,String projectdes){
        RetMessage retMessage = new RetMessage();

        if(mysqlDao.projectNameExists(umid,projectname,tableindex)){
            retMessage.setErrorCode("-1010");
            retMessage.setErrorMessage("projectname already exists");
            log.error("cteate project failed for projectname already exists,umid:{} projectname:{} tableindex:{}", umid, projectname, tableindex);
        }else {
            //创建项目
            String nt = String.valueOf(GlobalTools.getTimeBefore(0));
            int projectId = mysqlDao.addProject(tableindex, umid, projectname, projectdes, nt);
            if (projectId > 0) {   //project项目创建成功
                retMessage.setErrorCode("0");
                retMessage.setErrorMessage("success");
                Map<String, Object> map = new HashMap<>();
                map.put("projectId", projectId);
                retMessage.setRetContent(map2JsonString(map));
            } else {
                retMessage.setErrorCode("-1011");
                retMessage.setErrorMessage("failed to create project");
                log.error("cteate project failed,umid:{} projectname:{} tableindex:{}", umid, projectname, tableindex);
            }
        }
        return retMessage;
    }

    private RetMessage createItem(int umid,int tableindex,int projectid,String itemType,String itemName,String itemDes,String startYear,String startYearNDFlag,String endYear,String endYearNDFlag,String startYearDes,String endYearDes,String startTime,String endTime){
        RetMessage retMessage = new RetMessage();
        //校验projectid是否存在
        if(mysqlDao.projectIdexists(umid,projectid,tableindex)){
            int startYearNum =0;
            int endYearNum =0;
            int iType = GlobalTools.convertStringToInt(itemType);
            int istartNDFlag = GlobalTools.convertStringToInt(startYearNDFlag);
            int iendNDFlag = GlobalTools.convertStringToInt(endYearNDFlag);
            int paramErrorFlag = 1;
            if(iType == 1){   //点时间，公元纪年
                startYearNum = GlobalTools.convertStringToInt2(startYear);
                //为便于前台处理，点时间的endYearNum设置成等于startYearNum
                endYearNum = startYearNum;
                if(startYearNum == 0){
                    paramErrorFlag = -1;
                    log.error("start year[{}] is not valid! umid is:{}",startYear,umid);
                }
            }else if(iType == 2){  //点时间,年号纪年
                //根据年号查公元年份
                //startYearNum = getYearNumByNH(startYearDes);
                //为便于前台处理，点时间的endYearNum设置成等于startYearNum
                endYearNum = startYearNum;
                if(startYearNum == 0){
                    paramErrorFlag = -2;
                    log.error("start nian hao [{}] is not valid! umid is:{}",startYearDes,umid);
                }
            }else if(iType == 3){  //段时间，公元纪年
                startYearNum = GlobalTools.convertStringToInt2(startYear);
                endYearNum = GlobalTools.convertStringToInt2(endYear);
                if(startYearNum == 0 || endYearNum == 0){
                    paramErrorFlag = -1;
                    log.error("start year[{}] or end year[{}] is not valid! umid is:{}",startYear,endYear,umid);
                }
            }else if(iType == 4){  //段时间，年号纪年
                //根据年号查公元年份
                //startYearNum = getYearNumByNH(startYearDes);
                //endYearNum = getYearNumByNH(endYearDes);
                if(startYearNum == 0 || endYearNum == 0){
                    paramErrorFlag = -2;
                    log.error("start nian hao [{}] or end nian hao [{}] is not valid! umid is:{}",startYearDes,endYearDes,umid);
                }
            }else{
                paramErrorFlag = -3;
                log.error("itemtype[{}] is not valid! umid is:{}",iType,umid);
            }

            if(paramErrorFlag == 1) {
                int itemId = mysqlDao.addItem(tableindex, umid, projectid, itemName, itemDes, startYearNum, endYearNum, startYearDes, endYearDes, startTime, endTime, iType,istartNDFlag,iendNDFlag);
                if (itemId > 0) {
                    retMessage.setErrorCode("0");
                    retMessage.setErrorMessage("success");
                    Map<String, Object> map = new HashMap<>();
                    map.put("itmeId", itemId);
                    retMessage.setRetContent(map2JsonString(map));
                    //更新project表的最后修改时间
                    renewLastTime(tableindex, projectid);

                    log.info("umid:{} success to create item[{}] for project[{}]",umid,itemId,projectid);
                } else {
                    retMessage.setErrorCode("-1020");
                    retMessage.setErrorMessage("failed to create itme");
                    log.error("cteate item failed,umid:{} projecid:{} itemname:{}", umid, projectid, itemName);
                }
            }else{
                retMessage.setErrorCode("-1022");
                if(paramErrorFlag == -1){
                    retMessage.setErrorMessage("year is not valid");
                }else if(paramErrorFlag == -2){
                    retMessage.setErrorMessage("get year by nianhao failed");
                }else if(paramErrorFlag == -3){
                    retMessage.setErrorMessage("item type is not valid");
                }
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
        switch (functionName) {
            case "createProject":
                if (parammap.get("projectName") == null || parammap.get("projectDes") == null) {
                    ret = false;
                }
                break;
            case "createItem":
                if (parammap.get("projectId") == null || parammap.get("itemName") == null || parammap.get("itemDes") == null) {
                    ret = false;
                }
                break;
            case "addItemTip":
                if (parammap.get("projectId") == null || parammap.get("itemId") == null || parammap.get("tipContent") == null) {
                    ret = false;
                }
                break;
            case "addItemFile":
                if (parammap.get("projectId") == null || parammap.get("itemId") == null || parammap.get("fileName") == null || parammap.get("filePath") == null) {
                    ret = false;
                }
                break;
            case "getProjectItems":
                if (parammap.get("projectId") == null) {
                    ret = false;
                }
                break;
            case "delItemFile":
                if (parammap.get("projectId") == null || parammap.get("fileId") == null) {
                    ret = false;
                }
                break;
            case "delItemTip":
                if (parammap.get("projectId") == null || parammap.get("tipId") == null) {
                    ret = false;
                }
                break;
            case "delItem":
                if (parammap.get("projectId") == null || parammap.get("itemId") == null) {
                    ret = false;
                }
                break;
            case "modifyProject":
                if (parammap.get("projectId") == null || parammap.get("projectName") == null || parammap.get("projectDes") == null) {
                    ret = false;
                }
                break;
            case "modifyItem":
                if (parammap.get("projectId") == null || parammap.get("itemId") == null || parammap.get("itemName") == null || parammap.get("itemContent") == null || parammap.get("begintime") == null || parammap.get("endtime") == null) {
                    ret = false;
                }
                break;
            case "modifyItemTip":
                if (parammap.get("projectId") == null || parammap.get("itemId") == null || parammap.get("tipId") == null || parammap.get("tipContent") == null) {
                    ret = false;
                }
                break;
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

    private void renewLastTime(int tableindex,int projectId){
        String nt = String.valueOf(GlobalTools.getTimeBefore(0));
        mysqlDao.setProjectLastTime(tableindex,projectId,nt);
    }
}
