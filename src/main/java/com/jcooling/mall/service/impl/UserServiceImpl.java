package com.jcooling.mall.service.impl;

import com.jcooling.mall.exception.JcoolingMallException;
import com.jcooling.mall.exception.JcoolingMallExceptionEnum;
import com.jcooling.mall.model.dao.UserMapper;
import com.jcooling.mall.model.pojo.User;
import com.jcooling.mall.service.UserService;
import com.jcooling.mall.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
/**
* Cteate by IntelliJ IDEA.
* @author: JingHai
* @data: 2022/04/11
* @time: 10:36:46
* @version: 1.0
* @description: nothing.
*/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void register(String username, String password) throws JcoolingMallException {
        User result = userMapper.selectByUsername(username);
        //System.out.println("=================="+relust+"**********************");
        if (result!=null){
            throw new JcoolingMallException(JcoolingMallExceptionEnum.USER_EXISTED);
        }
        User user = new User();
        user.setUsername(username);

        try {
            user.setPassword(MD5Utils.getMD5Str(password));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        int count = userMapper.insertSelective(user);
        if (count==0){
            throw new JcoolingMallException(JcoolingMallExceptionEnum.INSERT_FAILED);
        }
    }

    @Override
    public User login(String username, String password) throws JcoolingMallException {
        String md5Pwd=null;
        try {
            md5Pwd = MD5Utils.getMD5Str(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        System.out.println("**********"+md5Pwd+"***********");
        User user = userMapper.selectLogin(username, md5Pwd);
        if (user==null){
            throw new JcoolingMallException(JcoolingMallExceptionEnum.WRONG_PASSWORD);
        }

        return user;
    }

    @Override
    public void updateInformation(User user) throws JcoolingMallException {
        //update
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount>1){
            throw new JcoolingMallException(JcoolingMallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public boolean checkAdminRole(User user) {
        //1 is ordinary users,2 is administrators
        return user.getRole().equals(2);
    }
}
