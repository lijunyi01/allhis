package com.allhis.authcenter;

import com.allhis.App;
import com.allhis.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashMap;
import java.util.Map;

//import org.apache.mina.util.CopyOnWriteMap;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackageClasses = App.class)
public class Application implements EmbeddedServletContainerCustomizer{

    private static Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class);

    }

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container){
        container.setPort(8081);
    }
}
