package org.example.protocol;


import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 可以使得handler有多种处理请求的方式
 */
public class DispatcherServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp){
        //责任链模式，可以加很多handler去处理请求
        new HttpServerHandler().handler(req, resp);
    }
}
