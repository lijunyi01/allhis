package com.allhis.conf;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
//import com.allhis.bean.ClientReqBean;
//import com.allhis.listener.*;
//import com.allhis.service.MyhisService;
//import com.allhis.service.UserService;
//import com.corundumstudio.socketio.SocketIOServer;
//import com.corundumstudio.socketio.Transport;
import com.fasterxml.jackson.databind.ObjectMapper;
//import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
//import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.InputStream;
//import com.corundumstudio.socketio.Configuration;

/*
从spring3.0开始，Spring将JavaConfig整合到核心模块，普通的POJO只需要标注@Configuration注解，就可以成为spring配置类，
并通过在方法上标注@Bean注解的方式注入bean。
*/
@Configuration
class ApplicationConfig {

//    private static Logger log = LoggerFactory.getLogger(ApplicationConfig.class);

//    @Value("${system.port}")
//    private int port;
//    @Value("${system.sslport}")
//    private int sslport;

    @Bean
    public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
        PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
        //ClassPathResource 的根目录在本项目是指resources目录
        //ppc.setLocation(new ClassPathResource("/test.properties"));
        ppc.setLocation(new FileSystemResource("/appconf/ah_websocketapi/app.properties"));
        return ppc;
    }

    @Bean
    public static JoranConfigurator readLogbackPropertyFile(){
        File logbackFile = new File("/appconf/ah_websocketapi/logback.xml");
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(lc);
        lc.reset();
        try {
            configurator.doConfigure(logbackFile);
        }
        catch (JoranException e) {
            e.printStackTrace(System.err);
            System.exit(-1);
        }
        return configurator;

    }

//    @Bean
//    MyAuthorizationListener myAuthorizationListener(){
//         return new MyAuthorizationListener();
//    }

//    @Bean
//    com.corundumstudio.socketio.Configuration configuration(){
//        com.corundumstudio.socketio.Configuration configuration = new com.corundumstudio.socketio.Configuration();
////        configuration.setHostname("192.168.8.100");
//        configuration.setPort(port);
//        //configuration.setPingInterval(30000);
//        //configuration.setPingTimeout(20000);
////        configuration.setTransports(Transport.WEBSOCKET);
//        configuration.setAuthorizationListener(myAuthorizationListener());
//        return configuration;
//    }

//    @Bean
//    com.corundumstudio.socketio.Configuration configurationSSL(){
//        com.corundumstudio.socketio.Configuration configuration = new com.corundumstudio.socketio.Configuration();
////        configuration.setHostname("192.168.8.100");
//        configuration.setPort(sslport);
//        InputStream stream = null;
//        try {
//            stream = new FileInputStream("/appconf/ah_websocketapi/certificates/keystore.jks");
//        } catch (FileNotFoundException e) {
//            log.error("exception:{}",e.toString());
//        }
//        if(stream!=null) {
//            configuration.setKeyStore(stream);
//            configuration.setKeyStorePassword("123456");
//        }
////        configuration.setTransports(Transport.WEBSOCKET);
//        configuration.setAuthorizationListener(myAuthorizationListener());
//        return configuration;
//    }

//    @Bean(initMethod = "start")
//    @Lazy(false)
//    SocketIOServer socketIOServer(){
//        SocketIOServer socketIOServer = new SocketIOServer(configuration());
//        socketIOServer.addConnectListener(myConncetListener());
//        socketIOServer.addDisconnectListener(myDisconncetListener());
//        //对应于客户端的自定义事件“Request”，形如：_sioClient.emit("Reguest", JSON.stringify(RegObject));
//        socketIOServer.addEventListener("Request", ClientReqBean.class,myRequestListener());
//        //对应于客户端的默认事件“message”，形如：_sioClient.send("hello");
//        socketIOServer.addEventListener("message",String.class,myMessageListener());
//        return socketIOServer;
//    }
//
//    @Bean(initMethod = "start")
//    @Lazy(false)
//    SocketIOServer socketIOServerSSL(){
//        SocketIOServer socketIOServer = new SocketIOServer(configurationSSL());
//        socketIOServer.addConnectListener(myConncetListener());
//        socketIOServer.addDisconnectListener(myDisconncetListener());
//        //对应于客户端的自定义事件“Regist”，形如：_sioClient.emit("Request", JSON.stringify(RegObject));
//        socketIOServer.addEventListener("Request", ClientReqBean.class, myRequestListener());
//        //对应于客户端的默认事件“message”，形如：_sioClient.send("hello");
//        socketIOServer.addEventListener("message",String.class,myMessageListener());
//        return socketIOServer;
//    }

//    @Bean
//    MyConncetListener myConncetListener(){
//        return new MyConncetListener();
//    }

//    @Bean
//    MyDisconncetListener myDisconncetListener(){
//        return new MyDisconncetListener();
//    }
//
//    @Bean
//    MyRequestListener myRequestListener(){
//        return new MyRequestListener();
//    }

//    @Bean
//    MyMessageListener myMessageListener(){
//        return new MyMessageListener();
//    }

    @Bean
    RestTemplate restTemplate(){
        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpComponentsClientHttpRequestFactory.setReadTimeout(10*1000);
        httpComponentsClientHttpRequestFactory.setConnectTimeout(6*1000);
        return new RestTemplate(httpComponentsClientHttpRequestFactory);
    }

    @Bean
    ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}