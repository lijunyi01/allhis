package com.allhis.myhisapp;

import com.allhis.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

//import com.allhis.service.AuthService;

//import org.apache.mina.util.CopyOnWriteMap;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackageClasses = App.class)
public class Application {

    private static Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class);

    }
}
