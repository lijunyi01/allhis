package com.allhis.conf;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import com.allhis.App;
import com.allhis.bean.RegBean;
import com.allhis.listener.*;
import com.corundumstudio.socketio.SocketIOServer;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
//import com.corundumstudio.socketio.Configuration;

/*
从spring3.0开始，Spring将JavaConfig整合到核心模块，普通的POJO只需要标注@Configuration注解，就可以成为spring配置类，
并通过在方法上标注@Bean注解的方式注入bean。
*/
@Configuration
class ApplicationConfig {

    @Bean
    public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
        PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
        //ClassPathResource 的根目录在本项目是指resources目录
        //ppc.setLocation(new ClassPathResource("/test.properties"));
        ppc.setLocation(new FileSystemResource("/appconf/websocketapi/app.properties"));
        return ppc;
    }

    @Bean
    public static JoranConfigurator readLogbackPropertyFile(){
        File logbackFile = new File("/appconf/websocketapi/logback.xml");
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

    @Bean
    MyAuthorizationListener myAuthorizationListener(){
        MyAuthorizationListener myAuthorizationListener = new MyAuthorizationListener();
        return myAuthorizationListener;
    }

    @Bean
    com.corundumstudio.socketio.Configuration configuration(){
        com.corundumstudio.socketio.Configuration configuration = new com.corundumstudio.socketio.Configuration();
//        configuration.setHostname("192.168.8.100");
        configuration.setPort(8090);
        //configuration.setPingInterval(30000);
        //configuration.setPingTimeout(20000);
        //configuration.setTransports(Transport.WEBSOCKET);
        configuration.setAuthorizationListener(myAuthorizationListener());
        return configuration;
    }

    @Bean(initMethod = "start")
    @Lazy(false)
    SocketIOServer socketIOServer(){
        SocketIOServer socketIOServer = new SocketIOServer(configuration());
        socketIOServer.addConnectListener(myConncetListener());
        socketIOServer.addDisconnectListener(myDisconncetListener());
        //对应于客户端的自定义事件“Regist”，形如：_sioClient.emit("Regist", JSON.stringify(RegObject));
        socketIOServer.addEventListener("Regist",RegBean.class,myRegListener());
        //对应于客户端的默认事件“message”，形如：_sioClient.send("hello");
        socketIOServer.addEventListener("message",String.class,myMessageListener());
        return socketIOServer;
    }

    @Bean
    MyConncetListener myConncetListener(){
        return new MyConncetListener();
    }

    @Bean
    MyDisconncetListener myDisconncetListener(){
        return new MyDisconncetListener();
    }

    @Bean
    MyRegListener myRegListener(){
        return new MyRegListener();
    }

    @Bean
    MyMessageListener myMessageListener(){
        return new MyMessageListener();
    }

}