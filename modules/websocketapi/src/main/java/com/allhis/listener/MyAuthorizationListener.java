package com.allhis.listener;

import com.allhis.service.UserService;
import com.allhis.toolkit.GlobalTools;
import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.HandshakeData;
//import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

//import javax.xml.ws.spi.http.HttpHandler;

/**
 * Created by ljy on 16/3/3.
 * ok
 */
@Component
public class MyAuthorizationListener implements AuthorizationListener {

    private static Logger log = LoggerFactory.getLogger(MyAuthorizationListener.class);

    private final UserService userService;

    @Autowired
    public MyAuthorizationListener(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isAuthorized(HandshakeData handshakeData) {
        boolean ret = false;
        String clientAddress;

//        HttpHeaders httpHeaders1 = handshakeData.getHttpHeaders();
//        Set<String> sets = httpHeaders1.names();
//        for(String str: sets){
//            String value = httpHeaders1.get(str);
//            log.debug("set: {} -- {}",str,value);
//        }

        HttpHeaders httpHeaders = handshakeData.getHttpHeaders();
        clientAddress = httpHeaders.get("X-Real-IP");
//        log.debug("ip from httpheader x-real-ip:{}",clientAddress);

        if(clientAddress==null && handshakeData.getAddress()!=null){
            clientAddress = handshakeData.getAddress().toString();
        }
        String umidS = handshakeData.getSingleUrlParam("umid");
        String token = handshakeData.getSingleUrlParam("token");

        int umid = GlobalTools.convertStringToInt(umidS);
        if(umid>-1 && token !=null) {
            //特殊处理，用于eatapple客户端项目（react－redux）测试
            if(umid==10000000){
                ret = true;
            }else {
                if (userService.tokenCheck(umid, token)) {
                    ret = true;
                }
            }
        }
        log.debug("client:{} with param umid:{} token:{} try to connect into,result:{}",clientAddress,umid,token,ret);
        return ret;
    }
}
