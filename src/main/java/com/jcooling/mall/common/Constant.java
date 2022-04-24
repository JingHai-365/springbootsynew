package com.jcooling.mall.common;


import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
* Cteate by IntelliJ IDEA.
* @author: JingHai
* @data: 2022/04/11
* @time: 10:29:59
* @version: 1.0
* @description: nothing.
*/

@Component
public class Constant {
    //加盐
    public  static  final String SALT="pzhu=+.z";
    public static final String SMOOTH_MALL_USER="smooth_mall_user";

    public static String FILE_UPLOAD_DIR;

    //不能有static，否则报错
    @Value("${file.upload.dir}")
    public void setFileUploadDir(String fileUploadDir) {
        FILE_UPLOAD_DIR = fileUploadDir;
    }

    public interface ProductListOrderBy{
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price desc","price asc");
    }
}
