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
 *
 * @author: JingHai
 * @date: 2022/4/29
 * @time: 0:23
 * @description: 用户校验过滤器
 * @version: 1.0
 */
public class UserFilter implements Filter {
    //登录后，能将用户信息保存下来
    public static User currentUser;

    @Autowired
    private UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();
        //1.获取session中的用户
        currentUser = (User) session.getAttribute(Constant.SMOOTH_MALL_USER);
        //2.判断当前用户是否为空，为空则需要登录
        if (currentUser == null) {
            PrintWriter out = new HttpServletResponseWrapper((HttpServletResponse)response).getWriter();
            out.write("{\n" +
                    "    \"status\": 10008,\n" +
                    "    \"msg\": \"NEED_LOGIN\",\n" +
                    "    \"data\": null\n" +
                    "}");
            out.flush();
            out.close();
            return;
        }
        //3.通过用户校验
        filterChain.doFilter(request,response);
    }

    @Override
    public void destroy() {}
}

