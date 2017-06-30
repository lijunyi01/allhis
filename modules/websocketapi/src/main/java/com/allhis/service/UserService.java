package com.allhis.service;

import com.allhis.bean.RetMessage;
import com.allhis.toolkit.GlobalTools;
import com.allhis.websocketapi.Application;
import com.corundumstudio.socketio.SocketIOClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ljy on 16/8/30.
 * ok
 */
@Service
public class UserService {

    private static Logger log = LoggerFactory.getLogger(UserService.class);

    @Value("${authcenter.authtokenurl}")
    private String authtokenurl;
    @Value("${authcenter.getuserinfourl}")
    private String getuserinfourl;
    @Value("#{'${myhisapp.param}'.split('\\<\\[CDATA\\]\\>')}")
    private List<String> paramStrList;

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    @Autowired
    public UserService(RestTemplate restTemplate, ObjectMapper mapper) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
    }

    public void recordUmidAndSocket(int umid,SocketIOClient socketIOClient){

        //<sessionId,umid>
        Application.sessionid_key_cache.put(socketIOClient.getSessionId(), umid);

        List<SocketIOClient> socketIOClientList;
        //<umid,List<socket>>
        if(Application.umid_key_cache.containsKey(umid)){
            socketIOClientList = Application.umid_key_cache.get(umid);
            if(!socketIOClientList.contains(socketIOClient)){
                socketIOClientList.add(socketIOClient);
                Application.umid_key_cache.put(umid, socketIOClientList);
            }
        }else{
            socketIOClientList = new ArrayList<>();
            socketIOClientList.add(socketIOClient);
            Application.umid_key_cache.put(umid, socketIOClientList);
        }

    }

//    public boolean authToken(int umid,String token,SocketIOClient socketIOClient){
//        boolean ret = false;
//
//        int umidInCache = -1;
//        if(Application.user_client_cache.containsKey(socketIOClient.getSessionId())){
//            umidInCache = Application.user_client_cache.get(socketIOClient.getSessionId());
//        }
//        SocketIOClient socketIOClientInCache = null;
//        if(Application.client_cache.containsKey(umid)){
//            socketIOClientInCache = Application.client_cache.get(umid);
//        }
//
//        if(socketIOClient == socketIOClientInCache && umid == umidInCache){   //已经校验过且保存在cache
//            ret = true;
//        }else{
//            if(tokenCheck(umid,token)){
//                //增加新的客户端
//                Application.client_cache.put(umid, socketIOClient);
//                //向用户与客户端存储中存入新的client
//                Application.user_client_cache.put(socketIOClient.getSessionId(), umid);
//                ret = true;
//            }
//        }
//
//        return ret;
//    }

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

    private int getDbIndex(int umid){
        int ret = -1;
        RetMessage retMessage = null;
        String url = getuserinfourl + "umid={umid}";
        log.debug("do query url:{}",url);
        try {
            retMessage = restTemplate.getForObject(url, RetMessage.class, umid);
        }catch (Exception e){
            log.error("catch exception when query url:{}",toString());
        }
        if(retMessage!= null && retMessage.getErrorCode().equals("0")) {
            String retContent = retMessage.getRetContent();
            Map<String,String> map = jsonString2Map(retContent);
            if(map.get("dbindex")!=null){
                ret = GlobalTools.convertStringToInt(map.get("dbindex").toString());
            }
        }
        if(ret<0){
            log.error("get dbindex failed! umid:{} dbindex:{}",umid,ret);
        }
        return ret;
    }

    public String getAppAddress(int umid){
        String ret = null;
        if(Application.user_address.containsKey(umid)){
            ret = Application.user_address.get(umid);
        }else {
            int dbindex = getDbIndex(umid);
            if (dbindex > -1) {
                int dbindexInProfile = -1;
                for (String str : paramStrList) {
                    Map<String, String> map = GlobalTools.parseInput(str, "\\<\\[CDATA1\\]\\>");
                    if (map.get("dbindex") != null) {
                        dbindexInProfile = GlobalTools.convertStringToInt(map.get("dbindex").toString());
                        if (dbindex == dbindexInProfile) {
                            if (map.get("address") != null) {
                                ret = map.get("address").toString();
                                Application.user_address.put(umid,ret);
                                break;
                            }
                        }
                    }
                }
            }
            if(ret == null){
                log.error("get app address failed! umid:{} dbindex:{}",umid,dbindex);
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
            jsonmap = new HashMap<>();
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
