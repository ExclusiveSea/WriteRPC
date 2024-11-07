# WriteRPC
<b>手写RPC框架</b><br>
·创建Consumer、Provider，分别作为服务消费者和提供者<br>
·创建Provider-Common来存储Provider中所用到的接口，Consumer和Provider的pom文件中使用Provider-Common的依赖<br>
·rpc软件包为rpc框架所需要的组件：<br>
  使用Innovation类来传递接口名称，接口中的方法名以及所需要的参数类型列表和参数列表，从而实现参数的动态传递<br>
  URL类动态传递hostname和port，方便完成动态代理和负载均衡<br>
  Innovation和URL都需要使用Stream传递所以都需要序列化<br>
  LoadBalance为负载均衡方法实现，此处采用从url列表中随机取一个url来简单实现负载均衡<br>
  HttpServer是服务端，这里使用Tomcat来进行服务器的创建，也可以使用Netty或Socket，在其中添加Servlet：DispatcherServlet来实现责任链<br>
  HttpClient为客户端，可以读取用户配置，建立与服务端之间的链接<br>
  HttpServerHandler为处理请求的方法，通过本地注册接口方法从而调用方法来获得接口并输出<br>
  通过DispatcherServlet可以组合不同的handler从而实现责任链模式<br>
  通过ProxyFactory创建接口并执行，在其中实现了设置服务mock，链接服务端、服务发现、服务调用、服务重试、负载均衡、容错机制<br>
  LocalRegister是本地注册，通过使用一个map来记录接口名和接口类，可以添加版本号来实现不同版本调用不同的接口<br>
  MapRemoteRegister是远程注册，使用map来记录接口名和url列表，从而实现url的动态调用和负载均衡，因为url的注册和读取是在不同线程中执行的，读取时会读不到添加的url，所以采用临时文件来存储url，注册和读取都对临时文件进行操作（也可以使用Redis来实现）<br>

