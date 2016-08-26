package com.allhis.listener;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ljy on 16/2/25.
 * ok
 */
public class MyConncetListener implements ConnectListener {

    private static Logger log = LoggerFactory.getLogger(MyConncetListener.class);

    @Override
    public void onConnect(SocketIOClient socketIOClient) {

        String clientIp = null;
        if(socketIOClient.getRemoteAddress()!=null) {
            clientIp = socketIOClient.getRemoteAddress().toString();
        }
        log.debug("in MyConncetListener:{}",clientIp);
    }
}
