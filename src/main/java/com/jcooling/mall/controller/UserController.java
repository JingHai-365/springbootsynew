package com.jcooling.mall.controller;

import com.jcooling.mall.common.ApiRestResponse;
import com.jcooling.mall.common.Constant;
import com.jcooling.mall.exception.JcoolingMallException;
import com.jcooling.mall.exception.JcoolingMallExceptionEnum;
import com.jcooling.mall.model.pojo.User;
import com.jcooling.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
/**
* Cteate by IntelliJ IDEA.
* @author: JingHai
* @date: 2022/04/11
* @time: 10:31:07
* @version: 1.0
* @description: nothing.
*/
@Controller
public class UserController {

    @Autowired
    private UserService userService;

   @PostMapping("/register")
   @ResponseBody
    public ApiRestResponse register(@RequestParam("username") String username,@RequestParam("password") String password) throws JcoolingMallException {
       if (StringUtils.isEmpty(username)){
           return ApiRestResponse.error(JcoolingMallExceptionEnum.NEED_USER_NAME);
       }
       if (StringUtils.isEmpty(password)){
           return ApiRestResponse.error(JcoolingMallExceptionEnum.NEED_PASSWORD);
       }
       if (password.length()<8||password.length()>16){
           return ApiRestResponse.error(JcoolingMallExceptionEnum.PASSWORD_TOO_LONG_OR_SHORT);
       }
       userService.register(username,password);
       return ApiRestResponse.success();
   }

    @PostMapping("/login")
    @ResponseBody
    public ApiRestResponse login(@RequestParam("username") String username,
                                 @RequestParam("password") String password, HttpSession session) throws JcoolingMallException {
        //username与password的空值校验
        if(StringUtils.isEmpty(username)){
            return ApiRestResponse.error(JcoolingMallExceptionEnum.NEED_USER_NAME);
        }
        if(StringUtils.isEmpty(password)){
            return ApiRestResponse.error(JcoolingMallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(username, password);
        //System.out.println(user.toString()+"*********************************************************");
        //保存用户信息时，不保存密码
        user.setPassword(null);
        session.setAttribute(Constant.SMOOTH_MALL_USER,user);
        return ApiRestResponse.success(user);
    }


    @PostMapping("/user/update")
    @ResponseBody
    public ApiRestResponse updateUserInfo(HttpSession session,@RequestParam String signature) throws JcoolingMallException{
        User currentUser = (User) session.getAttribute(Constant.SMOOTH_MALL_USER);
        if (currentUser == null) {
            return ApiRestResponse.error(JcoolingMallExceptionEnum.NEED_LOING);
        }
        User user = new User();
        user.setId(currentUser.getId());
        user.setPersonalizedSignature(signature);
        userService.updateInformation(user);
        return ApiRestResponse.success();
    }

    @PostMapping("/user/logout")
    @ResponseBody
    public ApiRestResponse logout(HttpSession session) throws JcoolingMallException{
       session.removeAttribute(Constant.SMOOTH_MALL_USER);
       return ApiRestResponse.success();
    }

    @PostMapping("/adminLogin")
    @ResponseBody
    public ApiRestResponse adminLogin(@RequestParam("username") String username,@RequestParam("password") String password,HttpSession session) throws JcoolingMallException {
        //username与password的空值校验
        if(StringUtils.isEmpty(username)){
            return ApiRestResponse.error(JcoolingMallExceptionEnum.NEED_USER_NAME);
        }
        if(StringUtils.isEmpty(password)){
            return ApiRestResponse.error(JcoolingMallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(username, password);
        if (userService.checkAdminRole(user)){
            user.setPassword(null);
            session.setAttribute(Constant.SMOOTH_MALL_USER,user);
            return ApiRestResponse.success(user);
        }else {
            return ApiRestResponse.error(JcoolingMallExceptionEnum.NEED_ADMIN);
        }
    }

}
