package com.jcooling.mall.utils;

import com.jcooling.mall.common.Constant;
import org.apache.tomcat.util.codec.binary.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
* Cteate by IntelliJ IDEA.
* @author: JingHai
* @data: 2022/04/11
* @time: 10:37:10
* @version: 1.0
* @description: nothing.
*/
public class MD5Utils {
    public static String getMD5Str(String strVal) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("md5");
        return Base64.encodeBase64String(md5.digest((strVal+ Constant.SALT).getBytes()));
    }

   /* public static void main(String[] args) {
        String md5Str = null;
        try {
            md5Str = getMD5Str("1234");
            System.out.println(md5Str);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }*/
}
