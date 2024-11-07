package org.example;

import org.example.common.Invocation;
import org.example.protocol.HttpClient;
import org.example.proxy.ProxyFactory;

import java.io.IOException;

/**
 * 消费者，客户端
 */
public class Consumer {
    public static void main(String[] args) throws IOException {
        // 使用代理对象获得接口的代理
        HelloService helloService = ProxyFactory.getProxy(HelloService.class);
        String result = helloService.sayHello("sea");
        System.out.println(result);
    }
}
