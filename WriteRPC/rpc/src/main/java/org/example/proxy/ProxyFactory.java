package org.example.proxy;

import org.example.common.Invocation;
import org.example.common.URL;
import org.example.loadbalance.LoadBalance;
import org.example.protocol.HttpClient;
import org.example.register.MapRemoteRegister;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * 代理工厂
 * 创建接口的代理并执行
 */
public class ProxyFactory {

    public static <T> T getProxy(Class interfaceClass) {
        // 用户配置

        Object proxyInstance = Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //设置一个服务mock，在未启动Provider时启动Consumer时输出文字，用于调试和测试
                String mock = System.getProperty("mock");
                if (mock != null && mock.startsWith("return:")) {
                    String result = mock.replace("return:", "");
                    return result;
                }
                //传入参数类型和参数，获得需要的方法，链接服务端进行服务调用
                Invocation invocation = new Invocation(interfaceClass.getName(), method.getName(),
                        new Class[]{String.class}, args);

                HttpClient httpClient = new HttpClient();

                //服务发现
                List<URL> list = MapRemoteRegister.get(interfaceClass.getName());

                //服务调用
                String result = null;
                List<URL> invokedUrls = new ArrayList<>();

                //服务重试
                int max = 3;
                while (max > 0) {
                    //可以采用休眠时间或者异步的形式来实现重试

                    //负载均衡
                    list.remove(invokedUrls);//排除已经调用过的url，重试别的url
                    URL url = LoadBalance.random(list);
                    invokedUrls.add(url);//记录一下已经被调用过的url，重试时考虑换别的url

                    try{
                        result = httpClient.send(url.getHostname(), url.getPort(), invocation);
                    } catch (Exception e) {
                        //服务重试机制，如果达到了重试次数再执行容错机制
                        if (max-- != 0) continue;
                        //容错机制
                        //error-callback = org.example.HelloServiceErrorCallback
                        return "报错了";
                    }
                }


                return result;
            }
        });

        return (T) proxyInstance;
    }
}
