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

            int umid = data.getUmid();
            String serial = data.getSerial();
            String functionName = data.getFunctionName();

            int umidInCache = -1;
            if(Application.sessionid_key_cache.containsKey(socketIOClient.getSessionId())){
                umidInCache = Application.sessionid_key_cache.get(socketIOClient.getSessionId());
            }

            ServerAckBean serverAckBean = new ServerAckBean();
            if(umid>0 && umid==umidInCache) {

                if (serial != null && functionName != null) {
                    String appAddress = userService.getAppAddress(umid);
                    if (appAddress != null) {
                        serverAckBean = myhisService.doRequest(umid, functionName, data.getGeneralParam(), appAddress);
                    } else {
                        log.error("get appaddress error! umid:{}", umid);
                    }
                    serverAckBean.setSerial(serial);
                } else {
                    log.error("param error in MyRequestListener! umid:{} serial:{} functionName:{}", umid, serial, functionName);
                }

            }else{
                log.error("umid check failed! need auth again!");
                serverAckBean.setErrorCode("-100");
                serverAckBean.setErrorMessage("umid check failed! need auth again!");
                if(serial!=null) {
                    serverAckBean.setSerial(serial);
                }
            }
            socketIOClient.sendEvent("AckReq", serverAckBean);
        }else{
            log.error("data null!");
        }

    }


}
