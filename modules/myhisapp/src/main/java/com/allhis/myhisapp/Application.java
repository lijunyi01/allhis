package com.allhis.myhisapp;

import com.allhis.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

//import com.allhis.service.AuthService;

//import org.apache.mina.util.CopyOnWriteMap;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackageClasses = App.class)
public class Application implements EmbeddedServletContainerCustomizer {

    private static Logger log = LoggerFactory.getLogger(Application.class);

    @Value("${system.embeddedtomcatport}")
    private int tomcatport;

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container){
        container.setPort(tomcatport);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class);

    }
}
