package com.allhis.service;

import com.allhis.bean.RetMessage;
import com.allhis.websocketapi.Application;
import com.corundumstudio.socketio.SocketIOClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by ljy on 16/8/30.
 * ok
 */
@Service
public class UserService {

    private static Logger log = LoggerFactory.getLogger(UserService.class);

    @Value("${system.authcenterurl}")
    private String authcenterurl;

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
        RestTemplate restTemplate = new RestTemplate();
        String authurl = authcenterurl + "umid="+umid+"&token="+token;
        retMessage = restTemplate.getForObject(authurl, RetMessage.class);
        if(retMessage.getErrorCode().equals("0")){
            ret = true;
        }else{
            log.info("umid:{} token check failed! token:{}",umid,token);
        }
        return ret;
    }
}
