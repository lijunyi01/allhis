package com.allhis.authcenter;

import com.allhis.App;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${system.embeddedtomcatport}")
    private int tomcatport;

//    private static Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class);

    }

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container){
        container.setPort(tomcatport);
    }
}
