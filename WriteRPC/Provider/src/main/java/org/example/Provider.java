package org.example;


import org.example.common.URL;
import org.example.protocol.HttpServer;
import org.example.register.LocalRegister;
import org.example.register.MapRemoteRegister;

/**
 * 服务端，启动需要被调用的服务
 */
public class Provider {
    public static void main(String[] args) {

        //注册
        LocalRegister.regist(HelloService.class.getName(),"1.0", HelloServiceImpl.class);

        //注册中心注册 服务注册
        URL url = new URL("localhost", 8080);
        MapRemoteRegister.regist(HelloService.class.getName(), url);

        HttpServer httpServer = new HttpServer();
        httpServer.start(url.getHostname(), url.getPort());
    }
}
