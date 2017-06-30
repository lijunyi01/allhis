package com.allhis.listener;

import com.allhis.websocketapi.Application;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DisconnectListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
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

        //<sessionId,umid>
        if(Application.sessionid_key_cache.containsKey(sessionId)){
            umid = Application.sessionid_key_cache.get(sessionId);
            Application.sessionid_key_cache.remove(sessionId);
        }

        if(umid > 0){
            //<umid,List<socket>>
            if(Application.umid_key_cache.containsKey(umid)){
                List<SocketIOClient> socketIOClientList = Application.umid_key_cache.get(umid);
                if(socketIOClientList.contains(socketIOClient)){
                    socketIOClientList.remove(socketIOClient);
                }

                if(socketIOClientList.size()>0){
                    Application.umid_key_cache.put(umid,socketIOClientList);
                }else{
                    Application.umid_key_cache.remove(umid);
                }
            }
        }else{
            log.error("get umid error from Application.sessionid_key_cache! umid:{} sessionId:{}", umid, sessionId);
        }

        log.debug("clientip:{} umid:{} disconnected.",clientIp,umid);
    }
}
