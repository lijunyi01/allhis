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

    //客户端暂存<umid,List<socket>>, 一个umid可能在多个地方登录，因此对应于List<socket>
    public static HashMap<Integer, List<SocketIOClient>> umid_key_cache = new HashMap<Integer, List<SocketIOClient>>();
    //用户客户端关系暂存<sessionId,umid> ,sessionid 是唯一的，每个sessionid对应一个umid;多个不同的sessionid可能对应同一个umid(同一用户在多个地方登录)
    public static HashMap<UUID,Integer> sessionid_key_cache = new HashMap<UUID,Integer>();
    //用户对应的app server 的address的暂存
    public static HashMap<Integer,String> user_address = new HashMap<Integer,String>();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

    }
}