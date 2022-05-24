package com.jcooling.mall.service;

import com.jcooling.mall.exception.JcoolingMallException;
import com.jcooling.mall.model.ov.UserVO;
import com.jcooling.mall.model.pojo.User;

import java.util.List;

/**
* Cteate by IntelliJ IDEA.
* @author: JingHai
* @date: 2022/04/11
* @time: 10:36:55
* @version: 1.0
* @description: nothing.
*/
public interface UserService {

    void register(String username, String password) throws JcoolingMallException;
    User login(String username,String password) throws JcoolingMallException;
    public void updateInformation(User user) throws JcoolingMallException;
    public  boolean checkAdminRole(User user);
    List<UserVO> list();
}
