package com.allhis.service;

import com.allhis.bean.RetMessage;
import com.allhis.toolkit.GlobalTools;
import com.allhis.websocketapi.Application;
import com.corundumstudio.socketio.SocketIOClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    @Autowired
    private RestTemplate restTemplate;

    public boolean authToken(int umid,String token,SocketIOClient socketIOClient){
        boolean ret = false;

        int umidInCache = -1;
        if(Application.user_client_cache.containsKey(socketIOClient.getSessionId())){
            umidInCache = Application.user_client_cache.get(socketIOClient.getSessionId());
        }
        SocketIOClient socketIOClientInCache = null;
        if(Application.client_cache.containsKey(umid)){
            socketIOClientInCache = Application.client_cache.get(umid);
        }

        if(socketIOClient == socketIOClientInCache && umid == umidInCache){   //已经校验过且保存在cache
            ret = true;
        }else{
            if(tokenCheck(umid,token)){
                //增加新的客户端
                Application.client_cache.put(umid, socketIOClient);
                //向用户与客户端存储中存入新的client
                Application.user_client_cache.put(socketIOClient.getSessionId(), umid);
                ret = true;
            }
        }

        return ret;
    }

    //向authcenter进行token校验
    private boolean tokenCheck(int umid,String token){
        boolean ret = false;
        RetMessage retMessage;
        String authurl = authtokenurl + "umid="+umid+"&token="+token;
        retMessage = restTemplate.getForObject(authurl, RetMessage.class);
        if(retMessage.getErrorCode().equals("0")){
            ret = true;
        }else{
            log.info("umid:{} token check failed! token:{}",umid,token);
        }
        return ret;
    }

    private int getDbIndex(int umid){
        int ret = -1;
        RetMessage retMessage;
        String url = getuserinfourl + "umid=" + umid;
        retMessage = restTemplate.getForObject(url, RetMessage.class);
        if(retMessage.getErrorCode().equals("0")) {
            String retContent = retMessage.getRetContent();
            Map<String,String> map = GlobalTools.parseInput(retContent);
            if(map.get("dbindex")!=null){
                ret = GlobalTools.convertStringToInt(map.get("dbindex").toString());
            }
        }
        if(ret<1){
            log.error("get dbindex failed! umid:{}",umid);
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
}
