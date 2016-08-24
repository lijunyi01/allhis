package com.allhis.conf;

import com.allhis.App;
import com.allhis.bean.RegBean;
import com.allhis.listener.MyAuthorizationListener;
import com.allhis.listener.MyConncetListener;
import com.allhis.listener.MyDisconncetListener;
import com.allhis.listener.MyRegListener;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
//import com.corundumstudio.socketio.Configuration;

/*
从spring3.0开始，Spring将JavaConfig整合到核心模块，普通的POJO只需要标注@Configuration注解，就可以成为spring配置类，
并通过在方法上标注@Bean注解的方式注入bean。
*/
@Configuration
class ApplicationConfig {

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
        socketIOServer.addEventListener("Regist",RegBean.class,myRegListener());
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

}