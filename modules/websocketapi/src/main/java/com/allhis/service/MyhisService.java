package com.allhis.service;

import com.allhis.bean.RetMessage;
import com.allhis.bean.ServerAckBean;
import com.corundumstudio.socketio.SocketIOClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by ljy on 16/8/30.
 * ok
 */
@Service
public class MyhisService {
    private static Logger log = LoggerFactory.getLogger(MyhisService.class);

    public ServerAckBean doRequest(int umid,String functionName,String generalParam,String appAddress){
        ServerAckBean serverAckBean = new ServerAckBean();
        RetMessage retMessage;
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://"+ appAddress + "/appinterface/gen?umid="+umid+"&functionName="+functionName+"&generalParam="+generalParam;
        log.debug("doRequest:"+ url);
        retMessage = restTemplate.getForObject(url, RetMessage.class);
        if(retMessage!=null){
            serverAckBean.setErrorCode(retMessage.getErrorCode());
            serverAckBean.setErrorMessage(retMessage.getErrorMessage());
            serverAckBean.setGeneralAckContent(retMessage.getRetContent());
        }
        return serverAckBean;
    }
}
