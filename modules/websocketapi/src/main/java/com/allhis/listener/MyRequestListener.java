package com.allhis.listener;


import com.allhis.bean.ClientReqBean;
import com.allhis.bean.ServerAckBean;
import com.allhis.service.MyhisService;
import com.allhis.service.UserService;
import com.allhis.websocketapi.Application;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class MyRequestListener implements DataListener<ClientReqBean> {

    private static Logger log = LoggerFactory.getLogger(MyRequestListener.class);

    @Autowired
    private UserService userService;
    @Autowired
    private MyhisService myhisService;

    @Override
    public void onData(SocketIOClient socketIOClient, ClientReqBean data, AckRequest ackSender) throws Exception {

        if(data != null) {
            //userid即为前台传到后台的regId
            int umid = data.getUmid();
            String token = data.getToken();
            String serial = data.getSerial();
            String functionName = data.getFunctionName();
            ServerAckBean serverAckBean = new ServerAckBean();
            if (umid > 0 && token != null && serial != null && functionName !=null) {
                //验证token
                if (userService.authToken(umid, token, socketIOClient)) {
                    String appAddress = userService.getAppAddress(umid);
                    if(appAddress != null){
                        serverAckBean = myhisService.doRequest(umid,functionName,data.getGeneralParam(),appAddress);
                    }else{
                        log.error("get appaddress error! umid:{}",umid);
                    }

                } else {
                    serverAckBean.setErrorCode("-2");
                    serverAckBean.setErrorMessage("auth fail");
                    serverAckBean.setSerial(serial);
                }

            } else {
                log.error("param error in MyRequestListener! umid:{} token:{} serial:{}", umid, token, serial);
            }
            serverAckBean.setSerial(serial);
            socketIOClient.sendEvent("AckReq", serverAckBean);
        }else{
            log.error("data null!");
        }

    }


}
