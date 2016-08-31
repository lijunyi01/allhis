package com.allhis.service;

import com.allhis.bean.AuthResp;
import com.allhis.toolkit.GlobalTools;
import org.apache.mina.util.CopyOnWriteMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
    @Autowired
    private RestTemplate restTemplate;

    public int getTableIndex(int umid){
        int ret = -1;
        if(tableindexMap.containsKey(umid)){
            ret = tableindexMap.get(umid);
        }else{
            //调authcenter的接口查用户的tableindex,查成功后同时写入tableindexMap
            int tableindex = getTableIndexFromApi(umid);
            if(tableindex >0 ){
                ret = tableindex;
                tableindexMap.put(umid,tableindex);
            }
        }
        return ret;
    }


    private int getTableIndexFromApi(int umid){
        int ret = -1;
        //调authcenter的接口查用户的tableindex
        AuthResp authResp = restTemplate.getForObject(url+umid, AuthResp.class);
        if(authResp.getAuthCode()==0){   //查询成功
            String content = authResp.getContent();
            if(content!=null && content.trim().length()>0) {
                Map<String, String> map = GlobalTools.parseInput(content, "=");
                if(map.get("tableindex")!=null){
                    int tableindex = GlobalTools.convertStringToInt(map.get("tableindex").toString());
                    if(tableindex>0){
                        ret = tableindex;
                    }else{
                        log.error("tableindex is not valid,tableindex:{}",map.get("tableindex").toString());
                    }
                }else {
                    log.error("authResp.getContent error! content:{}",content);
                }
            }else{
                log.error("content is empty!");
            }
        }else{
            log.error("failed to get tableindex from api! umid:{} errorcode:{} errormsg:{}",umid,authResp.getAuthCode(),authResp.getAuthMessage());
        }
        return ret;
    }
}


