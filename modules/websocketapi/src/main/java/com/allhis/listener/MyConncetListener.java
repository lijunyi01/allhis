package com.allhis.listener;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;

/**
 * Created by ljy on 16/2/25.
 * ok
 */
public class MyConncetListener implements ConnectListener {

    @Override
    public void onConnect(SocketIOClient socketIOClient) {
        //System.out.print(socketIOClient.getSessionId());
        String a = socketIOClient.getRemoteAddress().toString();
        //System.out.print(socketIOClient.getRemoteAddress().toString());
        System.out.print("in MyConncetListener:"+a+"\n");
    }
}
