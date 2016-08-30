package com.allhis.websocketapi;

import com.allhis.App;
import com.corundumstudio.socketio.SocketIOClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
@ComponentScan(basePackageClasses = App.class)
public class Application {

    //客户端暂存
    public static HashMap<Integer, SocketIOClient> client_cache = new HashMap<Integer, SocketIOClient>();
    //用户客户端关系暂存
    public static HashMap<UUID,Integer> user_client_cache = new HashMap<UUID,Integer>();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

    }
}