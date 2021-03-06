package com.allhis.service;

import com.allhis.bean.RetMessage;
import com.allhis.bean.ServerAckBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


/**
 * Created by ljy on 16/8/30.
 * ok
 */
@Service
public class MyhisService {
    private static Logger log = LoggerFactory.getLogger(MyhisService.class);

    private final RestTemplate restTemplate;

    @Autowired
    public MyhisService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ServerAckBean doRequest(int umid,String functionName,String generalParam,String appAddress){
        ServerAckBean serverAckBean = new ServerAckBean();
        RetMessage retMessage = null;
        String url = "http://{appAddress}/appinterface/gen?umid={umid}&functionName={functionName}&generalParam={generalParam}";
        log.debug("doRequest:"+ url);
        try {
            retMessage = restTemplate.getForObject(url, RetMessage.class, appAddress, umid, functionName, generalParam);
        }catch (Exception e){
            log.error("catch exception in query url:{}",e.toString());
        }
        if(retMessage!=null){
            serverAckBean.setErrorCode(retMessage.getErrorCode());
            serverAckBean.setErrorMessage(retMessage.getErrorMessage());
            serverAckBean.setGeneralAckContent(retMessage.getRetContent());
        }
        return serverAckBean;
    }
}
