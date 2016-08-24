package com.allhis.listener;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.HandshakeData;

/**
 * Created by ljy on 16/3/3.
 * ok
 */
public class MyAuthorizationListener implements AuthorizationListener {
    @Override
    public boolean isAuthorized(HandshakeData handshakeData) {
        boolean ret = true;
        String a = handshakeData.getAddress().toString();
        String b = handshakeData.getUrl();
        //此处可以验证连接等
        System.out.print("in MyAuthorizationListener:"+a+"\n");
        System.out.print("in MyAuthorizationListener:"+b+"\n");
        return ret;
    }
}
