package com.allhis.listener;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.HandshakeData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ljy on 16/3/3.
 * ok
 */
public class MyAuthorizationListener implements AuthorizationListener {

    private static Logger log = LoggerFactory.getLogger(MyAuthorizationListener.class);

    @Override
    public boolean isAuthorized(HandshakeData handshakeData) {
        boolean ret = true;
//        String a = handshakeData.getAddress().toString();
//        String b = handshakeData.getUrl();
//        //此处可以验证连接等
//        log.debug("in MyAuthorizationListener:{}",a);
//        log.debug("in MyAuthorizationListener:{}",b);
        return ret;
    }
}
