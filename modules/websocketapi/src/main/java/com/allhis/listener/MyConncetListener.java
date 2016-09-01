package com.allhis.listener;

import com.allhis.service.UserService;
import com.allhis.toolkit.GlobalTools;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * Created by ljy on 16/2/25.
 * ok
 */
public class MyConncetListener implements ConnectListener {

    private static Logger log = LoggerFactory.getLogger(MyConncetListener.class);

    @Autowired
    private UserService userService;

    @Override
    public void onConnect(SocketIOClient socketIOClient) {

        String clientAddress = null;
        if(socketIOClient.getRemoteAddress()!=null) {
            clientAddress = socketIOClient.getRemoteAddress().toString();
        }

        HandshakeData handshakeData = socketIOClient.getHandshakeData();
        String umidS = handshakeData.getSingleUrlParam("umid");
        int umid = GlobalTools.convertStringToInt(umidS);
        if(umid>0 && socketIOClient !=null) {
            userService.recordUmidAndSocket(umid, socketIOClient);
            log.debug("umid:{} connected from:{}", umid, clientAddress);
        }else{
            log.error("get param in onConnect error! clientaddress:{} umid:{}",clientAddress,umid);
        }
    }
}
