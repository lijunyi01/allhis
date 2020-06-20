package com.allhis.service;

import com.allhis.bean.RetMessage;
import com.allhis.toolkit.GlobalTools;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.mina.util.CopyOnWriteMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ljy on 16/8/18.
 * ok
 */
@Service
public class UserInfoService {

    private static Logger log = LoggerFactory.getLogger(UserInfoService.class);

    public static Map<Integer,Integer> tableindexMap = new CopyOnWriteMap<Integer,Integer>();

    @Value("${system.gettableindexurl}")
    private String url;
    @Value("${system.authtokenurl}")
    private String authtokenurl;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper mapper;

    public int getTableIndex(int umid){
        int ret = -1;
        if(tableindexMap.containsKey(umid)){
            ret = tableindexMap.get(umid);
        }else{
            //调authcenter的接口查用户的tableindex,查成功后同时写入tableindexMap
            int tableindex = getTableIndexFromApi(umid);
            if(tableindex > -1 ){
                ret = tableindex;
                tableindexMap.put(umid,tableindex);
            }
        }
        return ret;
    }


    private int getTableIndexFromApi(int umid){
        int ret = -1;
        //调authcenter的接口查用户的tableindex
        RetMessage retMessage = restTemplate.getForObject(url+"umid="+umid, RetMessage.class);
        if(retMessage.getErrorCode().equals("0")){   //查询成功
            String content = retMessage.getRetContent();
            if(content!=null && content.trim().length()>0) {
                Map<String, String> map = jsonString2Map(content);
                if(map.get("tableindex")!=null){
                    int tableindex = GlobalTools.convertStringToInt(map.get("tableindex").toString());
                    if(tableindex>-1){
                        ret = tableindex;
                    }else{
                        log.error("tableindex is not valid,tableindex:{}",map.get("tableindex").toString());
                    }
                }else {
                    log.error("getContent error! content:{}",content);
                }
            }else{
                log.error("content is empty!");
            }
        }else{
            log.error("failed to get tableindex from api! umid:{} errorcode:{} errormsg:{}",umid,retMessage.getErrorCode(),retMessage.getErrorMessage());
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

    //向authcenter进行token校验
    public boolean tokenCheck(int umid,String token){
        boolean ret = false;
        RetMessage retMessage;
        String authurl = authtokenurl + "umid={umid}&token={token}";
        retMessage = restTemplate.getForObject(authurl, RetMessage.class,umid,token);
        if(retMessage.getErrorCode().equals("0")){
            ret = true;
        }else{
            log.info("umid:{} token check failed! token:{}",umid,token);
        }
        return ret;
    }
}


