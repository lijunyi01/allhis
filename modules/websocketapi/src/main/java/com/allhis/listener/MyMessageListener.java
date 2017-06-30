package com.allhis.listener;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MyMessageListener implements DataListener<String> {

    private static Logger log = LoggerFactory.getLogger(MyMessageListener.class);

    @Override
    public void onData(SocketIOClient socketIOClient, String message, AckRequest ackSender) throws Exception {

        log.info("received message:{}",message);
    }
}
