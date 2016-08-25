package com.allhis.listener;

import com.allhis.websocketapi.Application;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DisconnectListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyDisconncetListener implements DisconnectListener {

    private static Logger log = LoggerFactory.getLogger(MyDisconncetListener.class);

    @Override
    public void onDisconnect(SocketIOClient socketIOClient) {

        String clientIp = socketIOClient.getRemoteAddress().toString();

        int umid = Application.user_client_cache.get(socketIOClient.getSessionId());
        if(umid > 0){
            if (Application.client_cache.get(umid).getSessionId().equals(socketIOClient.getSessionId())){ //如果当前缓存中的client就是断开的client
                //清除当前信息
                Application.client_cache.remove(umid);
            }
            //清除关系缓存中的信息
            Application.user_client_cache.remove(socketIOClient.getSessionId());
        }

        log.debug("clientip:{} disconnected. client socket is:{}",clientIp,socketIOClient.toString());
    }
}
