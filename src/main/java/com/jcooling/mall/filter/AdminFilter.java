package com.jcooling.mall.filter;

import com.jcooling.mall.common.Constant;
import com.jcooling.mall.model.pojo.User;
import com.jcooling.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
/**
* Cteate by IntelliJ IDEA.
* @author: JingHai
* @data: 2022/04/11
* @time: 10:34:30
* @version: 1.0
* @description: nothing.
*/
public class AdminFilter implements Filter {

    @Autowired
    private UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpSession session = req.getSession();
        User currentUser = (User)session.getAttribute(Constant.SMOOTH_MALL_USER);
        if (currentUser==null){
            PrintWriter out = new HttpServletResponseWrapper((HttpServletResponse) response).getWriter();
            out.write("{\n" +

                    "    \"status\": 10008,\n" +
                    "    \"msg\": \"NEED_LOGIN\",\n" +
                    "    \"data\": null\n" +
                    "}");
            out.flush();//clear the data stream of the buffer
            out.close();
            return;//出错情况下，直接过滤，过滤器不往下执行
        }
        //check admin
        boolean adminRole = userService.checkAdminRole(currentUser);
        if(adminRole){
            //如果是管理员，放行
            chain.doFilter(request,response);
        }else{
            PrintWriter out = new HttpServletResponseWrapper((HttpServletResponse)
                    response).getWriter();
            out.write("{\n" +
                    "    \"status\": 10010,\n" +
                    "    \"msg\": \"NEED_ADMIN\",\n" +
                    "    \"data\": null\n" +
                    "}");
            out.flush();
            out.close();
        }
    }


    @Override
    public void destroy() {

    }
}
