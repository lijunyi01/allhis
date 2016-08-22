package com.allhis.handler;

import com.allhis.service.AuthService;
import com.allhis.toolkit.GlobalTools;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.net.InetSocketAddress;
import java.util.Map;

public class TcpHandler extends IoHandlerAdapter {

	@Value("${system.timeout}")
	private int sessionIdleTimeout;

	@Autowired
	private AuthService authService;

	private static Logger logger = LoggerFactory.getLogger(TcpHandler.class);

	//连接建立不发欢迎消息，简化客户端的处理
	@Override
	public void sessionOpened(IoSession session) throws Exception{
		//setIdleTime()单位为秒
		session.getConfig().setBothIdleTime(sessionIdleTimeout);
//        session.getConfig().setReaderIdleTime(sessionIdleTimeout);
		//设置接收缓冲区大小
		session.getConfig().setReadBufferSize(2048);
		//调试，输出欢迎信息
//		if(sessionIdleTimeout==3600) {
//			session.write("+ok 220 Welcome to Auth Center.");
//		}
		super.sessionOpened(session);
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {

		String ip="";
		if(session.getAttribute("clientip")!=null){
			ip = session.getAttribute("clientip").toString();
		}else {
			try {
				ip = ((InetSocketAddress) session.getRemoteAddress()).getAddress().getHostAddress();
			} catch (Exception e) {
				logger.error("get ip address error");
			}
			if(!ip.equals("")){
				session.setAttribute("clientip",ip);
			}
		}

		String input = message.toString().trim();
		//记录下收到的指令,x心跳指令忽略
		if(!input.equals("HELO")) {
			logger.debug("client: [" + ip + "] " + input);
		}

		String answers = dealInput(input, ip, session);

		if(answers.equals("QUIT")) {
			session.close(true);
		}else if(answers.equals("550 ")){
			//心跳指令回应
			session.write(answers);
		}else{
			//answers是空就不回应
			if(!answers.equals("")) {
				session.write(answers);
				logger.debug("server: [" + ip + "] " + answers);
			}
		}
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.sessionClosed(session);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		// TODO Auto-generated method stub
		// session.write("idle timeout,session will be closed!");
		logger.info("session [" +session.toString()+ "] idle timeout");
		session.close(true);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {

		if(session!= null && cause.getMessage()!=null) {
			logger.error("exception catched! session:" + session.toString() + " and cause:" + cause.getMessage().toString());
		}else if(session!=null){
			logger.error("exception catched! session:" + session.toString() +" and cause.getMessage() get null!");
		}else if(cause.getMessage()!=null){
			logger.error("exception catched! session is null! and cause is:"  + cause.getMessage().toString());
		}else{
			logger.error("exception catched! session and cause.getMessage() are both null!");
		}

	}

	private String dealInput(String input,String clientIp,IoSession session) {
		String ret = "";

		String inputUpperCase = input.toUpperCase();

		if (inputUpperCase.startsWith("AUTH")) {
			//auth umid=ljy@allcomchina.com<[CDATA]>ip=202.96.209.133<[CDATA]>pass=password<[CDATA]>platform=web
			String umid;
			String ip;
			String pass;
			String platform;
			String a = input.substring("AUTH".length(),input.length());
			a = a.trim();
			if (a !=null && !a.isEmpty()){
				Map<String,String> map = GlobalTools.parseInput(a,"\\<\\[CDATA\\]\\>");
				umid = map.get("umid");
				ip = map.get("ip");
				pass = map.get("pass");
				platform = map.get("platform");
				if(umid ==null || ip ==null || pass==null ||platform ==null){
					ret = "-err PARAM ERROR";
				}else{
					int iret = authService.getErrorCode(umid,pass,ip,platform);
					String errorMessage = authService.getErrorMessage(iret);
					if(iret ==0){
						ret = "+ok "+iret+" "+errorMessage;
					}else{
						ret = "+err "+iret+" "+errorMessage;
					}
				}
			}else{
				ret = "-err PARAM ERROR";
			}
		} else if (inputUpperCase.startsWith("QUIT")) {
			ret = "QUIT";
		} else if(inputUpperCase.startsWith("HELO")){
			ret = "550 ";
		} else{
			ret = "QUIT";
		}
		return ret;
	}



}
