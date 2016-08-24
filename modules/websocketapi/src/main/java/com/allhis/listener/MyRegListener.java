package com.allhis.listener;


import com.allhis.bean.RegBean;
import com.allhis.websocketapi.Application;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;

public class MyRegListener implements DataListener<RegBean> {

    @Override
    public void onData(SocketIOClient socketIOClient, RegBean data, AckRequest ackSender) throws Exception {
        //userid即为前台传到后台的regId
        int umid = data.getUmid();
        String token = data.getToken();
        if (umid > 0 && token != null) {
            //todo：验证token

            //在客户端缓存中清除原有属于该userid的客户端
            Application.client_cache.remove(umid);
            //增加新的客户端
            Application.client_cache.put(umid, socketIOClient);
            //向用户与客户端存储中存入新的client
            Application.user_client_cache.put(socketIOClient.getSessionId(), umid);
            socketIOClient.sendEvent("regOK", data);
//            System.out.println("注册成功！key=" + userid);
        }

    }
}
