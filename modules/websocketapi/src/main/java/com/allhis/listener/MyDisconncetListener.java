package com.allhis.listener;

import com.allhis.websocketapi.Application;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DisconnectListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class MyDisconncetListener implements DisconnectListener {

    private static Logger log = LoggerFactory.getLogger(MyDisconncetListener.class);

    @Override
    public void onDisconnect(SocketIOClient socketIOClient) {

        int umid = -1;
        String clientIp = null;
        if(socketIOClient.getRemoteAddress()!=null) {
            clientIp = socketIOClient.getRemoteAddress().toString();
        }
        UUID sessionId = socketIOClient.getSessionId();
        if(Application.user_client_cache.get(sessionId)!=null){
            umid = Application.user_client_cache.get(sessionId);
        }

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
