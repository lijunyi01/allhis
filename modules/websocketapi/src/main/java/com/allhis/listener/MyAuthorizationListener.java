package com.allhis.listener;

import com.allhis.service.UserService;
import com.allhis.toolkit.GlobalTools;
import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.HandshakeData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by ljy on 16/3/3.
 * ok
 */
public class MyAuthorizationListener implements AuthorizationListener {

    private static Logger log = LoggerFactory.getLogger(MyAuthorizationListener.class);

    @Autowired
    private UserService userService;

    @Override
    public boolean isAuthorized(HandshakeData handshakeData) {
        boolean ret = false;
        String clientAddress = null;
        if(handshakeData.getAddress()!=null){
            clientAddress = handshakeData.getAddress().toString();
        }
        String umidS = handshakeData.getSingleUrlParam("umid");
        String token = handshakeData.getSingleUrlParam("token");

        int umid = GlobalTools.convertStringToInt(umidS);
        if(umid>-1 && token !=null) {
            if (userService.tokenCheck(umid, token)) {
                ret = true;
            }
        }
        log.debug("client:{} with param umid:{} token:{} try to connect into,result:{}",clientAddress,umid,token,ret);
        return ret;
    }
}
